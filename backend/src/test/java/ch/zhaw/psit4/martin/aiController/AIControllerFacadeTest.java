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
import ch.zhaw.psit4.martin.common.HistoryItem;
import ch.zhaw.psit4.martin.common.Request;
import ch.zhaw.psit4.martin.common.Response;
import ch.zhaw.psit4.martin.common.service.HistoryItemService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:Beans.xml" })
public class AIControllerFacadeTest {

    private HistoryItemService historyItemServiceMock;

    @Autowired
    private AIControllerFacade aiController;

    @Before
    public void setUp() {
        historyItemServiceMock = Mockito.mock(HistoryItemService.class);
        GenericApplicationContext mockContext = new GenericApplicationContext();
        mockContext.getBeanFactory().registerSingleton("historyItemService",
                historyItemServiceMock);
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

}
