package ch.zhaw.psit4.martin.api.types;

import org.json.*;

public abstract class MartinBaseType implements IMartinType {
	protected String data;
	
	public MartinBaseType(String data){
		this.data = data;
	}
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString(){
		return data;
	}
	
	@Override
	public String toJson() {
		JSONObject json = new JSONObject();
		json.put("type", this.getClass().getName());
		json.put("data", data);
		return json.toString(4);
	}
}
