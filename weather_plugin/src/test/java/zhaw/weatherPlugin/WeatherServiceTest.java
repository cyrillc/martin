package zhaw.weatherPlugin;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class WeatherServiceTest {

    public WeatherService service;

    @Before
    public void setUp() throws Exception {
        service = new WeatherService();
    }
    
    @Test
    public void canReturnWeatherofAKnownCity(){
        try {
            String weather = service.getWeatherAtCity("Zurich");
            assertTrue(weather.toLowerCase().contains("weather in zurich"));
        } catch (WeatherPluginException e) {
            fail();
        }
        
    }
    
    @Test
    public void returnNullIfCityIsUnknown(){
        try {
            String weather = service.getWeatherAtCity("Locarno");
            assertNull(weather);
        } catch (WeatherPluginException e) {
            fail();
        }
    }

}
