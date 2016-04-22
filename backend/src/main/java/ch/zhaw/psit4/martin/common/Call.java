package ch.zhaw.psit4.martin.common;

import java.util.HashMap;
import java.util.Map;

import ch.zhaw.psit4.martin.api.types.IMartinType;
import ch.zhaw.psit4.martin.pluginlib.db.function.Function;
import ch.zhaw.psit4.martin.pluginlib.db.plugin.Plugin;

/**
* This class holds information about a Plugin feature call. It holds the
* plugin and feature name along with the required and optional arguments.
*
* @version 0.1
**/
public class Call {
	private Plugin plugin;
	private Function feature;
	private Map<String, IMartinType> arguments;
	
	public Call(){
		this.arguments = new HashMap<String, IMartinType>();
	}

	public Plugin getPlugin() {
		return this.plugin;
	}

	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}

	public Function getFeature() {
		return this.feature;
	}

	public void setFeature(Function feature) {
		this.feature = feature;
	}
	
	/**
	 * Adds an argument. 
	 * @param key the argument name
	 * @param value the argument content packed into a IMartinType
	 */
	public void addArgument(String key, IMartinType value){
		this.arguments.put(key, value);
	}
	
	public void setParameters(Map<String, IMartinType> parameters){
		this.arguments = parameters;
	}
	
	/**
	 * @return A map of all arguments.
	 */
	public Map<String, IMartinType> getArguments(){
		return arguments;
	}

}
