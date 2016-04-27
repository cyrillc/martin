package ch.zhaw.psit4.martin.common;

import ch.zhaw.psit4.martin.api.types.EMartinType;

public class Phrase {
	private EMartinType type;
	private String value;
	private String nerTag;

	public Phrase(String nerTag, String value) {
		this.nerTag = nerTag;
		this.type = EMartinType.fromNerTag(nerTag);
		this.value = value;
	}
	
	public EMartinType getType() {
		return type;
	}

	public String getNerTag() {
		return nerTag;
	}

	public void setNerTag(String nerTag) {
		this.nerTag = nerTag;
	}

	public void setType(EMartinType type) {
		this.type = type;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}