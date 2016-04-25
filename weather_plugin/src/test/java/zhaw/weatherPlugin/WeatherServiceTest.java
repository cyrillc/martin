package zhaw.weatherPlugin;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.bitpipeline.lib.owm.OwmClient;
import org.bitpipeline.lib.owm.WeatherStatusResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class WeatherServiceTest {

    @InjectMocks
    public WeatherService service;

    @Mock
    private OwmClient ownClientMock;
    @Mock
    private WeatherStatusResponse owmResponseMock;
    @Mock
    private WeatherStatusResponseAdapter responseAdapterMock;
    @Mock
    private ResponseAdapterFactory responseAdapterFactoryMock;

    @Before
    public void setUp() throws Exception {
        service = new WeatherService();
        MockitoAnnotations.initMocks(this);

        when(ownClientMock.currentWeatherAtCity("testcity"))
                .thenReturn(owmResponseMock);
        when(responseAdapterFactoryMock.getResponseAdapter(owmResponseMock))
                .thenReturn(responseAdapterMock);
    }

    @Test
    public void canReturnWeatherofAKnownCity() {
        when(responseAdapterFactoryMock.getResponseAdapter(owmResponseMock))
                .thenReturn(responseAdapterMock);
        when(responseAdapterMock.hasWeatherData()).thenReturn(true);
        when(responseAdapterMock.getTemperature()).thenReturn(20.1f);
        when(responseAdapterMock.getWeatherDescription())
                .thenReturn("mega good");
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
