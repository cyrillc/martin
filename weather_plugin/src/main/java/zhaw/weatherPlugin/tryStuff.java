package zhaw.weatherPlugin;

import java.io.IOException;
import java.util.Date;

import org.bitpipeline.lib.owm.ForecastWeatherData;
import org.bitpipeline.lib.owm.OwmClient;
import org.bitpipeline.lib.owm.WeatherForecastResponse;
import org.json.JSONException;

import zhaw.weatherPlugin.plugin.WeatherService;
import zhaw.weatherPlugin.plugin.exception.WeatherPluginException;

public class tryStuff {

    public static void main(String[] args) throws JSONException, IOException, WeatherPluginException {
        WeatherService service = new WeatherService();
        String msg = service.getForecastAtCityInXHours("Zurich",12);
        System.out.println(msg);
    }

}
