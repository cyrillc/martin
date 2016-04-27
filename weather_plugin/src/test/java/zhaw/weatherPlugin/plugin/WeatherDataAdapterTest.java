package zhaw.weatherPlugin.plugin;

import org.bitpipeline.lib.owm.WeatherData;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

import java.util.Calendar;
import java.util.Date;

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
    public void itReturnsTemperaturInCelsius() {
        when(owmDataMock.getTemp()).thenReturn(300f);

        assertEquals(dataAdapter.getTemperature(), 26.85f, 0.001);
    }

    @Test
    public void itReturnsTheRightTimeAsDateObject() {
        Date now = new Date();
        when(owmDataMock.getDateTime()).thenReturn(now.getTime() / 1000);
        Date returnedDate = dataAdapter.getDate();
        Calendar rightValue = Calendar.getInstance();
        Calendar toTest = Calendar.getInstance();
        rightValue.setTime(now);
        toTest.setTime(returnedDate);
        assertEquals(toTest.get(Calendar.YEAR), rightValue.get(Calendar.YEAR));
        assertEquals(toTest.get(Calendar.DAY_OF_YEAR),
                rightValue.get(Calendar.DAY_OF_YEAR));
        assertEquals(toTest.get(Calendar.HOUR_OF_DAY), rightValue.get(Calendar.HOUR_OF_DAY));
        assertEquals(toTest.get(Calendar.MINUTE), rightValue.get(Calendar.MINUTE));
    }

}
