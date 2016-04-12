package ch.zhaw.psit4.martin.common.service;

import static org.junit.Assert.*;

import java.util.List;

import javax.validation.constraints.AssertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ch.zhaw.psit4.martin.common.HistoryItem;
import ch.zhaw.psit4.martin.common.LiquibaseTestFramework;
import ch.zhaw.psit4.martin.common.Request;
import ch.zhaw.psit4.martin.common.Response;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:Beans.xml", "classpath:Beans-unit-tests.xml"})
public class HistoryItemServiceTest {
	@Autowired
    private HistoryItemService historyItemService;
	
	@Autowired
	private LiquibaseTestFramework liquibase;

    @Before
    public void setUp() {
        liquibase.createDatabase("database/db.changeset-schema-latest.xml");
    }

   @Test
   @Transactional
    public void testAdd() {
        Request request = new Request("testRequest");
        Response response = new Response("testResponse");
        HistoryItem hs = new HistoryItem(request, response);
        historyItemService.addHistoryItem(hs);
        
        assertEquals(historyItemService.getHistory().size(), 1);
    }
    
    //@Test
    //public void testGetHistory(){
    //    List<HistoryItem> history = this.historyItemService.getHistory();
    //    Assert.assertTrue(history.size() > 0);
    //    for(HistoryItem item : history){
    //        System.out.println(item.getRequest().getCommand());
    //    }
    //}
    

}
