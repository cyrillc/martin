package zhaw.weatherPlugin.plugin;

import java.util.Date;

import org.bitpipeline.lib.owm.WeatherData;

public class WeatherDataAdapter {
    private WeatherData owmData;

    public WeatherDataAdapter(WeatherData data) {
        this.owmData = data;
    }

    public WeatherData getOwmData() {
        return owmData;
    }

    public boolean hasRain() {
        return this.owmData.hasRain();
    }

    public Date getDate() {
        // owm returns date as a unix timestamp
        return new Date(this.owmData.getDateTime() * 1000);
    }

    public int getRain() {
        return this.hasRain() ? this.owmData.getRain() : 0;
    }

    public String getWeatherDescription() {
        return this.owmData.getWeatherConditions().get(0).getDescription();
    }

    public float getTemperature() {
        return convertKelvinToCelsius(this.owmData.getTemp());
    }

    public float convertKelvinToCelsius(float kelvin) {
        return (float) (kelvin - 273.15);
    }

    public String getBasicWeatherString() {
        return getDate().toString().concat("\n").concat(getWeatherDescription());
    }

}
