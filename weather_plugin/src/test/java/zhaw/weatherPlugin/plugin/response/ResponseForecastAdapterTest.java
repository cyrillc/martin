package zhaw.weatherPlugin.plugin.response;

import org.bitpipeline.lib.owm.WeatherForecastResponse;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class ResponseForecastAdapterTest {

    @Mock
    WeatherForecastResponse owmResponseMock;
    @InjectMocks
    ResponseForecastAdapter responseAdapter;
    
    @Before
    public void setUp() throws Exception {}

}
