package zhaw.weatherPlugin;

import java.util.Map;

import ch.zhaw.psit4.martin.api.Feature;
import ch.zhaw.psit4.martin.api.types.IMartinType;
import ch.zhaw.psit4.martin.api.types.Text;

public class WeatherWork extends Feature {

    WeatherService weatherService;
    private String city;
    private String response;

    public WeatherWork(long requestID) {
        super(requestID);
        weatherService = new WeatherService();
    }

    @Override
    public void start(Map<String, IMartinType> args) throws Exception {
        Text text = (Text) args.get("city");
        this.city = text.getValue();
    }

    @Override
    public void run() throws Exception {
        response = weatherService.getWeatherAtCity(this.city);
        if (response == null) {
            response = "No weather info found for " + city;
        }
    }

    @Override
    public String stop() throws Exception {
        return response;
    }

}
