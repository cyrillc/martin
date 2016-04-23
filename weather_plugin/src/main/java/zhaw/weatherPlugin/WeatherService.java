package zhaw.weatherPlugin;

import java.io.IOException;

import org.bitpipeline.lib.owm.OwmClient;
import org.json.JSONException;

public class WeatherService {

    private OwmClient owmClient;
    private ResponseAdapterFactory responseAdapterFactory;

    public WeatherService() {
        owmClient = new OwmClient();
        responseAdapterFactory = new ResponseAdapterFactory();
        this.owmClient.setAPPID("c4cb05905b0c1017d58221beda81460d");
    }

    public String getWeatherAtCity(String city) throws WeatherPluginException {
        try {
            WeatherResponseAdapter response = responseAdapterFactory
                    .getResponseAdapter(owmClient.currentWeatherAtCity(city));
            if (response.hasWeatherData()) {

                float temperatureCelsius = response.getTemperature();
                String description = response.getWeatherDescription();
                int rain = response.getRain();

                return "Weather in " + city + ": " + description
                        + " Temperature: " + temperatureCelsius + " Rain: "
                        + rain;
            } else {
                return null;
            }
        } catch (JSONException e) {
            throw new WeatherPluginException(
                    "OpenWeatherMap Response not valid");
        } catch (IOException e) {
            throw new WeatherPluginException(
                    "impossible to connect with the server");
        }
    }

}
