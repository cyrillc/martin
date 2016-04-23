package ch.zhaw.psit4.martin.api.types;

/**
 * Distributed generic Type for MArtIn objects.
 * 
 * Martin wraps Objects, that are passed on to plugins in this container for
 * easy handling.
 *
 * @version 0.0.1-SNAPSHOT
 */
public interface IMartinType {
	/**
	 * Serialize this Object into a string in Text format.
	 * 
	 * @return The serialized {@link String}.
	 */
	@Override
	public String toString();
	
	/**
	 * De-serialize this object from a {@link String} in Text format.
	 * 
	 * @param data
	 *            The string filed with data to deserialize.
	 */
	public void fromString(String data) throws IMartinTypeInstanciationException;

	/**
	 * Serialize this Object into a string in JSON format.
	 * 
	 * @return The serialized {@link String}.
	 */
	public String toJson();
	
	/**
	 * De-serialize this object from a {@link String} in JSON format.
	 * 
	 * @param json
	 *            The string filed with data to deserialize.
	 */
	public void fromJSON(String json);

	/**
	 * Verifies, if the string-data can be converted to this type. This function
	 * should shall not use extensive API's and will be called a lot. Build this
	 * function lightweigth.
	 * 
	 * @param data
	 *            The string filed with data to deserialize.
	 */
	public boolean isInstancaeableWith(String data);

	/**
	 * @return If the instance is valid and can be used for further operation.
	 */
	public boolean isValid();
}
