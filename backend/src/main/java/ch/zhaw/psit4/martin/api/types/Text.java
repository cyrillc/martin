package ch.zhaw.psit4.martin.api.types;

/**
 * Distributed Text type for MArtIn API.
 * 
 * This is a String wrapper for the MArtIn API.
 *
 * @version 0.0.1-SNAPSHOT
 */
public class Text extends MartinType implements IMartinType {
	public Text(String data) {
		super(data);
	}
}
