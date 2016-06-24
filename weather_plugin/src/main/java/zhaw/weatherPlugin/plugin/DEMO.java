package zhaw.weatherPlugin.plugin;

import org.joda.time.DateTime;

import zhaw.weatherPlugin.plugin.exception.WeatherPluginException;

public class DEMO {

    public static void main(String[] args) throws WeatherPluginException {
        
        // SETTINGS
        String city = "Zurich";
        int forecastHours = 48;
        int forecastDays = 3;
        
        
        DateTime date = new DateTime(DateTime.now());
        date = date.plusDays(forecastDays);
        WeatherService weatherService = new WeatherService();
        
        //CURRENT WEATHER
        System.out.println("Actual Weather in " + city);
        String currentWeather = weatherService.getWeatherAtCity(null, city);
        System.out.println(currentWeather);
        
        //FORECAST
        System.out.println("\n\nWeather in " + city + " in " + forecastHours + " hours");
        String forecast = weatherService.getForecastAtCityInXHours(null, city, forecastHours);
        System.out.println(forecast);
        
        //SINGLE DAY FORECAST
        System.out.println("\n\nWeather in " + city + " in " + forecastDays + " days");
        String DayForecast = weatherService.getForecastAtCityForDay(null, city, date.toDate());
        System.out.println(DayForecast);
        
        //WEEK FORECAST
        DateTime start = new DateTime();
        String forecastW;
        for(int i = 0; i < 20; i++){
            forecastW = weatherService.getForecastAtCityForDay(null, city, start.plusDays(i).toDate());
            System.out.println(forecastW);
        }
    }

}
