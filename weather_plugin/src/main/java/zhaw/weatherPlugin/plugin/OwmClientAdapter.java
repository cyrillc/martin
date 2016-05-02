package zhaw.weatherPlugin.plugin;

import java.io.IOException;

import org.bitpipeline.lib.owm.OwmClient;
import org.bitpipeline.lib.owm.WeatherForecast16Response;
import org.bitpipeline.lib.owm.WeatherForecastResponse;
import org.bitpipeline.lib.owm.WeatherStatusResponse;
import org.json.JSONException;

import zhaw.weatherPlugin.plugin.response.ResponseForecast16Adapter;
import zhaw.weatherPlugin.plugin.response.ResponseForecastAdapter;
import zhaw.weatherPlugin.plugin.response.ResponseStatusAdapter;

public class OwmClientAdapter {
    private OwmClient owmClient;

    public OwmClientAdapter() {
        this.owmClient = new OwmClient(OwmClient.Units.METRIC);
        this.owmClient.setAPPID("c4cb05905b0c1017d58221beda81460d");
    }

    public ResponseStatusAdapter currentWeatherAtCity(String city)
            throws JSONException, IOException {
        WeatherStatusResponse r = owmClient.currentWeatherAtCity(city);
        return new ResponseStatusAdapter(r);
    }

    public ResponseForecastAdapter forecastWeatherAtCity(String city)
            throws JSONException, IOException {
        WeatherForecastResponse r = owmClient.forecastWeatherAtCity(city);
        return new ResponseForecastAdapter(r);
    }

    public ResponseForecast16Adapter dailyForecastAtCity(String city)
            throws JSONException, IOException {
        WeatherForecast16Response r = owmClient
                .dailyForecastWeatherAtCity(city);
        return new ResponseForecast16Adapter(r);
    }
}
