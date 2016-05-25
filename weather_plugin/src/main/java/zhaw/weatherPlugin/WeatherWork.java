package zhaw.weatherPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.Instant;
import org.joda.time.Period;
import org.joda.time.Duration;

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
	private Instant startTime;
	private Duration duration;
	private Period period;

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
            this.startTime = duration.getInstantStart();
            this.duration = duration.getDuration();
            this.period = duration.getPeriod();
        }

	}

	@Override
	public List<MOutput> execute() throws Exception {
		List<MOutput> response = new ArrayList<>();
		String apiResponse;

		if (this.time == null) {
			apiResponse = weatherService.getWeatherAtCity(this.city);
		} else if(this.duration == null) {
			apiResponse = weatherService.getForecastAtCityForSpecificTime(this.city, this.time.toDate());
		} else {
		    apiResponse = weatherService.getForecastAtCityForDay(this.city, this.startTime.toDate());
		}

		if (apiResponse == null) {
			apiResponse = "No weather info found for " + city;
		}
		response.add(new MOutput(MOutputType.HEADING, "Weather"));
		response.add(new MOutput(MOutputType.TEXT, apiResponse));

		return response;
	}

}
