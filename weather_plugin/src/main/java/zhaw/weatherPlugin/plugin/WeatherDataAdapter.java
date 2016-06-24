package zhaw.weatherPlugin.plugin;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bitpipeline.lib.owm.WeatherData;

public class WeatherDataAdapter {
	private WeatherData owmData;

	public WeatherDataAdapter(WeatherData data) {
		this.owmData = data;
	}

	public WeatherData getOwmData() {
		return owmData;
	}

	public boolean hasRain() {
		return this.owmData.hasRain();
	}

	public Date getDate() {
		// owm returns date as a unix timestamp
		return new Date(this.owmData.getDateTime() * 1000);
	}

	public int getRain() {
		return this.hasRain() ? this.owmData.getRain() : 0;
	}

	public String getWeatherDescription() {
		return this.owmData.getWeatherConditions().get(0).getDescription();
	}

	public float getTemperature() {
		return this.owmData.getTemperature().getTemp();
	}

	public float getTemperatureMax() {
		return this.owmData.getTemperature().getTempDayMax();
	}

	public float getTemperatureMin() {
		return this.owmData.getTemperature().getTempDayMin();
	}

	public float convertKelvinToCelsius(float kelvin) {
		return (float) (kelvin - 273.15);
	}

	public String getBasicWeatherString(String location) {
		String dayNumberSuffix = getDayNumberSuffix(getDate().getDate());
		String day = new SimpleDateFormat("EEEE, dd").format(getDate());
		String month = new SimpleDateFormat("MMMM yyyy").format(getDate());

		String outputString = "The weather in " + location + " on " + day + dayNumberSuffix + " " + month
				+ " will be " + getWeatherDescription() + ". The temperature will " + getTemperatureString() + ".";
		
		String appendix = "";
		if(getWeatherDescription().contains("rain")){
			appendix = " Take an umberalla with you!";
		}
		
		if(getWeatherDescription().contains("clear") && getTemperature() > 25){
			appendix = " Don't forget the sunscreen!";
		}
		
	
		return outputString + appendix;

	}
	
	private String getDayNumberSuffix(int day) {
	    if (day >= 11 && day <= 13) {
	        return "th";
	    }
	    switch (day % 10) {
	    case 1:
	        return "st";
	    case 2:
	        return "nd";
	    case 3:
	        return "rd";
	    default:
	        return "th";
	    }
	}

	public String getTemperatureString() {
		String temp = "";
		if (!Float.isNaN(getTemperatureMax()) && !Float.isNaN(getTemperatureMin())) {
			temp = "reach from " + String.format("%.0f", getTemperatureMin()) + "°C to " + String.format("%.0f", getTemperatureMax())
					+ "°C";
		} else if (!Float.isNaN(getTemperature())) {
			temp = "be " + String.format("%.0f", getTemperature()) + "°C";
		}
		return temp;
	}

}
