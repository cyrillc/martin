package zhaw.weatherPlugin;

import java.util.Map;

import org.joda.time.DateTime;

import ch.zhaw.psit4.martin.api.Feature;
import ch.zhaw.psit4.martin.api.types.IBaseType;
import ch.zhaw.psit4.martin.api.types.MLocation;
import ch.zhaw.psit4.martin.api.types.MTimestamp;
import zhaw.weatherPlugin.plugin.WeatherService;

public class ForecastWork extends Feature {

    WeatherService weatherService;
    private String city;
    private DateTime dateTime;

    public ForecastWork(long requestID) {
        super(requestID);
        weatherService = new WeatherService();
    }

    @Override
    public void initialize(Map<String, IBaseType> args) throws Exception {
        MLocation location = (MLocation) args.get("city");

        this.city = location.getData();

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
        if (!this.city.isEmpty() && this.dateTime != null) {
            response = weatherService.getForecastAtCityForSpecificTime(
                    this.city, this.dateTime.toDate());
        }

        if (response == null) {
            response = "No weather info found for " + city;
        }
        return response;
    }
}
