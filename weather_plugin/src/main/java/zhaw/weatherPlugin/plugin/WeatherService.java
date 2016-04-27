package zhaw.weatherPlugin.plugin;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;

import zhaw.weatherPlugin.plugin.exception.WeatherPluginException;
import zhaw.weatherPlugin.plugin.response.ResponseForecastAdapter;
import zhaw.weatherPlugin.plugin.response.ResponseStatusAdapter;

public class WeatherService {

    private OwmClientAdapter client;

    public WeatherService() {
        client = new OwmClientAdapter();
    }

    public String getWeatherAtCity(String city) throws WeatherPluginException {
        try {
            ResponseStatusAdapter response = client.currentWeatherAtCity(city);
            if (response.hasWeatherData()) {
                WeatherDataAdapter data = response.getWeatherData();
                return "Weather in " + city + " : "
                        + data.getBasicWeatherString();
            } else {
                return null;
            }
        } catch (JSONException e) {
            throw new WeatherPluginException("OWM Response not valid");
        } catch (IOException e) {
            throw new WeatherPluginException("Connection failed");
        }
    }

    public String getForecastAtCityForSpecificTime(String city, Date time)
            throws WeatherPluginException {
        try {
            ResponseForecastAdapter response;
            response = client.forecastWeatherAtCity(city);
            if (response.hasForecast()) {
                WeatherDataAdapter data;
                data = response.searchClosestForecastFrom(time);
                return "Weather in " + city + " : "
                        + data.getBasicWeatherString();
            } else {
                return null;
            }
        } catch (JSONException e) {
            throw new WeatherPluginException("OWM Response not valid");
        } catch (IOException e) {
            throw new WeatherPluginException("Connection failed");
        }

    }

    public String getForecastAtCityInXHours(String city, int hours)
            throws WeatherPluginException {
        Date searchedDate = getDateInXHours(hours);
        return getForecastAtCityForSpecificTime(city, searchedDate);
    }

    private Date getDateInXHours(int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, hours);
        return calendar.getTime();
    }

}
