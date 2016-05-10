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
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.zhaw.psit4.martin.api.MartinPlugin;
import ch.zhaw.psit4.martin.common.Call;
import ch.zhaw.psit4.martin.common.ExtendedRequest;
import ch.zhaw.psit4.martin.models.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:Beans.xml", "classpath:Beans-unit-tests.xml"})
public class PluginLibraryTest {

    private ExtendedRequest mockedRequests[];
    private Map<String, MartinPlugin> mockedExtensions;
    private UUID uuid;

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Spy
    private PluginLibrary spyLib;

    @Before
    public void setUp() {
        // Create spy and Autowire dependencies
        MockitoAnnotations.initMocks(this);
        beanFactory.autowireBean(spyLib);

        // Create request mocks
        uuid = UUID.randomUUID();
        mockedRequests = new ExtendedRequest[20];
        for (int i = 0; i < mockedRequests.length; i++) {
            // Create call mocks
        	Plugin testPlugin = Mockito.mock(Plugin.class);
        	Mockito.when(testPlugin.getDescription()).thenReturn("TestModule");
        
        	Function testFunction = Mockito.mock(Function.class);
        	Mockito.when(testFunction.getDescription()).thenReturn("testFeature");
        	
            List<Call> calls = new ArrayList<Call>();
            Call mockedCall = Mockito.mock(Call.class);
            Mockito.when(mockedCall.getArguments()).thenReturn(null);
            Mockito.when(mockedCall.getFunction()).thenReturn(testFunction);
            Mockito.when(mockedCall.getPlugin()).thenReturn(testPlugin);
            calls.add(mockedCall);

            mockedRequests[i] = Mockito.mock(ExtendedRequest.class);
            Mockito.when(mockedRequests[i].getID()).thenReturn(uuid);
            Mockito.when(mockedRequests[i].getCalls()).thenReturn(calls);
        }


        // create Mocked extentions
        MartinPlugin mockedService = Mockito.mock(MartinPlugin.class);
        mockedExtensions = new HashMap<String, MartinPlugin>();
        mockedExtensions.put("TestModule", mockedService);
    }

    @Test
    public void testeEecuteRequest() {
        // mock library
        spyLib.setPluginExtentions(mockedExtensions);
        // Create lib with spy
        for (int i = 0; i < mockedRequests.length; i++) {
            Response resp = spyLib.executeRequest(mockedRequests[i]);
            assertNotNull(resp);
        }
    }
}
