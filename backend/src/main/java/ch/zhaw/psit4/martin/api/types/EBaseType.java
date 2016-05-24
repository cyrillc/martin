package ch.zhaw.psit4.martin.api.types;

import java.util.HashMap;
import java.util.Map;

public enum EBaseType {
	DATE(MDate.class.getName()), DURATION(MDuration.class.getName()), LOCATION(MLocation.class.getName()), MISC(
			MMisc.class.getName()), MONEY(MMoney.class.getName()), NUMBER(MNumber.class.getName()), ORDINAL(
					MOrdinal.class.getName()), ORGANIZATION(MOrganization.class.getName()), PERCENT(
							MPercent.class.getName()), PERSON(MPerson.class.getName()), SET(MSet.class.getName()), TEXT(
									MText.class.getName()), TIME(
											MTime.class.getName()), TIMESTAMP(MTimestamp.class.getName()), UNKOWN(MUnknown.class.getName());

	private static final Map<String, String> NERTAGS;

	static {
		NERTAGS = new HashMap<>();
		NERTAGS.put(MDate.class.getName(), "DATE");
		NERTAGS.put(MDuration.class.getName(), "DURATION");
		NERTAGS.put(MLocation.class.getName(), "LOCATION");
		NERTAGS.put(MMisc.class.getName(), "MISC");
		NERTAGS.put(MMoney.class.getName(), "MONEY");
		NERTAGS.put(MNumber.class.getName(), "NUMBER");
		NERTAGS.put(MOrdinal.class.getName(), "ORDINAL");
		NERTAGS.put(MOrganization.class.getName(), "ORGANIZATION");
		NERTAGS.put(MPercent.class.getName(), "PERCENT");
		NERTAGS.put(MPerson.class.getName(), "PERSON");
		NERTAGS.put(MSet.class.getName(), "SET");
		NERTAGS.put(MText.class.getName(), "TEXT");
		NERTAGS.put(MTime.class.getName(), "TIME");
		NERTAGS.put(MTimestamp.class.getName(), "DATE TIME");
		NERTAGS.put(MUnknown.class.getName(), "O");
	}

	private String value;

	private EBaseType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static EBaseType fromClassName(String value) {
		for (EBaseType e : EBaseType.values()) {
			if (value.equals(e.value)) {
				return e;
			}
		}

		return null;
	}

	public static EBaseType fromNerTag(String tag) {
		for (EBaseType e : EBaseType.values()) {
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
