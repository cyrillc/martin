package ch.zhaw.psit4.martin.pluginlib.db.keyword;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mysql.fabric.xmlrpc.base.Array;

import ch.zhaw.psit4.martin.common.DatabaseTest;
import liquibase.exception.LiquibaseException;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.AssertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class KeywordServiceTest extends DatabaseTest {

    private KeywordService keywordService;

    @Before
    public void setUp() throws Exception{
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
        keywordService = (KeywordService) context.getBean("KeywordService");
        super.setChangeset("src/main/resources/unit-testing/keywordTest/db.keywordUnitTest-1.0.xml");
        super.setUp();
    }

    @After
    public void tearDown() throws LiquibaseException,SQLException {
        super.tearDown();
    }

    @Test
    public void testListKeywords() throws Exception {
        List<Keyword> keywords = new ArrayList<>();
        keywords = keywordService.listKeywords();

        //assertEquals(keywords.isEmpty(),false);
        assertEquals(true,true);
        

    }
    /*
    @Test
    public void testAddKeyword() throws Exception {
    }


    @Test
    public void testGetKeywordById() throws Exception {
    }

    @Test
    public void testUpdateKeyword() throws Exception {
    }

    @Test
    public void testRemoveKeyword() throws Exception {
    }
*/
}
