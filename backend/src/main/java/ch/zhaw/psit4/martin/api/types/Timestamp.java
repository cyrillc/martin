package ch.zhaw.psit4.martin.api.types;

import java.util.Date;
import java.util.Optional;

import javax.json.Json;

import org.json.JSONObject;

import com.wareninja.opensource.strtotime.Str2Time;

public class Timestamp extends MartinType {
	private Optional<Date> datetime;
	

	public Timestamp(String data) {
		super(data);
		datetime = Optional.ofNullable(Str2Time.convert(data));
	}
	
	public Date getDate(){
		return datetime.get();
	}
	
	public Long getTimestamp(){
		if(datetime.isPresent()){
			return datetime.get().getTime();
		} else {
			return null;
		}
	}

	@Override
	public String toJson() {
		JSONObject json = new JSONObject();
		json.put("type", this.getClass().getName());
		json.put("data", data);
		
		if(datetime.isPresent()){
			json.put("datetime", datetime.get().toString());
			json.put("timestamp", datetime.get().getTime());
		}
		
		return json.toString(4);
	}
}
