package ch.zhaw.psit4.martin.api.types;

import java.lang.reflect.Constructor;
import org.json.JSONObject;

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
	public static IMartinType fromString(String type, String data) throws IMartinTypeInstanciationException{
		try {
			Constructor<? extends IMartinType> constructor = Class.forName(type)
					.asSubclass(IMartinType.class).getConstructor(String.class);
			return constructor.newInstance(data);
		} catch (Exception e) {
			throw new IMartinTypeInstanciationException(e);
		}
	}

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
	 * @throws IMartinTypeInstanciationException
	 */
	public static IMartinType fromJSON(String json) throws IMartinTypeInstanciationException {
		try {
			JSONObject jsonObject = new JSONObject(json);
			Constructor<? extends IMartinType> constructor = Class.forName(jsonObject.getString("type"))
					.asSubclass(IMartinType.class).getConstructor(String.class);
			return constructor.newInstance(jsonObject.getString("data"));
		} catch (Exception e) {
			throw new IMartinTypeInstanciationException(e);
		}
	}
}
