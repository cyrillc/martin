package zhaw.weatherPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.Instant;
import org.joda.time.Interval;
import org.joda.time.DateTime;

import ch.zhaw.psit4.martin.api.Feature;
import ch.zhaw.psit4.martin.api.types.IBaseType;
import ch.zhaw.psit4.martin.api.types.MDuration;
import ch.zhaw.psit4.martin.api.types.MLocation;
import ch.zhaw.psit4.martin.api.types.MTimestamp;
import ch.zhaw.psit4.martin.api.types.output.MOutput;
import ch.zhaw.psit4.martin.api.types.output.MOutputType;
import zhaw.weatherPlugin.plugin.WeatherService;

public class WeatherWork extends Feature {

	WeatherService weatherService;
	private String city;
	private Instant time;
	private Interval interval;

	public WeatherWork(long requestID) {
		super(requestID);
		weatherService = new WeatherService();
	}

	@Override
	public void initialize(Map<String, IBaseType> args) throws Exception {
	    
		MLocation location = (MLocation) args.get("city");
		this.city = location.toString();

		if (args.containsKey("time")) {
			MTimestamp timestamp = (MTimestamp) args.get("time");
			this.time = timestamp.getInstant();
		}
		
		if (args.containsKey("duration")) {
            MDuration duration = (MDuration) args.get("duration");
           this.interval = duration.getInterval();
        }

	}

	@Override
	public List<MOutput> execute() throws Exception {
		List<MOutput> response = new ArrayList<>();
		String apiResponse;
		
		response.add(new MOutput(MOutputType.HEADING, "Weather"));

		if (this.time == null && this.interval == null) {
			apiResponse = weatherService.getWeatherAtCity(this.city);
			response.add(new MOutput(MOutputType.TEXT, apiResponse));
		} else if(this.interval == null) {
			apiResponse = weatherService.getForecastAtCityForDay(this.city, this.time.toDate());
			response.add(new MOutput(MOutputType.TEXT, apiResponse));
		} else {
		    DateTime start = interval.getStart();
		    apiResponse = weatherService.getForecastAtCityForDay(this.city, start.toDate());
		    int i = 1;
		    while(start.plusDays(i).isBefore(interval.getEnd())){
		        apiResponse += weatherService.getForecastAtCityForDay(this.city, start.plusDays(i).toDate());
		        response.add(new MOutput(MOutputType.TEXT, apiResponse));
		        i++;
		    }
		}

		if (apiResponse == null) {
			apiResponse = "No weather info found for " + city;
			response.add(new MOutput(MOutputType.TEXT, apiResponse));
		}

		return response;
	}

}
