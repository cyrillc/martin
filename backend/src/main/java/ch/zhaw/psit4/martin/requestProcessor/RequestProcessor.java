package ch.zhaw.psit4.martin.requestProcessor;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.zhaw.psit4.martin.api.types.IMartinType;
import ch.zhaw.psit4.martin.api.types.*;
import ch.zhaw.psit4.martin.api.util.Pair;
import ch.zhaw.psit4.martin.common.Call;
import ch.zhaw.psit4.martin.common.ExtendedRequest;
import ch.zhaw.psit4.martin.common.Request;
import ch.zhaw.psit4.martin.pluginlib.IPluginLibrary;

/**
 * This class is responible for extending a request to a computer readable
 * format. It searches for possible function calls with their corresponding
 * arguments.
 *
 * @version 0.1
 **/
public class RequestProcessor implements IRequestProcessor {
	private IPluginLibrary library;

	/**
	 * Sets the library to be queried.
	 */
	public void setLibrary(IPluginLibrary library) {
		this.library = library;
	}
	
	/**
	 * Searches the plugin-library for matching plugin features for a list of
	 * keywords.
	 * 
	 * @param keywords
	 *            A list of keywords to be searched in the library.
	 * @return
	 */
	private Pair<String, String> getFeatureByKeywords(String[] keywords) {
		Map<String, Pair<String, String>> featureList = new HashMap<String, Pair<String, String>>();
		Map<String, Integer> featureCount = new HashMap<String, Integer>();
		List<Pair<String, String>> queryResult = new ArrayList<Pair<String, String>>();

		// Get features by keywords and count them
		for (String keyword : keywords) {

			try {
				queryResult = this.library.queryFunctionsByKeyword(keyword);
			} catch (Exception e) {
				continue;
			}

			for (Pair<String, String> feature : queryResult) {
				String key = feature.first + "." + feature.second;

				featureList.put(key, feature);

				if (featureCount.containsKey(key)) {
					featureCount.put(key, featureCount.get(key) + 1);
				} else {
					featureCount.put(key, 1);
				}
			}
		}

		// Get most frequent Plugin / Feature
		Integer highestCount = 0;
		String mostFrequentKey = null;
		for (String key : featureCount.keySet()) {
			if (highestCount < featureCount.get(key)) {
				mostFrequentKey = key;
				highestCount = featureCount.get(key);
			}
		}

		if (featureList.isEmpty()) {
			return null;
		} else {
			return featureList.get(mostFrequentKey);
		}
	}

	/**
	 * Searches for parameters in the command string and returns IMartinType
	 * wrapped argument content.
	 * 
	 * @param parameterName
	 *            name of parameter to be searched
	 * @param command
	 *            command string to be searched
	 * @param martinType
	 *            type to be returned
	 * @return
	 */
	private IMartinType getParameterFromCommand(String parameterName, String command, String martinType) {
		Pattern pattern = Pattern.compile("(" + parameterName +")\\s([^\\s]+)");
		Matcher matcher = pattern.matcher(command.toLowerCase());

		if (matcher.find()) {
			// ToDo: Return corresponding martinType. At the moment only Text
			// elements are returned.
			return new Text(matcher.group(2));
		} else {
			return null;
		}
	}

	/**
	 * Extends a request from a basic command and tries to determine possible
	 * module calls.
	 * 
	 * @param request
	 *            Raw request to be extended
	 * @return Returns an ExtendedRequest with original-request and a possible
	 *         executable function calls.
	 */
	@Override
	public ExtendedRequest extend(Request request) throws Exception {
		ExtendedRequest extendedRequest = new ExtendedRequest();
		extendedRequest.setInput(request);

		String[] keywords = request.getCommand().toLowerCase().split(" ");

		Pair<String, String> pluginFeature = this.getFeatureByKeywords(keywords);

		if (pluginFeature != null) {
			String plugin = pluginFeature.first;
			String feature = pluginFeature.second;
			
			Call call = new Call();
			call.setFeature(feature);
			call.setPlugin(plugin);
		
			Map<String, String> parameters = this.library.queryFunctionArguments(plugin, feature);

			for (String key : parameters.keySet()) {
				call.addRequiredArgument(key,
						this.getParameterFromCommand(key, request.getCommand(), parameters.get(key)));
			}
			
			extendedRequest.addCall(call);
		} else {
			throw new Exception("No module found for this command.");
		}

		return extendedRequest;
	}

}
