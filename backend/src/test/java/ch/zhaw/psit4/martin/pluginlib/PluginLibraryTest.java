package ch.zhaw.psit4.martin.pluginlib;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.support.GenericApplicationContext;

import ch.zhaw.psit4.martin.api.PluginService;
import ch.zhaw.psit4.martin.boot.MartinBoot;
import ch.zhaw.psit4.martin.common.Call;
import ch.zhaw.psit4.martin.common.ExtendedRequest;
import ch.zhaw.psit4.martin.common.Response;

public class PluginLibraryTest {
    private PluginLibrary spyLib;
    private ExtendedRequest mockedRequests[];
    private Map<String, PluginService> mockedExtensions;
    private UUID uuid;

    @Before
    public void setUp() {
        // Create Mock for MartinContextAccessor
        MartinContextAccessor context = Mockito
                .mock(MartinContextAccessor.class);
        GenericApplicationContext mockContext = new GenericApplicationContext();
        mockContext.getBeanFactory().registerSingleton("MartinContextAccessor",
                context);
        mockContext.refresh();
        MartinBoot.setContext(mockContext);

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
        spyLib = Mockito.spy(new PluginLibrary());
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
