package ch.zhaw.psit4.martin.common;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.zhaw.psit4.martin.api.types.IMartinType;
import ch.zhaw.psit4.martin.api.util.Pair;

/**
* This class holds extended information about the request. It holds a possible 
* function call along with the required and optional arguments used to call 
* the plugin feature.
*
* @version 0.1
**/
public class ExtendedRequest {
	private String command;
	private String plugin;
	private String feature;
	private Map<String, IMartinType> requiredArguments;
	private Map<String, IMartinType> optionalArguments;

	public String getCommand() {
		return this.command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

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
