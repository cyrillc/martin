package ch.zhaw.psit4.martin.api.types;

import java.util.HashMap;
import java.util.Map;

public enum EMartinType {
	DATE(MartinDate.class.getName()), DURATION(MartinDuration.class.getName()), LOCATION(MartinLocation.class.getName()), MISC(
			MartinMisc.class.getName()), MONEY(MartinMoney.class.getName()), NUMBER(MartinNumber.class.getName()), ORDINAL(
					MartinOrdinal.class.getName()), ORGANIZATION(MartinOrganization.class.getName()), PERCENT(
							MartinPercent.class.getName()), PERSON(MartinPerson.class.getName()), SET(MartinSet.class.getName()), TEXT(
									MartinText.class.getName()), TIME(
											MartinTime.class.getName()), TIMESTAMP(MartinTimestamp.class.getName());

	private static final Map<String, String> NERTAGS;

	static {
		NERTAGS = new HashMap<>();
		NERTAGS.put(MartinDate.class.getName(), "DATE");
		NERTAGS.put(MartinDuration.class.getName(), "DURATION");
		NERTAGS.put(MartinLocation.class.getName(), "LOCATION");
		NERTAGS.put(MartinMisc.class.getName(), "MISC");
		NERTAGS.put(MartinMoney.class.getName(), "MONEY");
		NERTAGS.put(MartinNumber.class.getName(), "NUMBER");
		NERTAGS.put(MartinOrdinal.class.getName(), "ORDINAL");
		NERTAGS.put(MartinOrganization.class.getName(), "ORGANIZATION");
		NERTAGS.put(MartinPercent.class.getName(), "PERCENT");
		NERTAGS.put(MartinPerson.class.getName(), "PERSON");
		NERTAGS.put(MartinSet.class.getName(), "SET");
		NERTAGS.put(MartinText.class.getName(), "O");
		NERTAGS.put(MartinTime.class.getName(), "TIME");
		NERTAGS.put(MartinTimestamp.class.getName(), "DATE TIME");
	}

	private String value;

	private EMartinType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static EMartinType fromClassName(String value) {
		for (EMartinType e : EMartinType.values()) {
			if (value.equals(e.value)) {
				return e;
			}
		}

		return null;
	}

	public static EMartinType fromNerTag(String tag) {
		for (EMartinType e : EMartinType.values()) {
			if (tag.equals(e.getNerTag())) {
				return e;
			}
		}
		return null;
	}

	public String getNerTag() {
		return NERTAGS.get(value);
	}
}
