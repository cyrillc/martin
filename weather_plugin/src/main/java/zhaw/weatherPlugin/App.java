package zhaw.weatherPlugin;

import java.io.IOException;

import org.json.JSONException;

public class App {
    public static void main(String[] args) throws JSONException, IOException {
        Controller controller = new Controller();
        try {
            String currentWeather = controller.getWeatherAtCity("Lugano");
            System.out.println(currentWeather);
        } catch (WeatherPluginException e) {
            System.out.println("Something went Wrong");
        }
    }
}
