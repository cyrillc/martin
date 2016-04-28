package zhaw.weatherPlugin.plugin;

import zhaw.weatherPlugin.plugin.exception.WeatherPluginException;

public class DEMO {

    public static void main(String[] args) throws WeatherPluginException {
        
        String city = "Zurich";
        int forecastHours = 48;
        
        WeatherService weaterService = new WeatherService();
        
        //CURRENT WEATHER
        System.out.println("Actual Weather in " + city);
        String currentWeather = weaterService.getWeatherAtCity(city);
        System.out.println(currentWeather);
        
        //FORECAST
        System.out.println("\n\nWeather in " + city + " in " + forecastHours + " hours");
        String forecast = weaterService.getForecastAtCityInXHours(city, forecastHours);
        System.out.println(forecast);
    }

}
