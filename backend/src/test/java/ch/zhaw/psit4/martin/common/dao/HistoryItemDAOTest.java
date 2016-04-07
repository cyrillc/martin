package ch.zhaw.psit4.martin.common.dao;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import ch.zhaw.psit4.martin.common.DatabaseTest;
import ch.zhaw.psit4.martin.common.HistoryItem;
import ch.zhaw.psit4.martin.common.Request;
import ch.zhaw.psit4.martin.common.Response;
import liquibase.exception.LiquibaseException;

public class HistoryItemDAOTest extends DatabaseTest{

    public HistoryItemDAO historyItemDAO;

    @Before
    public void setUp() throws ClassNotFoundException, SQLException, LiquibaseException, Exception {
    	// Set the database-version to use
    	super.setChangeset("src/main/resources/database/db.dev.xml");
    	super.setUp();
    	
        this.historyItemDAO = new HistoryItemDAO();
        this.historyItemDAO.setSessionFactory(super.getHibernateSessionFactory());
    }
    
    @Test
    public void someTest(){
    	// ToDo: Implement something
    	
    	//Request request = new Request("test");
    	//Response response = new Response("test");
    	//HistoryItem historyItem = new HistoryItem(request, response);
    	//this.historyItemDAO.add(historyItem);
    	
    	assertTrue(true);
    }
    
    @After
    public void tearDown() throws LiquibaseException, SQLException{
    	super.tearDown();
    }

}
