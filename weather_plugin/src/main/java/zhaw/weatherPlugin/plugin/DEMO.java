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
        WeatherService weaterService = new WeatherService();
        
        //CURRENT WEATHER
        System.out.println("Actual Weather in " + city);
        String currentWeather = weaterService.getWeatherAtCity(city);
        System.out.println(currentWeather);
        
        //FORECAST
        System.out.println("\n\nWeather in " + city + " in " + forecastHours + " hours");
        String forecast = weaterService.getForecastAtCityInXHours(city, forecastHours);
        System.out.println(forecast);
        
        //SINGLE DAY FORECAST
        System.out.println("\n\nWeather in " + city + " in " + forecastDays + " days");
        String DayForecast = weaterService.getForecastAtCityForDay(city, date.toDate());
        System.out.println(DayForecast);
    }

}
