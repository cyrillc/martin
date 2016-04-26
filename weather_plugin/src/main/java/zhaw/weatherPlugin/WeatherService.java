package zhaw.weatherPlugin;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

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

    public String getForecastAtCityInXHours(String city, int hours) throws WeatherPluginException {

        try {
            WeatherForecastResponseAdapter response = responseAdapterFactory
                    .getResponseAdapter(owmClient.forecastWeatherAtCity(city));
            if (response.hasForecast()) {
                Date searchedDate = getDateInXHours(hours);
                WeatherDataAdapter data = response
                        .searchClosestForecastFrom(searchedDate);
                
                String foundDate = data.getDate().toString();
                float temperatureCelsius = data.getTemperature();
                String description = data.getWeatherDescription();
                int rain = data.getRain();

                return "Weather in " + city + " for : " + foundDate + "\n"
                        + description + " Temperature: " + temperatureCelsius
                        + " Rain: " + rain;
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
    
    private Date getDateInXHours(int hours){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, hours);
         return calendar.getTime();
    }

}
