package ch.zhaw.psit4.martin.db;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.zhaw.psit4.martin.common.LiquibaseTestFramework;
import ch.zhaw.psit4.martin.db.author.Author;
import ch.zhaw.psit4.martin.db.author.AuthorService;
import ch.zhaw.psit4.martin.db.function.Function;
import ch.zhaw.psit4.martin.db.function.FunctionService;
import ch.zhaw.psit4.martin.db.parameter.ParameterService;
import ch.zhaw.psit4.martin.db.plugin.Plugin;
import ch.zhaw.psit4.martin.db.plugin.PluginService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:Beans.xml", "classpath:Beans-unit-tests.xml"})
public class PluginServiceTest {

	@Autowired
	private LiquibaseTestFramework liquibase;

	@Autowired
    private PluginService pluginService;
	
	@Autowired
	private AuthorService authorService;
	
	@Autowired
	private FunctionService functionService;
	
	@Autowired
    private ParameterService parameterService;
    private Log log;

    @Before
    public void setUp(){
    	liquibase.createDatabase("database/unit-tests/pluginTest/db.pluginUnitTest-1.0.xml");
        log = LogFactory.getLog(PluginServiceTest.class);        
    }

    @Test
    public void testListplugins() throws Exception {
        List<Plugin> plugins = pluginService.listPlugins();
        plugins.stream().forEach(plugin -> printPlugin(plugin));
        assertEquals(false,plugins.isEmpty());
        
    }

    private void printPlugin(Plugin plugin) {
        StringBuilder str  = new StringBuilder(plugin.getId());
        str.append(", ");
        str.append(plugin.getName());
        str.append(" \"");
        str.append(plugin.getDescription());
        str.append("\"");
        str.append("\nfrom ");
        str.append(plugin.getAuthor().getName());
        str.append("\nFunctions: ");
        plugin.getFunctions().stream().forEach(function -> str.append("\n->"+function.getName()));

        log.info(str);
    }

   
    @Test
    public void testGetPluginById() {
        Plugin plugin = pluginService.getPluginById(1);
        assertNotNull(plugin);
        assertEquals(1,plugin.getId());
        assertEquals("Tells the weather for a given time and location",plugin.getDescription());
        assertEquals("WeatherApp",plugin.getName());
        assertEquals(1,plugin.getAuthor().getId());
        
    }
    
    @Test
    public void testAddPlugin() {
        Plugin newPlugin = createPlugin();
        pluginService.addPlugin(newPlugin);
        
        int pluginID = newPlugin.getId();      
        Plugin plugin = pluginService.getPluginById(pluginID);
        
        assertNotNull(plugin);
        assertEquals(pluginID,plugin.getId());
        assertEquals("Test Description",plugin.getDescription());
        assertEquals("Testname",plugin.getName());
        
    }
    
    private Plugin createPlugin() {  
        Plugin newPlugin = new Plugin();
        Author author = new Author();
        author.setName("TestAuthor");
        author.setEmail("testAUthor@mail.ch");
        authorService.addAuthor(author);
        
        newPlugin.setAuthor(author);
        newPlugin.setDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
        newPlugin.setDescription("Test Description");
        newPlugin.setName("Testname");
        newPlugin.setUuid(UUID.randomUUID().toString());
        return newPlugin;      
    }

  
    /*
    @Test
    public void testRemovePlugin() {
        pluginService.removePlugin(1);
        
        assertNull(pluginService.getPluginById(1));
        assertNull(functionService.getFunctionById(1));
        assertNull(parameterService.getParameterById(1));
        assertNotNull(authorService.getAuthorById(1));
    }
*/
    
}
