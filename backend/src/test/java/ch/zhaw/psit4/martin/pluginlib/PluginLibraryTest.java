package ch.zhaw.psit4.martin.pluginlib;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.zhaw.psit4.martin.api.PluginService;
import ch.zhaw.psit4.martin.common.Call;
import ch.zhaw.psit4.martin.common.ExtendedRequest;
import ch.zhaw.psit4.martin.common.Response;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:Beans.xml", "classpath:Beans-unit-tests.xml"})
public class PluginLibraryTest {
    
    private ExtendedRequest mockedRequests[];
    private Map<String, PluginService> mockedExtensions;
    private UUID uuid;
    
    @Autowired
    private PluginLibrary spyLib;
 
    @Before
    public void setUp() { 
        // Create request mocks
        uuid = UUID.randomUUID();
        mockedRequests = new ExtendedRequest[20];
        for (int i = 0; i < mockedRequests.length; i++) {
            // Create call mocks
            List<Call> calls = new ArrayList<Call>();
            Call mockedCall = Mockito.mock(Call.class);
            Mockito.when(mockedCall.getArguments()).thenReturn(null);
            Mockito.when(mockedCall.getFeature()).thenReturn("testFeature");
            Mockito.when(mockedCall.getPlugin()).thenReturn("TestModule");
            calls.add(mockedCall);

            mockedRequests[i] = Mockito.mock(ExtendedRequest.class);
            Mockito.when(mockedRequests[i].getID()).thenReturn(uuid);
            Mockito.when(mockedRequests[i].getCalls()).thenReturn(calls);
        }
        
        

        // create Mocked extentions
        PluginService mockedService = Mockito.mock(PluginService.class);
        mockedExtensions = new HashMap<String, PluginService>();
        mockedExtensions.put("TestModule", mockedService);

        // create library
        spyLib.setPluginExtentions(mockedExtensions);
    }

    @Test
    public void testeEecuteRequest() {
        // Create lib with spy
        for (int i = 0; i < mockedRequests.length; i++) {
            Response resp = spyLib.executeRequest(mockedRequests[i]);
            assertNotNull(resp);
        }
    }
}
