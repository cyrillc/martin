package ch.zhaw.psit4.martin.api.types;

import java.util.HashMap;
import java.util.Map;

public enum EMartinType {
	DATE(Date.class.getName()), DURATION(Duration.class.getName()), LOCATION(Location.class.getName()), MISC(
			Misc.class.getName()), MONEY(Money.class.getName()), NUMBER(Number.class.getName()), ORDINAL(
					Ordinal.class.getName()), ORGANIZATION(Organization.class.getName()), PERCENT(
							Percent.class.getName()), PERSON(Person.class.getName()), SET(Set.class.getName()), TEXT(
									Text.class.getName()), TIME(
											Time.class.getName()), TIMESTAMP(Timestamp.class.getName());

	private static final Map<String, String> NERTAGS;

	static {
		NERTAGS = new HashMap<>();
		NERTAGS.put(Date.class.getName(), "DATE");
		NERTAGS.put(Duration.class.getName(), "DURATION");
		NERTAGS.put(Location.class.getName(), "LOCATION");
		NERTAGS.put(Misc.class.getName(), "MISC");
		NERTAGS.put(Money.class.getName(), "MONEY");
		NERTAGS.put(Number.class.getName(), "NUMBER");
		NERTAGS.put(Ordinal.class.getName(), "ORDINAL");
		NERTAGS.put(Organization.class.getName(), "ORGANIZATION");
		NERTAGS.put(Percent.class.getName(), "PERCENT");
		NERTAGS.put(Person.class.getName(), "PERSON");
		NERTAGS.put(Set.class.getName(), "SET");
		NERTAGS.put(Text.class.getName(), "O");
		NERTAGS.put(Time.class.getName(), "TIME");
		NERTAGS.put(Timestamp.class.getName(), "DATE TIME");
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
