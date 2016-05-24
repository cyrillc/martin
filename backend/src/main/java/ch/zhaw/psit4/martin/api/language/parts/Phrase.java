package ch.zhaw.psit4.martin.api.language.parts;

import ch.zhaw.psit4.martin.api.types.EBaseType;
import ch.zhaw.psit4.martin.api.types.MText;

public class Phrase {
	private EBaseType type;
	private String value;
	private String nerTag;
	private String normalizedValue;
	private Object payload;

	public Phrase(String value) {
		this.type = EBaseType.fromClassName(MText.class.getName());
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
		this.type = EBaseType.fromNerTag(nerTag);
	}

	public void setType(EBaseType type) {
		this.type = type;
		this.nerTag = type.getNerTag();
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	@Override
	public String toString(){
		return type.toString() + "|" + normalizedValue + "|" + value;
	}
	
	public String getNormalizedValue() {
		return normalizedValue;
	}

	public void setNormalizedValue(String normalizedValue) {
		this.normalizedValue = normalizedValue;
	}
	
	public Object getPayload(){
		return this.payload;
	}
	
	public void setPayload(Object payload){
		this.payload = payload;
	}

}