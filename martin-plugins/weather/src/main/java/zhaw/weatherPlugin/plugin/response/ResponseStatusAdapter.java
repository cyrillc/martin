package zhaw.weatherPlugin.plugin.response;



import org.bitpipeline.lib.owm.WeatherStatusResponse;

import zhaw.weatherPlugin.plugin.WeatherDataAdapter;

public class ResponseStatusAdapter {

    private WeatherStatusResponse owmResponse;
    private WeatherDataAdapter data;

    public ResponseStatusAdapter(WeatherStatusResponse response) {
        this.owmResponse = response;
        if (owmResponse.hasWeatherStatus()) {
            this.data = new WeatherDataAdapter(
                    owmResponse.getWeatherStatus().get(0));
        }
    }

    public WeatherStatusResponse getOwmResponse() {
        return owmResponse;
    }

    public WeatherDataAdapter getWeatherData() {
        return data;
    }

    public boolean hasWeatherData() {
        return (this.data == null) ? false : true;
    }

}
