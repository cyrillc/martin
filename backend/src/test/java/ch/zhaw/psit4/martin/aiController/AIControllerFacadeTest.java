package ch.zhaw.psit4.martin.aiController;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.zhaw.psit4.martin.boot.MartinBoot;
import ch.zhaw.psit4.martin.common.ExtendedRequest;
import ch.zhaw.psit4.martin.common.HistoryItem;
import ch.zhaw.psit4.martin.common.Request;
import ch.zhaw.psit4.martin.common.Response;
import ch.zhaw.psit4.martin.common.service.HistoryItemService;
import ch.zhaw.psit4.martin.pluginlib.IPluginLibrary;
import ch.zhaw.psit4.martin.requestProcessor.RequestProcessor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:Beans.xml" })
public class AIControllerFacadeTest {

    private HistoryItemService historyItemServiceMock;
    private RequestProcessor requestProcessorMock;
    private IPluginLibrary pluginLibraryMock;

    @Autowired
    private AIControllerFacade aiController;

    @Before
    public void setUp() {
        historyItemServiceMock = Mockito.mock(HistoryItemService.class);
        requestProcessorMock = Mockito.mock(RequestProcessor.class);
        pluginLibraryMock = Mockito.mock(IPluginLibrary.class);
        
        GenericApplicationContext mockContext = new GenericApplicationContext();
        
        mockContext.getBeanFactory().registerSingleton("historyItemService",
                historyItemServiceMock);
        mockContext.getBeanFactory().registerSingleton("RequestProcessor",
                requestProcessorMock);
        mockContext.getBeanFactory().registerSingleton("IPluginLibrary",
                pluginLibraryMock);
        
        mockContext.refresh();
        MartinBoot.setContext(mockContext);
    }

    @Test
    public void canGetAListOfHistoryItems() {
        ArrayList<HistoryItem> getHistoryResult = new ArrayList<>();
        getHistoryResult.add(new HistoryItem(new Request("command1"),
                new Response("response1")));
        getHistoryResult.add(new HistoryItem(new Request("command2"),
                new Response("response2")));
        getHistoryResult.add(new HistoryItem(new Request("command3"),
                new Response("response3")));

        when(historyItemServiceMock.getHistory()).thenReturn(getHistoryResult);

        List<HistoryItem> list = aiController.getHistory();
        assertEquals(3, list.size());
        assertEquals("command1", list.get(0).getRequest().getCommand());
    }
    
    @Test
    public void saveAHistoryItemWhenRequestMakeSense() throws Exception{
        Request request = new Request("request test");
        ExtendedRequest extRequest = new ExtendedRequest();
        Response response = new Response("response test");
        HistoryItem historyItem = new HistoryItem(request, response);
        
        when(requestProcessorMock.extend(request)).thenReturn(extRequest);
        when(pluginLibraryMock.executeRequest(extRequest)).thenReturn(response);
        aiController.elaborateRequest(request);
        verify(historyItemServiceMock).addHistoryItem(historyItem);
        
    }

}
