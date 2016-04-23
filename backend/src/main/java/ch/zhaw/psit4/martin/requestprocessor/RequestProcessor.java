package ch.zhaw.psit4.martin.requestprocessor;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ch.zhaw.psit4.martin.api.types.Date;
import ch.zhaw.psit4.martin.api.types.IMartinType;
import ch.zhaw.psit4.martin.api.types.Time;
import ch.zhaw.psit4.martin.api.types.Timestamp;
import ch.zhaw.psit4.martin.common.Call;
import ch.zhaw.psit4.martin.common.ExtendedRequest;
import ch.zhaw.psit4.martin.common.Request;
import ch.zhaw.psit4.martin.common.Sentence;
import ch.zhaw.psit4.martin.common.Phrase;
import ch.zhaw.psit4.martin.pluginlib.db.function.Function;
import ch.zhaw.psit4.martin.pluginlib.db.function.FunctionService;
import ch.zhaw.psit4.martin.pluginlib.db.keyword.Keyword;
import ch.zhaw.psit4.martin.pluginlib.db.parameter.Parameter;
import ch.zhaw.psit4.martin.pluginlib.db.plugin.Plugin;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * This class is responible for extending a request to a computer readable
 * format. It searches for possible function calls with their corresponding
 * arguments.
 *
 * @version 0.1
 **/
public class RequestProcessor implements IRequestProcessor {

	@Autowired
	private FunctionService functionService;

	@Autowired

	private StanfordCoreNLP stanfordNLP;

	private static final Log LOG = LogFactory.getLog(RequestProcessor.class);

	/**
	 * Extends a request from a basic command and tries to determine possible
	 * function calls.
	 * 
	 * @param request
	 *            Raw request to be extended
	 * @return Returns an ExtendedRequest with original-request and a possible
	 *         executable function calls.
	 */
	@Override
	public ExtendedRequest extend(Request request) {
		List<PossibleResult> possibleResults = new ArrayList<>();

		Sentence sentence = new Sentence(request.getCommand(), stanfordNLP);

		// Find possible Plugins/Functions by keywords
		addPossibleRequestsWithKeywords(possibleResults, sentence.getWords());

		// Resolve parameters
		resolveParameters(possibleResults, sentence);

		// Sort by relevance
		possibleResults.sort(
				(PossibleResult result1, PossibleResult result2) -> result1.getRelevance() - result2.getRelevance());

		// Create final ExtendedRequest
		ExtendedRequest extendedRequest = new ExtendedRequest();
		extendedRequest.setInput(request);

		for (PossibleResult possibleResult : possibleResults) {
			// Create Call
			Call call = new Call();
			call.setPlugin(possibleResult.getPlugin());
			call.setFeature(possibleResult.getFunction());
			call.setParameters(possibleResult.getParameters());

			extendedRequest.addCall(call);
		}

		return extendedRequest;
	}

	/**
	 * Searches the database for plugins/functions with the keywords provided
	 * and adds them to the list.
	 * 
	 * @param possibleResults
	 *            List of possible results.
	 * @param words
	 *            words to be matched with the keywords.
	 * @return the extended list
	 */
	private List<PossibleResult> addPossibleRequestsWithKeywords(List<PossibleResult> possibleResults, String[] words) {

		for (String word : words) {

			List<Object[]> functionsKeywords = functionService.getByKeyword(word);
			for (Object[] functionsKeyword : functionsKeywords) {
				Function function = (Function) functionsKeyword[0];
				Plugin plugin = function.getPlugin();

				Keyword keyword = (Keyword) functionsKeyword[1];

				Optional<PossibleResult> optionalPossibleResult = possibleResults.stream()
						.filter(o -> o.getPlugin().getId() == plugin.getId())
						.filter(o -> o.getFunction().getId() == function.getId()).findFirst();

				if (optionalPossibleResult.isPresent()) {
					optionalPossibleResult.get().addMatchingKeyword(keyword);
				} else {
					PossibleResult possibleResult = new PossibleResult(plugin, function);
					possibleResult.addMatchingKeyword(keyword);
					possibleResults.add(possibleResult);
				}
			}
		}

		return possibleResults;
	}

	/**
	 * This function tries to determine the best word or phrase in the provided
	 * sentence to fit into an argument that is needed from the function. It
	 * uses the Stanford CoreNLP library to search for IMartinTypes in the
	 * sentence.
	 * 
	 * The result of the parameter-filling may not be perfect and is heavy
	 * dependent on the provided sentence. If the sentence has bad spelling or
	 * is grammatically incorrect, the results may be not as good as expected.
	 * The more details and contextual consistent the sentence is, the better
	 * the results.
	 * 
	 * @param possibleResults
	 *            A list of possible results, whose parameters should be filled
	 * @param sentence
	 *            A sentence which provides the base for the parameter-finding
	 * @return A list of PossibleResults with their corresponding parameters
	 *         filled as good as possible
	 */
	public List<PossibleResult> resolveParameters(List<PossibleResult> possibleResults, Sentence sentence) {
		for (PossibleResult possibleResult : possibleResults) {
			Function function = possibleResult.getFunction();

			for (Parameter parameter : function.getParameter()) {
				// Create instance of IMartinType for requested type
				IMartinType parameterValue = getParameterValue(parameter, sentence);
				if (parameterValue != null && parameterValue.isValid()) {
					possibleResult.addParameter(parameter.getName(), parameterValue);
				}
			}
		}

		return possibleResults;
	}

	public IMartinType getParameterValue(Parameter parameter, Sentence sentence){
		try {
			IMartinType parameterValue = Class.forName(parameter.getType()).asSubclass(IMartinType.class)
					.newInstance();

			Integer possibilitiesLeft;
			do {
				// Perform Name Entity Recognition
				String data = "";
				
				if (Timestamp.class.getName().equals(parameter.getType())) {
					// Timestamp consists of Date and Time
					Phrase date = sentence.popPhraseOfIMartinType(Date.class.getName());
					Phrase time = sentence.popPhraseOfIMartinType(Time.class.getName());
					
					possibilitiesLeft = sentence.getPhrasesOfIMartionType(Date.class.getName()).size() + sentence.getPhrasesOfIMartionType(Time.class.getName()).size();
	
					data = (date.getValue() + " " + time.getValue()).trim();
				} else {
					// All the rest can be resolved directly
					Phrase phrase = sentence.popPhraseOfIMartinType(parameter.getType());
					
					possibilitiesLeft = sentence.getPhrasesOfIMartionType(parameter.getType()).size();
	
					if(phrase != null) {
						data = phrase.getValue();
					}
				}
	
				if(data != "" && parameterValue.isInstancaeableWith(data)) {
					parameterValue.fromString(data);
	
					LOG.info("\n Parameter found via Name Entity Recognition: { " + "\n    name:          '"
							+ parameter.getName() + "', " + "\n    value:         '" + parameterValue.toString()
							+ "'" + "\n    type:          '" + parameter.getType() + "\n }");
					return parameterValue;
				}
			} while(possibilitiesLeft > 0);
		} catch (Exception e) {
			LOG.debug(e);
			LOG.error("The IMartinType '" + parameter.getType() + "' could not be found.");
		}
		return null;
	}

}
