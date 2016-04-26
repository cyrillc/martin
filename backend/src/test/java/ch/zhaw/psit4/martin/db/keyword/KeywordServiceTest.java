package ch.zhaw.psit4.martin.db.keyword;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.zhaw.psit4.martin.common.LiquibaseTestFramework;
import ch.zhaw.psit4.martin.db.keyword.Keyword;
import ch.zhaw.psit4.martin.db.keyword.KeywordService;

import static org.junit.Assert.*;

import java.util.List;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    public void setUp(){
    	liquibase.createDatabase("database/unit-tests/keywordTest/db.keywordUnitTest-1.0.xml");
        log = LogFactory.getLog(KeywordServiceTest.class);        
    }
    
    @Test
    public void testListKeywords() throws Exception {
        List<Keyword> keywords = keywordService.listKeywords();
        keywords.stream().forEach(keyword -> log.info(keyword.getId()+", "+keyword.getKeyword()));
        assertEquals(false,keywords.isEmpty());
        assertEquals(6,keywords.size());
    }
    
    @Test
    public void testAddKeyword() throws Exception {
        Keyword newWord = new Keyword("Hallo Welt");
        keywordService.addKeyword(newWord);
        String word = keywordService.getKeywordById(7).getKeyword();
        assertEquals("Hallo Welt", word);
        keywordService.removeKeyword(7);
    }


    @Test
    public void testGetKeywordById() throws Exception {
        assertEquals("weather", keywordService.getKeywordById(1).getKeyword());
        assertEquals(1, keywordService.getKeywordById(1).getId());
        assertEquals("Sunday", keywordService.getKeywordById(6).getKeyword());
        
        assertEquals(null, keywordService.getKeywordById(10));
    }

    @Test
    public void testUpdateKeyword() throws Exception {
        Keyword toChange = keywordService.getKeywordById(3);
        toChange.setKeyword("there");
        keywordService.updateKeyword(toChange);
        assertEquals("there", keywordService.getKeywordById(3).getKeyword());
    }

    @Test
    public void testRemoveKeyword() throws Exception {
        //remove 2 & 6
        keywordService.removeKeyword(2);
        keywordService.removeKeyword(6);
        
        //check
        List<Keyword> keywords = keywordService.listKeywords();
        keywords.stream().forEach(keyword -> log.info(keyword.getId()+", "+keyword.getKeyword()));
        assertEquals(4, keywords.size());
        assertEquals(null, keywordService.getKeywordById(6));
        
    }
}
