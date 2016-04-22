package ch.zhaw.psit4.martin.api.types;

import java.util.Date;

import com.wareninja.opensource.strtotime.Str2Time;

public class Timestamp implements IMartinType{
	private boolean instance = false;
	/**
	 * Number of milliseconds since 01. Jan 1970.
	 */
	private Date date;
	private String data;
	
	@Override
	public void fromString(String data) throws MartinTypeInstanciationException {
		date = Str2Time.convert(data);
		this.data = data;
		
		if(date == null){
			throw new MartinTypeInstanciationException("This time / date cannot be converted to timestamp.");
		}  else {
			instance = true;
		}
	}

	@Override
	public boolean isInstancaeableWith(String data) {
		return Str2Time.convert(data) != null;
	}
	
	@Override
	public String toString(){
		return data;
	}
	
	public Long getTimestamp(){
		return date.getTime();
	}

	@Override
	public boolean isInstance() {
		return instance;
	}
	
	
	
}
