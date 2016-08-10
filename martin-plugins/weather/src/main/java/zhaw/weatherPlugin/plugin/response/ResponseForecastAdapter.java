package zhaw.weatherPlugin.plugin.response;

import java.util.Date;
import java.util.List;

import org.bitpipeline.lib.owm.ForecastWeatherData;
import org.bitpipeline.lib.owm.WeatherForecastResponse;

import zhaw.weatherPlugin.plugin.WeatherDataAdapter;

public class ResponseForecastAdapter {

    private WeatherForecastResponse owmResponse;

    public ResponseForecastAdapter(WeatherForecastResponse owmResponse) {
        this.owmResponse = owmResponse;
    }

    public WeatherForecastResponse getOwnResponse() {
        return this.owmResponse;
    }

    public boolean hasForecast() {
        return owmResponse.hasForecasts();
    }

    /**
     * 
     * @param searchedDate
     * @return the closes forecast at the given date. Null if the searched Date
     *         is earlier from the first available forecast or after the last
     */
    public WeatherDataAdapter searchClosestForecastFrom(Date searchedDate) {
        List<ForecastWeatherData> forecasts = owmResponse.getForecasts();

        Date firstForecastDate = convertDate(forecasts.get(0).getDateTime());
        if (searchedDate.compareTo(firstForecastDate) < 0) {
            return null;
        }

        for (int i = 1; i < forecasts.size(); i++) {
            ForecastWeatherData lastForecast = forecasts.get(i - 1);
            ForecastWeatherData nextForecast = forecasts.get(i);

            Date nextForecastDate = convertDate(nextForecast.getDateTime());
            if (searchedDate.compareTo(nextForecastDate) < 0
                    || searchedDate.compareTo(nextForecastDate) == 0) {
                long diffWithB = nextForecastDate.getTime()
                        - searchedDate.getTime();
                if (diffWithB / (60 * 1000) < 90) { // time between two
                                                    // Forecasts is 180 min
                    return new WeatherDataAdapter(nextForecast);
                } else {
                    return new WeatherDataAdapter(lastForecast);
                }
            }
        }
        return null;
    }

    private Date convertDate(long unixDate) {
        return new Date(unixDate * 1000);
    }

}
