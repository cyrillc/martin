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
    /**De-serialize this object from a {@link String} in JSON format.
     * @param data The string filed with data to deserialize.
     */
    public void fromString(String data);

    /**Serialize this Object into a string in JSON format.
     * @return The serialized {@link String}.
     */
    @Override
    public String toString();
}
