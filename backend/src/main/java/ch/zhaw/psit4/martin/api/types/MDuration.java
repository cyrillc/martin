package ch.zhaw.psit4.martin.api.types;

import org.joda.time.Duration;
import org.joda.time.Period;
import org.json.JSONObject;


/*
 * A Duration represents a period of time (without endpoints) 
 */
public class MDuration extends BaseType {

	Duration duration;
	Period period;

	public MDuration(String data) {
		super(data);
	}

	public Duration getDuration() {
		return duration;
	}
	
	public Long getMilliseconds(){
		return duration.getMillis();
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period periond) {
		this.period = periond;
	}
	
	@Override
	public String toJson() {
		JSONObject json = new JSONObject();
		json.put("type", this.getClass().getName());
		json.put("data", data);
		json.put("duration", duration.getMillis());

		return json.toString(4);
	}

}
