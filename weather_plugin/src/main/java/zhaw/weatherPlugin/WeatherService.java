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
            WeatherStatusResponseAdapter response = responseAdapterFactory
                    .getResponseAdapter(owmClient.currentWeatherAtCity(city));
            if (response.hasWeatherData()) {
                WeatherDataAdapter data = response.getWeatherData();
                float temperatureCelsius = data.getTemperature();
                String description = data.getWeatherDescription();
                int rain = data.getRain();

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

    public String getForecastAtCity(String city) throws WeatherPluginException {

        try {
            WeatherForecastResponseAdapter response = responseAdapterFactory
                    .getResponseAdapter(owmClient.forecastWeatherAtCity(city));
            if(response.hasForecast()){
                // TODO
                return null;
            }else {
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
