package ch.zhaw.psit4.martin.db.historyitem;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.zhaw.psit4.martin.common.Request;
import ch.zhaw.psit4.martin.common.Response;
import ch.zhaw.psit4.martin.db.historyitem.HistoryItem;
import ch.zhaw.psit4.martin.db.historyitem.HistoryItemDAO;
import ch.zhaw.psit4.martin.db.historyitem.HistoryItemService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:Beans.xml",
        "classpath:Beans-unit-tests.xml" })
public class HistoryItemServiceTest {

    class DaoStub extends HistoryItemDAO {
        public boolean addIsCalled = false;
        public boolean getAllIsCalled = false;

        public void add(HistoryItem historyItem) {
            this.addIsCalled = true;
        }

        public List<HistoryItem> getAll() {
            this.getAllIsCalled = true;
            return new ArrayList<>();
        }
    }

    @Autowired
    private HistoryItemService historyItemService;
    private DaoStub daoStub;

    @Before
    public void setUp() {
        daoStub = new DaoStub();
        historyItemService.setHistoryItemDAO(daoStub);
    }

    @Test
    public void addHistoryItemUsingDAO() {

        Request request = new Request("testRequest");
        Response response = new Response("testResponse");
        HistoryItem hs = new HistoryItem(request, response);
        historyItemService.addHistoryItem(hs);

        assertTrue(this.daoStub.addIsCalled);
    }

    @Test
    public void canGetAHistoryItemListUsingDAO() {
        this.historyItemService.getHistory();
        Assert.assertTrue(this.daoStub.getAllIsCalled);
    }

}
