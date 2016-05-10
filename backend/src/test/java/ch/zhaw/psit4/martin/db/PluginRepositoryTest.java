package ch.zhaw.psit4.martin.db;

import static org.junit.Assert.*;

import java.util.Calendar;
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
import ch.zhaw.psit4.martin.models.Author;
import ch.zhaw.psit4.martin.models.Plugin;
import ch.zhaw.psit4.martin.models.repositories.AuthorRepository;
import ch.zhaw.psit4.martin.models.repositories.PluginRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:Beans.xml", "classpath:Beans-unit-tests.xml" })
public class PluginRepositoryTest {

	@Autowired
	private LiquibaseTestFramework liquibase;

	@Autowired
	private PluginRepository pluginRepository;

	@Autowired
	private AuthorRepository authorRepository;

	private Log log;

	@Before
	public void setUp() {
		liquibase.createDatabase("database/unit-tests/pluginTest/db.pluginUnitTest-1.0.xml");
		log = LogFactory.getLog(PluginRepositoryTest.class);
	}

	@Test
	public void testListplugins() throws Exception {
		List<Plugin> plugins = pluginRepository.findAll();
		plugins.stream().forEach(plugin -> printPlugin(plugin));
		assertEquals(false, plugins.isEmpty());

	}

	private void printPlugin(Plugin plugin) {
		StringBuilder str = new StringBuilder(plugin.getId());
		str.append(", ");
		str.append(plugin.getName());
		str.append(" \"");
		str.append(plugin.getDescription());
		str.append("\"");
		str.append("\nfrom ");
		str.append(plugin.getAuthor().getName());
		str.append("\nFunctions: ");
		plugin.getFunctions().stream().forEach(function -> str.append("\n->" + function.getName()));

		log.info(str);
	}

	@Test
	public void testGetPluginById() {
		Plugin plugin = pluginRepository.findOne(1);
		assertNotNull(plugin);
		assertEquals(1, plugin.getId());
		assertEquals("Tells the weather for a given time and location", plugin.getDescription());
		assertEquals("WeatherApp", plugin.getName());
		assertEquals(1, plugin.getAuthor().getId());

	}

	@Test
	public void testAddPlugin() {
		Plugin newPlugin = createPlugin();
		pluginRepository.save(newPlugin);

		int pluginID = newPlugin.getId();
		Plugin plugin = pluginRepository.findOne(pluginID);

		assertNotNull(plugin);
		assertEquals(pluginID, plugin.getId());
		assertEquals("Test Description", plugin.getDescription());
		assertEquals("Testname", plugin.getName());

	}

	private Plugin createPlugin() {
		Plugin newPlugin = new Plugin();
		Author author = new Author();
		author.setName("TestAuthor");
		author.setEmail("testAUthor@mail.ch");

		newPlugin.setAuthor(author);
		newPlugin.setDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
		newPlugin.setDescription("Test Description");
		newPlugin.setName("Testname");
		newPlugin.setUuid(UUID.randomUUID().toString());
		return newPlugin;
	}

	@Test
	public void testRemovePlugin() {
		pluginRepository.delete(2);

		assertNull(pluginRepository.findOne(2));
		assertNotNull(authorRepository.findOne(1));
	}

}
