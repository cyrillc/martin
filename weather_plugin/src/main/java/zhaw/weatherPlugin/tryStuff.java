package zhaw.weatherPlugin;

import java.io.IOException;
import java.util.Date;

import org.bitpipeline.lib.owm.ForecastWeatherData;
import org.bitpipeline.lib.owm.OwmClient;
import org.bitpipeline.lib.owm.WeatherForecastResponse;
import org.json.JSONException;

public class tryStuff {

    public static void main(String[] args) throws JSONException, IOException {
        OwmClient owm = new OwmClient();
        owm.setAPPID("c4cb05905b0c1017d58221beda81460d");
        WeatherForecastResponse owmResponse = owm.forecastWeatherAtCity("Zurich");
        ForecastWeatherData forecastData = owmResponse.getForecasts().get(1);
        Date date = new Date();
        //date.setTime();
        System.out.println(date.getTime());
        System.out.println(new Date(forecastData.getDateTime() * 1000));
    }

}
