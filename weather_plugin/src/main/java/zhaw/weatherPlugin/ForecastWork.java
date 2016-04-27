package zhaw.weatherPlugin;

import java.util.Map;

import org.joda.time.DateTime;

import ch.zhaw.psit4.martin.api.Feature;
import ch.zhaw.psit4.martin.api.types.IMartinType;
import ch.zhaw.psit4.martin.api.types.Location;
import ch.zhaw.psit4.martin.api.types.Timestamp;
import zhaw.weatherPlugin.plugin.WeatherService;

public class ForecastWork extends Feature {

    WeatherService weatherService;
    private String city;
    private String response;
    private DateTime dateTime;

    public ForecastWork(long requestID) {
        super(requestID);
        weatherService = new WeatherService();
    }

    @Override
    public void start(Map<String, IMartinType> args) throws Exception {
        Location location = (Location) args.get("city");

        this.city = location.getData();

        if (args.containsKey("time")) {
            Timestamp timestamp = (Timestamp) args.get("time");

            if (timestamp.getDatetime().isPresent()) {
                this.dateTime = timestamp.getDatetime().get();
            }
        }

    }

    @Override
    public void run() throws Exception {
        if (!this.city.isEmpty() && this.dateTime != null) {
            response = weatherService.getForecastAtCityForSpecificTime(
                    this.city, this.dateTime.toDate());
        }

        if (response == null) {
            response = "No weather info found for " + city;
        }
    }

    @Override
    public String stop() throws Exception {
        return response;
    }
}
