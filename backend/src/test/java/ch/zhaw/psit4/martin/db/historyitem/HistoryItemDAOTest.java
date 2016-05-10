package ch.zhaw.psit4.martin.db.historyitem;

import static org.junit.Assert.*;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.zhaw.psit4.martin.common.LiquibaseTestFramework;
import ch.zhaw.psit4.martin.models.HistoryItem;
import ch.zhaw.psit4.martin.models.Request;
import ch.zhaw.psit4.martin.models.Response;
import ch.zhaw.psit4.martin.models.repositories.HistoryItemRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:Beans.xml", "classpath:Beans-unit-tests.xml" })
public class HistoryItemDAOTest {

	/**
	 * Is used to setup the unit-test environment (setup in-memory-database and
	 * import database schema).
	 */
	@Autowired
	private LiquibaseTestFramework liquibase;

	/**
	 * The class to test.
	 */
	@Autowired
	private HistoryItemRepository historyItemRepository;

	@Before
	public void setUp() {
		liquibase.createDatabase("database/unit-tests/HistoryTest/db.changeset-test.xml");
	}

	@Test
	@Transactional
	public void aHistoryItemCanBeSavedInDB() throws Exception {

		Request request = new Request("test");
		Response response = new Response("test");
		HistoryItem historyItem = new HistoryItem(request, response);
		this.historyItemRepository.save(historyItem);

		assertEquals(3, this.historyItemRepository.findAll().size());
	}

	@Test
	@Transactional
	public void canGetAListOfAllHistoryItems() throws Exception {
		assertEquals(this.historyItemRepository.findAll().size(), 2);
	}
}
