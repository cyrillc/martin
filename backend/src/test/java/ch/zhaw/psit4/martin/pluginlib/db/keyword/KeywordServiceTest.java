package ch.zhaw.psit4.martin.pluginlib.db.keyword;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ch.zhaw.psit4.martin.common.LiquibaseTestFramework;

import static org.junit.Assert.*;

import java.util.List;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:Beans.xml", "classpath:Beans-unit-tests.xml"})
public class KeywordServiceTest {
	
	@Autowired
	private LiquibaseTestFramework liquibase;

	@Autowired
    private KeywordService keywordService;
    private Log log;

    @Before
    @Transactional
    public void setUp(){
    	liquibase.createDatabase("database/unit-tests/keywordTest/db.keywordUnitTest-1.0.xml");
        log = LogFactory.getLog(KeywordServiceTest.class);        
    }
    
    @Test
    @Transactional
    public void testListKeywords() throws Exception {
        List<Keyword> keywords = keywordService.listKeywords();
        assertEquals(false,keywords.isEmpty());
    }
    
    @Test
    @Transactional
    public void testAddKeyword() throws Exception {
        Keyword newWord = new Keyword();
        newWord.setKeyword("Hallo Welt");
        keywordService.addKeyword(newWord);
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
