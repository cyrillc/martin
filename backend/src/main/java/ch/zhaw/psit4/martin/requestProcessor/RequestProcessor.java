package ch.zhaw.psit4.martin.requestProcessor;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ch.zhaw.psit4.martin.api.types.IMartinType;
import ch.zhaw.psit4.martin.api.Feature;
import ch.zhaw.psit4.martin.api.types.*;
import ch.zhaw.psit4.martin.api.util.Pair;
import ch.zhaw.psit4.martin.common.Call;
import ch.zhaw.psit4.martin.common.ExtendedRequest;
import ch.zhaw.psit4.martin.common.Request;
import ch.zhaw.psit4.martin.pluginlib.IPluginLibrary;
import ch.zhaw.psit4.martin.pluginlib.db.function.Function;
import ch.zhaw.psit4.martin.pluginlib.db.function.FunctionService;
import ch.zhaw.psit4.martin.pluginlib.db.keyword.Keyword;
import ch.zhaw.psit4.martin.pluginlib.db.parameter.Parameter;
import ch.zhaw.psit4.martin.pluginlib.db.plugin.Plugin;
import ch.zhaw.psit4.martin.pluginlib.db.plugin.PluginService;

/**
 * This class is responible for extending a request to a computer readable
 * format. It searches for possible function calls with their corresponding
 * arguments.
 *
 * @version 0.1
 **/
public class RequestProcessor implements IRequestProcessor {

	@Autowired
	private IPluginLibrary library;
	
	@Autowired
	private FunctionService functionService;

	private static final Log LOG = LogFactory.getLog(RequestProcessor.class);
	
	
	/**
	 * Extends a request from a basic command and tries to determine possible
	 * module calls. In oder for this method to work, set the library beforehand
	 * with {@code setLibrary}
	 * 
	 * @param request
	 *            Raw request to be extended
	 * @return Returns an ExtendedRequest with original-request and a possible
	 *         executable function calls.
	 */
	@Override
	public ExtendedRequest extend(Request request) throws Exception {	
		List<PossibleResult> possibleResults = new ArrayList<>();
		
		Sentence sentence = new Sentence(request.getCommand());

		// Find possible Plugins/Functions by keywords
		

		addPossibleRequestsWithKeywords(possibleResults, sentence.getWords());
		resolveParameters(possibleResults, sentence);
		
		
		
		possibleResults.sort((PossibleResult result1, PossibleResult result2) -> result1.getMatchingKeywords().size()
				- result2.getMatchingKeywords().size());
		
		
		
		// Create final ExtendedRequest
		ExtendedRequest extendedRequest = new ExtendedRequest();
		extendedRequest.setInput(request);
		
		for(PossibleResult possibleResult : possibleResults){
			// Create Call
			Call call = new Call();
			call.setPlugin(possibleResult.getPlugin());
			call.setFeature(possibleResult.getFunction());
			call.setParameters(possibleResult.getParameters());
			
			extendedRequest.addCall(call);
		}
		
		return extendedRequest;
	}
	

	private List<PossibleResult> addPossibleRequestsWithKeywords(List<PossibleResult> possibleResults, List<Word> words) {
		
		for (Word word : words) {
		
			List<Object[]> functionsKeywords = functionService.getByKeyword(word.toString());
			for (Object[] functionsKeyword : functionsKeywords) {
				Function function = (Function)functionsKeyword[0];
				Plugin plugin = function.getPlugin();

				Keyword keyword = (Keyword)functionsKeyword[1];
				
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
	
	private List<PossibleResult> resolveParameters(List<PossibleResult> possibleResults, Sentence sentence){
		for(PossibleResult possibleResult : possibleResults){
			Function function = possibleResult.getFunction();
			
			for(Parameter parameter : function.getParameter()){	
				// Create instance of IMartinType for 
				try {
					IMartinType parameterValue = Class.forName(parameter.getType()).asSubclass(IMartinType.class).newInstance();

					for(Word word : sentence.getWords()){
						if(parameterValue.isInstancaeableWith(word.toString())){
							parameterValue.fromString(word.toString());
							
							LOG.info("Parameter resolved: " + parameter.getName() + " | " + parameter.getType() + " | " + word.toString() + " | " + parameterValue.toString());
						
							break;
						}
					}
					
					if(parameterValue.isInstance()){
						possibleResult.addParameter(parameter.getName(), parameterValue);
					} else {
						throw new Exception("Parameter " + parameter.getName() + " cannot be found.");
					}
				} catch(Exception e){
					LOG.info(e.getMessage());
					break;
				}
			}
			
		}
		
		return possibleResults;
	}


}
