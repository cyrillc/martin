package zhaw.weatherPlugin;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.bitpipeline.lib.owm.ForecastWeatherData;
import org.bitpipeline.lib.owm.WeatherForecastResponse;

public class WeatherForecastResponseAdapter {

    private WeatherForecastResponse owmResponse;

    public WeatherForecastResponseAdapter(WeatherForecastResponse owmResponse) {
        this.owmResponse = owmResponse;
    }

    public WeatherForecastResponse getOwnResponse() {
        return this.owmResponse;
    }

    public boolean hasForecast() {
        return owmResponse.hasForecasts();
    }

    public String getWeatherDescriptionAtTime(Date date) {
        ForecastWeatherData forecastData = searchClosestForecastFrom(date);
        return forecastData.getWeatherConditions().get(0).getDescription();
    }

    /**
     * 
     * @param searchedDate
     * @return the closes forecast at the given date. Null if the searched Date
     *         is earlier from the first available forecast or after the last
     */
    private ForecastWeatherData searchClosestForecastFrom(Date searchedDate) {
        List<ForecastWeatherData> forecasts = owmResponse.getForecasts();

        Date firstForecastDate = convertDate(forecasts.get(0).getDateTime());
        if (searchedDate.compareTo(firstForecastDate) < 0) {
            return null;
        }

        for (int i = 1; i < forecasts.size(); i++) {
            ForecastWeatherData lastForecast = forecasts.get(i - 1);
            ForecastWeatherData nextForecast = forecasts.get(i);

            Date nextForecastDate = convertDate(nextForecast.getDateTime());
            if (searchedDate.compareTo(nextForecastDate) < 0) {
                long diffWithB = nextForecastDate.getTime()
                        - searchedDate.getTime();
                if (diffWithB / (60 * 1000) < 90) { // time between two
                                                    // Forecasts is 180 min
                    return nextForecast;
                } else {
                    return lastForecast;
                }
            }
        }
        return null;
    }

    private Date convertDate(long unixDate) {
        return new Date(unixDate * 1000);
    }

}
