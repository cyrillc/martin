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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class KeywordServiceTest extends DatabaseTest {

    private KeywordService keywordService;
    private Log log;

    @Before
    public void setUp() throws Exception{
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
        keywordService = (KeywordService) context.getBean("keywordService");
        
        log = LogFactory.getLog(KeywordServiceTest.class);
        
        super.setChangesetPath("database/unit-tests/keywordTest/db.keywordUnitTest-1.0.xml");
        super.setUp();
    }


    @After
    public void tearDown() throws LiquibaseException,SQLException {
        super.tearDown();
    }

    @Test
    public void testListKeywords() throws Exception {
        List<Keyword> keywords = new ArrayList<>();
        keywords = (ArrayList) keywordService.listKeywords();
        

        assertEquals(false,keywords.isEmpty());
        //assertEquals(true,true);
        

    }
    @Test
    public void testAddKeyword() throws Exception {
        Keyword newWord = new Keyword();
        newWord.setKeyword("Hallo Welt");
        //keywordService.addKeyword(newWord);
        

    }

    /*

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
