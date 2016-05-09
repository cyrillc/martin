package ch.zhaw.psit4.martin.aiController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.zhaw.psit4.martin.common.Call;
import ch.zhaw.psit4.martin.common.ExtendedRequest;
import ch.zhaw.psit4.martin.common.LiquibaseTestFramework;
import ch.zhaw.psit4.martin.common.Sentence;
import ch.zhaw.psit4.martin.db.*;
import ch.zhaw.psit4.martin.pluginlib.IPluginLibrary;
import ch.zhaw.psit4.martin.requestprocessor.RequestProcessor;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.StanfordCoreNLPClient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:Beans.xml", "classpath:Beans-unit-tests.xml" })
public class AIControllerFacadeTest {

	@Mock
	private HistoryItemService historyItemServiceMock;

	@Mock
	private RequestProcessor requestProcessorMock;

	@Mock
	private IPluginLibrary pluginLibraryMock;

	@InjectMocks
	private AIControllerFacade aiController;

	@Autowired
	private LiquibaseTestFramework liquibase;
	
	@Autowired
	private StanfordCoreNLPClient stanfordNLP;

	Request request = null;
	ExtendedRequest extRequest = null;
	Response response = null;
	HistoryItem historyItem = null;
	Call call = null;

	@Before
	public void setUp() throws Exception {
		liquibase.createDatabase("database/db.changeset-schema-latest.xml");
		MockitoAnnotations.initMocks(this);

		request = new Request("request test");
		extRequest = new ExtendedRequest();
		call = new Call();
		extRequest.addCall(call);
		extRequest.setSentence(new Sentence("test", stanfordNLP));
		response = new Response("response test");
		historyItem = new HistoryItem(request, response);

		when(requestProcessorMock.extend(request)).thenReturn(extRequest);
		when(pluginLibraryMock.executeRequest(extRequest)).thenReturn(response);
		doNothing().when(historyItemServiceMock).addHistoryItem(historyItem);

		ArrayList<HistoryItem> getHistoryResult = new ArrayList<>();
		getHistoryResult.add(new HistoryItem(new Request("command1"), new Response("response1")));
		getHistoryResult.add(new HistoryItem(new Request("command2"), new Response("response2")));
		getHistoryResult.add(new HistoryItem(new Request("command3"), new Response("response3")));
		when(historyItemServiceMock.getHistory()).thenReturn(getHistoryResult);
	}

	@Test
	public void canGetAListOfHistoryItems() {
		List<HistoryItem> list = aiController.getHistory();
		assertEquals(3, list.size());
		assertEquals("command1", list.get(0).getRequest().getCommand());
	}

	@Test
	public void saveAHistoryItemWhenRequestMakeSense() {
		aiController.elaborateRequest(request);
		// The timestamp must be updated to be the "same" as the generated in
		// the elaborateRequestFunction, otherwise the mock will not be the
		// same as the one generated.
		historyItem.setDate(new Timestamp(new Date().getTime()));
		verify(historyItemServiceMock).addHistoryItem(historyItem);
	}

	@Test
	public void checkElaborationOfRequest() {
		Response responseTest = null;

		responseTest = aiController.elaborateRequest(request);
		assertTrue(responseTest.equals(response));
	}

}
