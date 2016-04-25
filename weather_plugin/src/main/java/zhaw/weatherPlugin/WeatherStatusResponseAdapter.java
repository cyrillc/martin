package zhaw.weatherPlugin;

import org.bitpipeline.lib.owm.WeatherData;
import org.bitpipeline.lib.owm.WeatherStatusResponse;

public class WeatherStatusResponseAdapter {

    private WeatherStatusResponse owmResponse;
    private WeatherData weatherData;

    public WeatherStatusResponseAdapter(WeatherStatusResponse response) {
        this.owmResponse = response;
        if (owmResponse.hasWeatherStatus()) {
            this.weatherData = owmResponse.getWeatherStatus().get(0);
        }
    }

    public WeatherStatusResponse getOwmResponse() {
        return owmResponse;
    }

    public WeatherData getWeatherData() {
        return weatherData;
    }

    public boolean hasWeatherData() {
        return (this.weatherData == null) ? false : true;
    }

    public boolean hasRain() {
        return (this.weatherData.getRain() == Integer.MIN_VALUE) ? false : true;
    }

    public int getRain() {
        return this.hasRain() ? this.weatherData.getRain() : 0;
    }

    public String getWeatherDescription() {
        return this.weatherData.getWeatherConditions().get(0).getDescription();
    }

    public float getTemperature() {
        return convertKelvinToCelsius(this.weatherData.getTemp());
    }

    public float convertKelvinToCelsius(float kelvin) {
        return (float) (kelvin - 273.15);
    }

}
