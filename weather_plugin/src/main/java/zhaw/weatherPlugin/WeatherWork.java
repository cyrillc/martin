package zhaw.weatherPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import ch.zhaw.psit4.martin.api.Feature;
import ch.zhaw.psit4.martin.api.types.IBaseType;
import ch.zhaw.psit4.martin.api.types.MLocation;
import ch.zhaw.psit4.martin.api.types.MTimestamp;
import ch.zhaw.psit4.martin.api.types.output.MOutput;
import ch.zhaw.psit4.martin.api.types.output.MOutputType;
import zhaw.weatherPlugin.plugin.WeatherService;

public class WeatherWork extends Feature {

	WeatherService weatherService;
	private String city;
	private DateTime dateTime;

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

			if (timestamp.getDatetime().isPresent()) {
				this.dateTime = timestamp.getDatetime().get();
			}
		}

	}

	@Override
	public List<MOutput> execute() throws Exception {
		List<MOutput> response = new ArrayList<>();
		String apiResponse;

		if (this.dateTime == null) {
			apiResponse = weatherService.getWeatherAtCity(this.city);
		} else {
			apiResponse = weatherService.getForecastAtCityForSpecificTime(this.city, this.dateTime.toDate());
		}

		if (apiResponse == null) {
			apiResponse = "No weather info found for " + city;
		}
		response.add(new MOutput(MOutputType.HEADING, "Weather"));
		response.add(new MOutput(MOutputType.TEXT, apiResponse));

		return response;
	}

}
