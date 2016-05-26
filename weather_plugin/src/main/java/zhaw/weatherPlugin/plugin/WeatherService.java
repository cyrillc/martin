package zhaw.weatherPlugin.plugin;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;

import zhaw.weatherPlugin.plugin.exception.WeatherPluginException;
import zhaw.weatherPlugin.plugin.response.ResponseForecast16Adapter;
import zhaw.weatherPlugin.plugin.response.ResponseForecastAdapter;
import zhaw.weatherPlugin.plugin.response.ResponseStatusAdapter;

public class WeatherService {

    private OwmClientAdapter client;

    public WeatherService() {
        client = new OwmClientAdapter();
    }

    /**
     * This method return the current Weather at the given city
     * 
     * @param city
     * @return a String with the basic info about the weather, null if the city
     *         is not found.
     * @throws WeatherPluginException
     */
    public String getWeatherAtCity(String city) throws WeatherPluginException {
        try {
            ResponseStatusAdapter response = client.currentWeatherAtCity(city);
            if (response.hasWeatherData()) {
                WeatherDataAdapter data = response.getWeatherData();
                return data.getBasicWeatherString();
            } else {
                return null;
            }
        } catch (JSONException e) {
            throw new WeatherPluginException("OWM Response not valid");
        } catch (IOException e) {
            throw new WeatherPluginException("Connection failed");
        }
    }

    /**
     * This method give the weather forecast for a specific City at a specific
     * Time. It can get forecast until 5 days in advance.
     * 
     * @param city
     * @param time
     * @return a String with the basic info about the forecast. Null if the date
     *         is not in the available forecast range or if the city is not
     *         found.
     * @throws WeatherPluginException
     */
    public String getForecastAtCityForSpecificTime(String city, Date time)
            throws WeatherPluginException {
        try {
            ResponseForecastAdapter response;
            response = client.forecastWeatherAtCity(city);
            if (response.hasForecast()) {
                WeatherDataAdapter data;
                data = response.searchClosestForecastFrom(time);
                return data.getBasicWeatherString();
            } else {
                return null;
            }
        } catch (JSONException e) {
            throw new WeatherPluginException("OWM Response not valid");
        } catch (IOException e) {
            throw new WeatherPluginException("Connection failed");
        }
    }

    public String getForecastAtCityForDay(String city, Date day)
            throws WeatherPluginException {

        try {
            ResponseForecast16Adapter response;
            response = client.dailyForecastAtCity(city);
            if (response.hasForecast()) {
                WeatherDataAdapter data;
                data = response.getForecastForDate(day);
                return (data != null) ? data.getBasicWeatherString() : null;
            } else {
                return null;
            }
        } catch (JSONException e) {
            throw new WeatherPluginException("OWM Response not valid");
        } catch (IOException e) {
            throw new WeatherPluginException("Connection failed");
        }
    }

    /**
     * This method give the weather forecast for a specific City at some hours
     * in advance form now.
     * 
     * @param city
     * @param hours
     * @return a String with the basic info about the forecast. Null if the time
     *         is not in the available forecast range or if the city is not
     *         found.
     * @throws WeatherPluginException
     */
    public String getForecastAtCityInXHours(String city, int hours)
            throws WeatherPluginException {
        if (hours < 0) {
            return null;
        }
        Date searchedDate = getDateInXHours(hours);
        return getForecastAtCityForSpecificTime(city, searchedDate);
    }

    private Date getDateInXHours(int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, hours);
        return calendar.getTime();
    }

}
