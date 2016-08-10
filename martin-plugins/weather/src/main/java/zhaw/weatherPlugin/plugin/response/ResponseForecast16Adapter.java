package zhaw.weatherPlugin.plugin.response;

import java.util.Calendar;
import java.util.Date;
import java.util.List;



import org.bitpipeline.lib.owm.Forecast16WeatherData;
import org.bitpipeline.lib.owm.WeatherForecast16Response;

import zhaw.weatherPlugin.plugin.WeatherDataAdapter;

public class ResponseForecast16Adapter {

    private WeatherForecast16Response owmResponse;

    public ResponseForecast16Adapter(WeatherForecast16Response r) {
        this.owmResponse = r;
    }

    public WeatherForecast16Response getOwmResponse() {
        return this.owmResponse;
    }

    public boolean hasForecast() {
        return this.owmResponse.hasForecasts();
    }

    public WeatherDataAdapter getForecastForDate(Date searchedDate) {
        List<Forecast16WeatherData> forecasts = owmResponse.getForecasts();
        
        for(int i = 0; i<forecasts.size(); i++){
            Forecast16WeatherData currentForecast = forecasts.get(i);
            Date forecastDate = convertDateTime(currentForecast.getDateTime());
            if(sameDay(forecastDate, searchedDate)){
                return new WeatherDataAdapter(currentForecast);
            }
        }
        return null;
    }

    private boolean sameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1
                .get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    private Date convertDateTime(long unixDate) {
        return new Date(unixDate * 1000);
    }

}
