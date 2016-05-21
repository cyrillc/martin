package ch.zhaw.psit4.martin.language.typefactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Temporal {
	private String temporalString;
	
	private Double offset;
	private Double intersect;
	
	public Temporal(String temporalString){
		this.temporalString = temporalString;
		
		this.offset = this.getOffset(); 
		this.intersect = this.getIntersect();
	}
	
	
	private Double getOffset(){
		Pattern p = Pattern.compile("(OFFSET)+\\s+(\\H*)");
		Matcher m = p.matcher(temporalString);
		
		if(m.find()){
			return durationToSeconds(m.group(2));
		}
		
		return 0.0;
	}
	
	public Double getIntersect(){
		Pattern p = Pattern.compile("(INTERSECT)+\\s+(\\H*)");
		Matcher m = p.matcher(temporalString);
		
		if(m.find()){
			return durationToSeconds(m.group(2));
		}
		
		return 0.0;
	}
	
	// P1W, P-3D, P2W
	private Double durationToSeconds(String pointString){
		pointString = pointString.substring(1, pointString.length());
		
		String modifier;
		Double amount;
	
		if("T".equals(pointString.substring(0, 1))){
			amount = Double.parseDouble(pointString.substring(1, pointString.length()-1));
			modifier = "T" + pointString.substring(pointString.length()-2, pointString.length());
		} else {
			amount = Double.parseDouble(pointString.substring(0, pointString.length()-1));
			modifier = pointString.substring(pointString.length()-1, pointString.length());
		}
		
		
		switch(modifier){
		case "TS":
			return 1000 * amount;
		case "TM":
			return 1000 * 60 * amount;
		case "TH":
			return 1000 * 60 * 60 * amount;
		case "D":
			return 1000 * 60 * 60 * 24 * amount;
		case "W":
			return 1000 * 60 * 60 * 24 * 7 * amount;
		case "Y":
			return 1000 * 60 * 60 * 24 * 365 * amount;
		default:
			return 0.0;
		}
	}
	
	
	
	

}
