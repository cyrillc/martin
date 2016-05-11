package ch.zhaw.psit4.martin.common;

import ch.zhaw.psit4.martin.api.types.EBaseType;

public class Phrase {
	private EBaseType type;
	private String value;
	private String nerTag;

	public Phrase(String nerTag, String value) {
		this.nerTag = nerTag;
		this.type = EBaseType.fromNerTag(nerTag);
		this.value = value;
	}
	
	public EBaseType getType() {
		return type;
	}

	public String getNerTag() {
		return nerTag;
	}

	public void setNerTag(String nerTag) {
		this.nerTag = nerTag;
	}

	public void setType(EBaseType type) {
		this.type = type;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}