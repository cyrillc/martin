package ch.zhaw.psit4.martin.requestProcessor;

import java.util.HashMap;
import java.util.Map;

import ch.zhaw.psit4.martin.api.types.IMartinType;
import ch.zhaw.psit4.martin.pluginlib.db.function.Function;
import ch.zhaw.psit4.martin.pluginlib.db.keyword.Keyword;
import ch.zhaw.psit4.martin.pluginlib.db.plugin.Plugin;

/**
 * This class holds the information for a possible result.
 * @author simonflepp
 *
 */
class PossibleResult {
	private Plugin plugin;
	private Function function;
	private HashMap<String, IMartinType> parameters = new HashMap<>();
	private HashMap<Integer, Keyword> matchingKeywords = new HashMap<>();
	
	private static final Integer RELEVANCE_WEIGHT_KEYWORD_COUNT = 10;
	
	public PossibleResult(Plugin plugin, Function function){
		this.plugin = plugin;
		this.function = function;
	}
	
	
	public void setPlugin(Plugin plugin){
		this.plugin = plugin;
	}
	
	public Plugin getPlugin(){
		return plugin;
	}
	
	public void setFunction(Function function){
		this.function = function;
	}
	
	public Function getFunction(){
		return function;
	}
	
	public void addMatchingKeyword(Keyword keyword){
		matchingKeywords.put(keyword.getId(), keyword);
	}
	
	public Map<Integer, Keyword> getMatchingKeywords(){
		return matchingKeywords;
	}
	
	public void addParameter(String key, IMartinType value){
		parameters.put(key, value);
	}
	
	public Map<String, IMartinType> getParameters(){
		return parameters;
	}
	
	public Integer getRelevance(){
		return RELEVANCE_WEIGHT_KEYWORD_COUNT * this.matchingKeywords.size();
	}
}
