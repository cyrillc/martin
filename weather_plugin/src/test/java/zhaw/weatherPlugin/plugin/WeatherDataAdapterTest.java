package zhaw.weatherPlugin.plugin;

import org.bitpipeline.lib.owm.WeatherData;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;


import static org.mockito.Mockito.*;

public class WeatherDataAdapterTest {
    
    @Mock
    private WeatherData owmDataMock;
    
    @InjectMocks
    WeatherDataAdapter dataAdapter;

    @Before
    public void setUp() throws Exception {
        dataAdapter = new WeatherDataAdapter(owmDataMock);
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void itReturnsTemperaturInCelsius(){
        when(owmDataMock.getTemp()).thenReturn(300f);
        
        assertEquals(dataAdapter.getTemperature(), 26.85f, 0.001);
    }

}
