package zhaw.weatherPlugin.plugin;

import java.text.SimpleDateFormat;
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
        return this.owmData.getTemperature().getTemp();
    }

    public float getTemperatureMax() {
        return this.owmData.getTemperature().getTempDayMax();
    }

    public float getTemperatureMin() {
        return this.owmData.getTemperature().getTempDayMin();
    }

    public float convertKelvinToCelsius(float kelvin) {
        return (float) (kelvin - 273.15);
    }

    public String getBasicWeatherString() {
        String when = new SimpleDateFormat("EEEE dd.MM.yyyy").format(getDate());
        return when.concat(": ").concat(getWeatherDescription()
                .concat(" ").concat(getTemperatureString()));
    }

    public String getTemperatureString() {
        String temp = "-";
        if (!Float.isNaN(getTemperatureMax())
                && !Float.isNaN(getTemperatureMin())) {
            temp = "Min: " + Float.toString(getTemperatureMin()) + " Max: "
                    + Float.toString(getTemperatureMax());
        } else if (!Float.isNaN(getTemperature())) {
            temp = Float.toString(getTemperature());
        }
        return "Temperature: " + temp;
    }

}
