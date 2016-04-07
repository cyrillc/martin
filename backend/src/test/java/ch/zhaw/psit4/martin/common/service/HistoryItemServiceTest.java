package ch.zhaw.psit4.martin.common.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ch.zhaw.psit4.martin.common.HistoryItem;
import ch.zhaw.psit4.martin.common.Request;
import ch.zhaw.psit4.martin.common.Response;

public class HistoryItemServiceTest {
    private HistoryItemService historyItemService;

    @Before
    public void setUp() {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "Beans.xml");
        historyItemService = (HistoryItemService) context
                .getBean("historyItemService");
    }

    @Test
    public void testAdd() {
        Request request = new Request("testRequest");
        Response response = new Response("testResponse");
        HistoryItem hs = new HistoryItem(request, response);
        this.historyItemService.addHistoryItem(hs);
    }

}
