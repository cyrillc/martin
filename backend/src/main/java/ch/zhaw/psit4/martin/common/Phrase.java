package ch.zhaw.psit4.martin.common;

import ch.zhaw.psit4.martin.api.types.Date;
import ch.zhaw.psit4.martin.api.types.Duration;
import ch.zhaw.psit4.martin.api.types.Location;
import ch.zhaw.psit4.martin.api.types.Misc;
import ch.zhaw.psit4.martin.api.types.Money;
import ch.zhaw.psit4.martin.api.types.Ordinal;
import ch.zhaw.psit4.martin.api.types.Organization;
import ch.zhaw.psit4.martin.api.types.Percent;
import ch.zhaw.psit4.martin.api.types.Person;
import ch.zhaw.psit4.martin.api.types.Set;
import ch.zhaw.psit4.martin.api.types.Text;
import ch.zhaw.psit4.martin.api.types.Time;

public class Phrase {
	private String type;
	private String value;

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
			return Organization.class.getName();
		case "MISC":
			return Misc.class.getName();
		case "MONEY":
			return Money.class.getName();
		case "NUMBER":
			return Number.class.getName();
		case "ORDINAL":
			return Ordinal.class.getName();
		case "PERCENT":
			return Percent.class.getName();
		case "DURATION":
			return Duration.class.getName();
		case "SET":
			return Set.class.getName();
		default:
			return Text.class.getName();
		}
	}

	public String getValue() {
		return value;
	}

}