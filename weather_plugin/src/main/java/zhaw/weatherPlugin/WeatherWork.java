package zhaw.weatherPlugin;

import java.util.Map;

import org.joda.time.DateTime;

import ch.zhaw.psit4.martin.api.Feature;
import ch.zhaw.psit4.martin.api.types.IBaseType;
import ch.zhaw.psit4.martin.api.types.MLocation;
import ch.zhaw.psit4.martin.api.types.MTimestamp;
import zhaw.weatherPlugin.plugin.WeatherService;

public class WeatherWork extends Feature {

	WeatherService weatherService;
	private String city;
	private Double latitude;
	private Double longitude;
	private DateTime dateTime;

	public WeatherWork(long requestID) {
		super(requestID);
		weatherService = new WeatherService();
	}

	@Override
	public void initialize(Map<String, IBaseType> args) throws Exception {
		MLocation location = (MLocation) args.get("city");

		this.city = location.toString();

		if (location.getLatitude().isPresent() && location.getLongitude().isPresent()) {
			this.latitude = location.getLatitude().get();
			this.longitude = location.getLongitude().get();
		}

		if (args.containsKey("time")) {
			MTimestamp timestamp = (MTimestamp) args.get("time");

			if (timestamp.getDatetime().isPresent()) {
				this.dateTime = timestamp.getDatetime().get();
			}
		}

	}

	@Override
	public String execute() throws Exception {
	    String response = null;
		if(this.dateTime == null){
			response = weatherService.getWeatherAtCity(this.city);
		} else {
			response = weatherService.getForecastAtCityForSpecificTime(this.city, this.dateTime.toDate());
		}
		
		
		if (response == null) {
			response = "No weather info found for " + city;
		}
		return response;
	}

}
