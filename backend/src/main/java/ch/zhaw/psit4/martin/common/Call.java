package ch.zhaw.psit4.martin.common;

import java.util.Map;

import ch.zhaw.psit4.martin.api.types.IMartinType;

/**
* This class holds information about a Plugin feature call. It holds the
* plugin and feature name along with the required and optional arguments.
*
* @version 0.1
**/
public class Call {
	private String plugin;
	private String feature;
	private Map<String, IMartinType> requiredArguments;
	private Map<String, IMartinType> optionalArguments;

	public String getPlugin() {
		return this.plugin;
	}

	public void setPlugin(String plugin) {
		this.plugin = plugin;
	}

	public String getFeature() {
		return this.feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}
	
	/**
	 * Adds a required argument. 
	 * @param key the argument name
	 * @param value the argument content packed into a IMartinType
	 */
	public void addRequiredArgument(String key, IMartinType value){
		this.requiredArguments.put(key, value);
	}
	
	/**
	 * @return A map of all required arguments.
	 */
	public Map<String, IMartinType> getRequiredArguments(){
		return requiredArguments;
	}
	
	/**
	 * Adds an optional argument.
	 * @param key the argument name
	 * @param value the argument content packed into a IMartinType
	 */
	public void addOptionalArgument(String key, IMartinType value){
		this.optionalArguments.put(key, value);
	}
	
	/**
	 * 
	 * @return A map of all optional arguments.
	 */
	public Map<String, IMartinType> getOptionalArguments(){
		return requiredArguments;
	}

}
