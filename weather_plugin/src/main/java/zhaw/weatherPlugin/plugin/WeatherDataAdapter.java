package zhaw.weatherPlugin.plugin;

import java.util.Date;

import org.bitpipeline.lib.owm.WeatherData;

public class WeatherDataAdapter {
    private WeatherData data;

    public WeatherDataAdapter(WeatherData data) {
        this.data = data;
    }

    public boolean hasRain() {
        return this.data.hasRain();
    }

    public Date getDate() {
        return new Date(this.data.getDateTime() * 1000);
    }

    public int getRain() {
        return this.hasRain() ? this.data.getRain() : 0;
    }

    public String getWeatherDescription() {
        return this.data.getWeatherConditions().get(0).getDescription();
    }

    public float getTemperature() {
        return convertKelvinToCelsius(this.data.getTemp());
    }

    public float convertKelvinToCelsius(float kelvin) {
        return (float) (kelvin - 273.15);
    }

    public String getBasicWeatherString() {
        String date = getDate().toString();
        float temp = getTemperature();
        String description = getWeatherDescription();
        int rain = getRain();

        return date + "\n" + description + " Temperature: " + temp + " Rain: "
                + rain;
    }

}
