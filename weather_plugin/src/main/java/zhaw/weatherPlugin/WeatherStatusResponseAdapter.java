package zhaw.weatherPlugin;

import org.bitpipeline.lib.owm.WeatherData;
import org.bitpipeline.lib.owm.WeatherStatusResponse;

public class WeatherStatusResponseAdapter {

    private WeatherStatusResponse owmResponse;
    private WeatherDataAdapter data;

    public WeatherStatusResponseAdapter(WeatherStatusResponse response) {
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
