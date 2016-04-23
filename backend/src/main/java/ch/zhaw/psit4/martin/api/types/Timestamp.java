package ch.zhaw.psit4.martin.api.types;

import java.util.Date;

import com.wareninja.opensource.strtotime.Str2Time;

public class Timestamp implements IMartinType{
	private boolean isValid = false;
	/**
	 * Number of milliseconds since 01. Jan 1970.
	 */
	private Date datetime;
	private String data;
	
	@Override
	public void fromString(String data) throws IMartinTypeInstanciationException {
		datetime = Str2Time.convert(data);
		this.data = data;
		
		if(datetime != null){
			isValid = true;
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
	
	public Date getDate(){
		return datetime;
	}
	
	public Long getTimestamp(){
		return datetime.getTime();
	}

	@Override
	public boolean isValid() {
		return isValid;
	}

	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public void fromJSON(String json) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
