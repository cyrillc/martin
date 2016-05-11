package ch.zhaw.psit4.martin.requestprocessor;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ch.zhaw.psit4.martin.api.typefactory.BaseTypeFactory;
import ch.zhaw.psit4.martin.api.types.EBaseType;
import ch.zhaw.psit4.martin.api.types.IBaseType;
import ch.zhaw.psit4.martin.api.types.BaseTypeInstanciationException;
import ch.zhaw.psit4.martin.common.Call;
import ch.zhaw.psit4.martin.common.ExtendedRequest;
import ch.zhaw.psit4.martin.common.Sentence;
import ch.zhaw.psit4.martin.models.*;
import ch.zhaw.psit4.martin.models.repositories.MKeywordRepository;
import ch.zhaw.psit4.martin.common.Phrase;
import edu.stanford.nlp.pipeline.StanfordCoreNLPClient;

/**
 * This class is responible for extending a request to a computer readable
 * format. It searches for possible function calls with their corresponding
 * arguments.
 *
 * @version 0.1
 **/
public class RequestProcessor implements IRequestProcessor {
	
	@Autowired
	private MKeywordRepository keywordRepository;

	@Autowired
	private StanfordCoreNLPClient stanfordNLP;

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
	public ExtendedRequest extend(MRequest request) {	
		List<PossibleCall> possibleCalls = new ArrayList<>();

		Sentence sentence = new Sentence(request.getCommand(), stanfordNLP);

		// Find possible Calls by keywords
		addPossibleCallsWithKeywords(possibleCalls, sentence.getWords());

		// Resolve parameters
		resolveParameters(possibleCalls, sentence);

		// Sort by relevance
		possibleCalls
				.sort((PossibleCall result1, PossibleCall result2) -> result1.getRelevance() - result2.getRelevance());

		// Create final ExtendedRequest
		ExtendedRequest extendedRequest = new ExtendedRequest();
		extendedRequest.setInput(request);
		extendedRequest.setSentence(sentence);

		for (PossibleCall possibleCall : possibleCalls) {
			// Create Call
			Call call = new Call();
			call.setPlugin(possibleCall.getPlugin());
			call.setFunction(possibleCall.getFunction());
			call.setParameters(possibleCall.getParameters());

			extendedRequest.addCall(call);
		}

		return extendedRequest;
	}

	/**
	 * Searches the database for plugins/functions with the keywords provided
	 * and adds them to the list.
	 * 
	 * @param possibleCalls
	 *            List of possible results.
	 * @param words
	 *            words to be matched with the keywords.
	 * @return the extended list
	 */
	private List<PossibleCall> addPossibleCallsWithKeywords(List<PossibleCall> possibleCalls, List<String> words) {

		for (String word : words) {

			MKeyword keyword = keywordRepository.findByKeywordIgnoreCase(word);

			if (keyword != null) {
				for (MFunction function : keyword.getFunctions()) {
					MPlugin plugin = function.getPlugin();

					Optional<PossibleCall> optionalPossibleResult = possibleCalls.stream()
							.filter(o -> o.getPlugin().getId() == plugin.getId())
							.filter(o -> o.getFunction().getId() == function.getId()).findFirst();

					if (optionalPossibleResult.isPresent()) {
						optionalPossibleResult.get().addMatchingKeyword(keyword);
					} else {
						PossibleCall possibleCall = new PossibleCall(plugin, function);
						possibleCall.addMatchingKeyword(keyword);
						possibleCalls.add(possibleCall);
					}
				}
			}

		}

		return possibleCalls;
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
	 * @param possibleCalls
	 *            A list of possible results, whose parameters should be filled
	 * @param sentence
	 *            A sentence which provides the base for the parameter-finding
	 * @return A list of PossibleResults with their corresponding parameters
	 *         filled as good as possible
	 */
	public List<PossibleCall> resolveParameters(List<PossibleCall> possibleCalls, Sentence sentence) {
		for (PossibleCall possibleCall : possibleCalls) {
			MFunction function = possibleCall.getFunction();

			for (MParameter parameter : function.getParameters()) {
				// Create instance of IMartinType for requested type
				IBaseType parameterValue = getParameterValue(parameter, sentence);
				possibleCall.addParameter(parameter.getName(), parameterValue);
			}
		}

		return possibleCalls;
	}

	public IBaseType getParameterValue(MParameter parameter, Sentence sentence) {
		try {

			Integer possibilitiesLeft;
			do {
				// Perform Name Entity Recognition
				String data = "";

				if (EBaseType.TIMESTAMP.equals(EBaseType.fromClassName(parameter.getType()))) {
					// Timestamp consists of Date and Time
					Phrase date = sentence.popPhraseOfType(EBaseType.DATE);
					Phrase time = sentence.popPhraseOfType(EBaseType.TIME);

					possibilitiesLeft = sentence.getPhrasesOfType(EBaseType.DATE).size()
							+ sentence.getPhrasesOfType(EBaseType.TIME).size();

					data = (date.getValue() + " " + time.getValue()).trim();
				} else {
					// All the rest can be resolved directly
					Phrase phrase = sentence.popPhraseOfType(EBaseType.fromClassName(parameter.getType()));

					possibilitiesLeft = sentence.getPhrasesOfType(EBaseType.fromClassName(parameter.getType()))
							.size();

					if (phrase != null) {
						data = phrase.getValue();
					}
				}

				if (!"".equals(data)) {
					try {
						IBaseType parameterValue = BaseTypeFactory
								.fromType(EBaseType.fromClassName(parameter.getType()), data);
						LOG.info("\n Parameter found via Name Entity Recognition: " + parameterValue.toJson());
						return parameterValue;
					} catch (BaseTypeInstanciationException e) {
						LOG.debug(e);
					}
				}
			} while (possibilitiesLeft > 0);
		} catch (Exception e) {
			LOG.debug(e);
			LOG.error("The IMartinType '" + parameter.getType() + "' could not be found.");
		}
		return null;
	}

}
