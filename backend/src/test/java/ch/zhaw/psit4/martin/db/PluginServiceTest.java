package ch.zhaw.psit4.martin.db;

import static org.junit.Assert.*;

import java.util.List;

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
import ch.zhaw.psit4.martin.db.plugin.Plugin;
import ch.zhaw.psit4.martin.db.plugin.PluginService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:Beans.xml", "classpath:Beans-unit-tests.xml"})
public class PluginServiceTest {

	@Autowired
	private LiquibaseTestFramework liquibase;

	@Autowired
    private PluginService pluginService;
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
    public void testAddPlugin() {
        Plugin newPlugin = new Plugin(3);
        Author author = new Author(3);
        author.setName("TestAuthor");
        newPlugin.setAuthor(author);
        newPlugin.setDescription("Test Description");
        newPlugin.setName("Testname");
        
        pluginService.addPlugin(newPlugin);
        
        Plugin plugin = pluginService.getPluginById(3);
        assertNotNull(plugin);
        assertEquals(3,plugin.getId());
        assertEquals("Test Description",plugin.getDescription());
        assertEquals("Testname",plugin.getName());
        assertEquals(3,plugin.getAuthor().getId());
        
    }

   

    @Test
    public void testGetPluginById() {
        Plugin plugin = pluginService.getPluginById(2);
        assertNotNull(plugin);
        assertEquals(2,plugin.getId());
        assertEquals("Tells the weather for a given time and location",plugin.getDescription());
        assertEquals("WeatherApp",plugin.getName());
        assertEquals(1,plugin.getAuthor().getId());
        
    }

    @Test
    public void testRemovePlugin() {
        pluginService.removePlugin(1);
        assertNull(pluginService.getPluginById(1));
    }

    
}
