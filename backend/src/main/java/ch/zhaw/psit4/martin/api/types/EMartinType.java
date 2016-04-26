package ch.zhaw.psit4.martin.api.types;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Enums;
import com.google.common.base.Optional;

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

	public static EMartinType fromNerTag(String tag) {
		String key = "";
		for (Map.Entry<String, String> entry : NERTAGS.entrySet()) {
			if (entry.getValue().equalsIgnoreCase(tag)) {
				key = entry.getKey();
			}
		}

		Optional<EMartinType> possible = Enums.getIfPresent(EMartinType.class, tag);
		if (!possible.isPresent()) {
			throw new IllegalArgumentException(key + "? There is no such NER-Tag!");
		}
		return possible.get();
	}

	public String getNerTag() {
		return NERTAGS.get(value);
	}
}
