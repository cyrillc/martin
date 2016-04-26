package ch.zhaw.psit4.martin.api.types;

import java.util.Date;
import java.util.Optional;

import javax.json.Json;

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
		return Json.createObjectBuilder()
				.add("type", this.getClass().getName())
				.add("data", data)
				.add("timestamp", getTimestamp())
				.build().toString();
	}
}
