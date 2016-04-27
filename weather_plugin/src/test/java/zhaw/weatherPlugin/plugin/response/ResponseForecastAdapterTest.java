package zhaw.weatherPlugin.plugin.response;

import org.bitpipeline.lib.owm.ForecastWeatherData;
import org.bitpipeline.lib.owm.WeatherForecastResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import zhaw.weatherPlugin.plugin.WeatherDataAdapter;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ResponseForecastAdapterTest {

    @Mock
    WeatherForecastResponse owmResponseMock;
    @InjectMocks
    ResponseForecastAdapter responseAdapter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        responseAdapter = new ResponseForecastAdapter(owmResponseMock);
    }

    @Test
    public void givenADateCanSearchTheClosestForecast() {

        ForecastWeatherData data1 = mock(ForecastWeatherData.class);
        ForecastWeatherData data2 = mock(ForecastWeatherData.class);
        ForecastWeatherData data3 = mock(ForecastWeatherData.class);

        when(data1.getDateTime()).thenReturn((long) 1461790800);
        when(data2.getDateTime()).thenReturn((long) 1461801600);
        when(data3.getDateTime()).thenReturn((long) 1461812400);

        List<ForecastWeatherData> dataList;
        dataList = Arrays.asList(data1, data2, data3);

        when(owmResponseMock.getForecasts()).thenReturn(dataList);

        long secondDateTimePlus1Hour = ((long)1461801600 + (60*60)) * 1000;
        Date searchDate = new Date(secondDateTimePlus1Hour);
        WeatherDataAdapter returnedData;
        returnedData = responseAdapter.searchClosestForecastFrom(searchDate);
        assertEquals(data2, returnedData.getOwmData());
        
        long secondDateTimePlus2Hour = ((long)1461801600 + (60*60*2)) * 1000;
        searchDate = new Date(secondDateTimePlus2Hour);
        returnedData = responseAdapter.searchClosestForecastFrom(searchDate);
        assertEquals(data3, returnedData.getOwmData());
    }

}
