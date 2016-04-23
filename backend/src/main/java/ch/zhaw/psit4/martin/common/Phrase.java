package ch.zhaw.psit4.martin.common;

import ch.zhaw.psit4.martin.api.types.Date;
import ch.zhaw.psit4.martin.api.types.Location;
import ch.zhaw.psit4.martin.api.types.Person;
import ch.zhaw.psit4.martin.api.types.Text;
import ch.zhaw.psit4.martin.api.types.Time;
import ch.zhaw.psit4.martin.api.types.Timestamp;

public class Phrase {
	private String type;
	private String value;
	private String iMartinType;

	public Phrase(String type, String value) {
		this.type = type;
		this.value = value;
	}
	
	public String getType() {
		return type;
	}

	public String getIMartinType(){
		switch (type) {
		case "LOCATION":
			return Location.class.getName();
		case "PERSON":
			return Person.class.getName();
		case "DATE":
			return Date.class.getName();
		case "TIME":
			return Time.class.getName();
		case "ORGANIZATION":
			// TODO: Add Organization IMartinType
			return Text.class.getName();
		case "MISC":
			// TODO: Add Misc IMartinType
			return Text.class.getName();
		case "MONEY":
			// TODO: Add Money IMartinType
			return Text.class.getName();
		case "NUMBER":
			// TODO: Add Number IMArtionType
			return Text.class.getName();
		case "ORDINAL":
			// TODO: Add Ordinal IMartinType
			return Text.class.getName();
		case "PERCENT":
			// TODO: Add Percent IMArtinType
			return Text.class.getName();
		case "DURATION":
			// TODO: Add Duration IMartionType
			return Text.class.getName();
		case "SET":
			// TODO: Add Set IMartionType
			return Text.class.getName();
		default:
			return Text.class.getName();
		}
	}

	public String getValue() {
		return value;
	}

}