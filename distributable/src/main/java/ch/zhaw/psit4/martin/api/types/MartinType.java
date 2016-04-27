package ch.zhaw.psit4.martin.api.types;

import javax.json.Json;

public abstract class MartinType implements IMartinType {
	protected String data;
	
	public MartinType(String data){
		this.data = data;
	}
	
	@Override
	public String toString(){
		return data;
	}
	
	@Override
	public String toJson() {
		return Json.createObjectBuilder()
				.add("type", this.getClass().getName())
				.add("data", data)
				.build().toString();
	}
}
