package zhaw.weatherPlugin;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.bitpipeline.lib.owm.OwmClient;
import org.bitpipeline.lib.owm.WeatherData;
import org.bitpipeline.lib.owm.WeatherStatusResponse;
import org.bitpipeline.lib.owm.WeatherData.WeatherCondition;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class WeatherServiceTest {

    public WeatherService service;
    
    @Mock
    private OwmClient ownClientMock;
    @Mock
    private WeatherStatusResponse owmResponseMock;
    @Mock
    private WeatherResponseAdapter responseAdapterMock;

    @Before
    public void setUp() throws Exception {
        service = new WeatherService();
        MockitoAnnotations.initMocks(this);
        
        when(ownClientMock.currentWeatherAtCity("testcity")).thenReturn(owmResponseMock);      
    }

    @Test
    public void canReturnWeatherofAKnownCity() {
        
        when(responseAdapterMock.hasWeatherData()).thenReturn(true);
        when(responseAdapterMock.getTemperature()).thenReturn(20.1f);
        when(responseAdapterMock.getWeatherDescription()).thenReturn("mega good");
        when(responseAdapterMock.getRain()).thenReturn(0);
        
        try {
            String weather = service.getWeatherAtCity("testcity");
            assertTrue(weather.toLowerCase().contains("weather in testcity"));
        } catch (WeatherPluginException e) {
            fail();
        }

    }

    @Test
    public void returnNullIfCityIsUnknown() {
        try {
            when(responseAdapterMock.hasWeatherData()).thenReturn(false);
            
            String weather = service.getWeatherAtCity("testcity");
            assertNull(weather);
        } catch (WeatherPluginException e) {
            fail();
        }
    }

}
