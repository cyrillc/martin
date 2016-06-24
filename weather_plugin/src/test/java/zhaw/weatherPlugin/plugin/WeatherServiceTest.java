package zhaw.weatherPlugin.plugin;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import zhaw.weatherPlugin.plugin.OwmClientAdapter;
import zhaw.weatherPlugin.plugin.WeatherDataAdapter;
import zhaw.weatherPlugin.plugin.WeatherService;
import zhaw.weatherPlugin.plugin.exception.WeatherPluginException;
import zhaw.weatherPlugin.plugin.response.ResponseForecastAdapter;
import zhaw.weatherPlugin.plugin.response.ResponseStatusAdapter;

public class WeatherServiceTest {

    @InjectMocks
    public WeatherService service;

    @Mock
    private OwmClientAdapter clientMock;
    @Mock
    private ResponseForecastAdapter responseForecastMock;
    @Mock
    private ResponseStatusAdapter responseStatusMock;
    @Mock
    private WeatherDataAdapter weatherDataMock;

    @Before
    public void setUp() throws Exception {
        service = new WeatherService();
        MockitoAnnotations.initMocks(this);

        when(clientMock.currentWeatherAtCity("testcity"))
                .thenReturn(responseStatusMock);
        when(clientMock.forecastWeatherAtCity("testcity"))
                .thenReturn(responseForecastMock);
        when(responseStatusMock.getWeatherData()).thenReturn(weatherDataMock);
    }

    @Test
    public void canReturnWeatherofAKnownCity() {
        when(responseStatusMock.hasWeatherData()).thenReturn(true);
        when(weatherDataMock.getBasicWeatherString(any())).thenReturn("megaGoood!");

        try {
            String weather = service.getWeatherAtCity(null, "testcity");
            assertNotNull(weather);
        } catch (WeatherPluginException e) {
            fail();
        }

    }

    @Test
    public void returnNullIfCityIsUnknown_currentWeather() {
        try {
            when(responseStatusMock.hasWeatherData()).thenReturn(false);

            String weather = service.getWeatherAtCity(null, "testcity");
            assertNull(weather);
        } catch (WeatherPluginException e) {
            fail();
        }
    }

    @Test
    public void returnNullIfCityIsUnknown_forecast() {
        try {
            when(responseForecastMock.hasForecast()).thenReturn(false);

            String weather = service.getForecastAtCityInXHours(null, "testcity", 12);
            assertNull(weather);
        } catch (WeatherPluginException e) {
            fail();
        }
    }
}
