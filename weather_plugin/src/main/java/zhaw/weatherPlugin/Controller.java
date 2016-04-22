package zhaw.weatherPlugin;

import java.io.IOException;

import org.bitpipeline.lib.owm.OwmClient;
import org.bitpipeline.lib.owm.WeatherData;
import org.bitpipeline.lib.owm.WeatherData.WeatherCondition;
import org.bitpipeline.lib.owm.WeatherStatusResponse;
import org.json.JSONException;

/**
 * 
 * 
 *
 */
public class Controller {

    private OwmClient owmClient;

    public Controller() {
        this.owmClient = new OwmClient();
        this.owmClient.setAPPID("c4cb05905b0c1017d58221beda81460d");
    }

    /**
     * 
     * @param city
     * @throws WeatherPluginException
     */
    public String getWeatherAtCity(String city) throws WeatherPluginException {
        try {
            WeatherStatusResponse currentWeather = this.owmClient.currentWeatherAtCity(city);
            if (currentWeather.hasWeatherStatus()) {
                WeatherData data = currentWeather.getWeatherStatus().get(0);
                WeatherCondition conditions = data.getWeatherConditions().get(0);
                float temperatureCelsius = convertKelvinToCelsius(data.getTemp());
                String description = conditions.getDescription();
                int rain = (data.getRain() == Integer.MIN_VALUE) ? 0 : data.getRain();

                return "Weather in " + city + ": " + description + " Temperature: "
                        + temperatureCelsius + " Rain: " + rain;
            } else {
                return null;
            }
        } catch (JSONException e) {
            throw new WeatherPluginException("OpenWeatherMap Response not valid");
        } catch (IOException e) {
            throw new WeatherPluginException("impossible to connect with the server");
        }
    }

    private float convertKelvinToCelsius(float kelvin) {
        return (float) (kelvin - 273.15);
    }



}
