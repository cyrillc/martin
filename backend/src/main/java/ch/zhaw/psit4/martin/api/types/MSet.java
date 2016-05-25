package ch.zhaw.psit4.martin.api.types;

import org.joda.time.Duration;

public class MSet extends BaseType{
	
	Duration duration;

	public MSet(String data) {
		super(data);
	}
	
	public void setDuration(Duration duration){
		this.duration = duration;
	}
	
	public Duration getDuration(){
		return this.duration;
	}

}
