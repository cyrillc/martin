package ch.zhaw.psit4.martin.requestprocessor;

import java.util.HashMap;
import java.util.Map;

import ch.zhaw.psit4.martin.api.types.IMartinType;
import ch.zhaw.psit4.martin.pluginlib.db.function.Function;
import ch.zhaw.psit4.martin.pluginlib.db.keyword.Keyword;
import ch.zhaw.psit4.martin.pluginlib.db.plugin.Plugin;

/**
 * This class is used to prepare possible results. The state of the
 * PossibleResult may not be complete and will be changed over the analyzation
 * of the code. A PossibleResult may be thrown away if not all criteria for a
 * valid Call is being met.
 * 
 * @author simonflepp
 *
 */
class PossibleResult {
	private Plugin plugin;
	private Function function;
	private HashMap<String, IMartinType> parameters = new HashMap<>();
	private HashMap<Integer, Keyword> matchingKeywords = new HashMap<>();

	/**
	 * Is used to determine how important the keyword-count for a usable result
	 * is.
	 */
	private static final Integer RELEVANCE_WEIGHT_KEYWORD_COUNT = 10;
	// TODO: Add more weights to get better results

	public PossibleResult(Plugin plugin, Function function) {
		this.plugin = plugin;
		this.function = function;
	}
	
	/**
	 * @return Returns a number which can be compared with other PossibleRequests to determine 
	 * the best result
	 */
	public Integer getRelevance() {
		return RELEVANCE_WEIGHT_KEYWORD_COUNT * this.matchingKeywords.size();
	}

	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public void setFunction(Function function) {
		this.function = function;
	}

	public Function getFunction() {
		return function;
	}

	public void addMatchingKeyword(Keyword keyword) {
		matchingKeywords.put(keyword.getId(), keyword);
	}

	public Map<Integer, Keyword> getMatchingKeywords() {
		return matchingKeywords;
	}

	public void addParameter(String key, IMartinType value) {
		parameters.put(key, value);
	}

	public Map<String, IMartinType> getParameters() {
		return parameters;
	}
}
