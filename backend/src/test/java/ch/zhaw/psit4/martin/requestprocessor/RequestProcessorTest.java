package ch.zhaw.psit4.martin.requestprocessor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.zhaw.psit4.martin.common.ExtendedRequest;
import ch.zhaw.psit4.martin.common.LiquibaseTestFramework;
import ch.zhaw.psit4.martin.db.*;
import ch.zhaw.psit4.martin.requestprocessor.RequestProcessor;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:Beans.xml",
        "classpath:Beans-unit-tests.xml" })
public class RequestProcessorTest {
	@Autowired
	private RequestProcessor requestProcessor;
	
	@Autowired
	private LiquibaseTestFramework liquibase;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		liquibase.createDatabase("database/unit-tests/RequestProcessorTest/db.RequestProcessorTest-1.0.xml");
	}
	
	@Test
	public void testMultipleParameterOfSameType() {
		Request request1 = new Request("Hello Martin, today I met Chuck Norris and and he's such an awsome guy!");
		ExtendedRequest extRequest1 = requestProcessor.extend(request1);
		assertEquals(extRequest1.getCalls().isEmpty(), false);
		assertEquals(extRequest1.getCalls().get(0).getPlugin().getName(), "HelloPlugin");
		assertEquals(extRequest1.getCalls().get(0).getFunction().getName(), "greeting");
		assertEquals(extRequest1.getCalls().get(0).getArguments().values().size(), 2);
		if(extRequest1.getCalls().get(0).getArguments().get("name1").toString().equals("Martin")){
			assertEquals(extRequest1.getCalls().get(0).getArguments().get("name1").toString(), "Martin");
			assertEquals(extRequest1.getCalls().get(0).getArguments().get("name2").toString(), "Chuck Norris");
		} else {
			assertEquals(extRequest1.getCalls().get(0).getArguments().get("name1").toString(), "Chuck Norris");
			assertEquals(extRequest1.getCalls().get(0).getArguments().get("name2").toString(), "Martin");
		}
	}

	@Test
	public void testExtendRequestPluginAndFeature() {
		Request request0 = new Request("Whats the weather tomorrow in Zürich?");
		ExtendedRequest extRequest0 = requestProcessor.extend(request0);
		assertEquals(extRequest0.getCalls().isEmpty(), false);
		assertEquals(extRequest0.getCalls().get(0).getPlugin().getName(), "WetterPlugin");
		assertEquals(extRequest0.getCalls().get(0).getFunction().getName(), "getWeatherAtLocation");
		assertEquals(extRequest0.getCalls().get(0).getArguments().get("time").toString(), "tomorrow");
		assertEquals(extRequest0.getCalls().get(0).getArguments().get("location").toString(), "Zürich");
	}
	
	@Test
	public void testUnknownLocation(){
		Request request2 = new Request("I'd like to know the weather at the Hugentoblerplatz.");
		ExtendedRequest extRequest2 = requestProcessor.extend(request2);
		assertEquals(extRequest2.getCalls().isEmpty(), false);
		assertEquals(extRequest2.getCalls().get(0).getPlugin().getName(), "WetterPlugin");
		assertEquals(extRequest2.getCalls().get(0).getFunction().getName(), "getWeatherAtLocation");
		assertEquals(extRequest2.getCalls().get(0).getArguments().get("location").toString(), "Hugentoblerplatz");
		assertEquals(extRequest2.getCalls().size(), 1);
	}
	
}

