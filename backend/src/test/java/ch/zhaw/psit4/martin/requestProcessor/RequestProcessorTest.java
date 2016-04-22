package ch.zhaw.psit4.martin.requestProcessor;

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
import ch.zhaw.psit4.martin.common.Request;


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
	public void testExtendRequestPluginAndFeature() throws Exception {
	
		Request request0 = new Request("Whats the weather tomorrow in Zürich?");
		ExtendedRequest extRequest0 = requestProcessor.extend(request0);
		assertEquals(extRequest0.getCalls().isEmpty(), false);
		assertEquals(extRequest0.getCalls().get(0).getPlugin().getName(), "WeatherApp");
		assertEquals(extRequest0.getCalls().get(0).getFeature().getName(), "weather");
		//assertEquals(extRequest0.getCalls().get(0).getArguments().get("time").toString(), (new JSONObject()).put("value", "tomorrow").toString());
		//assertEquals(extRequest0.getCalls().get(0).getArguments().get("location").toString(), (new JSONObject()).put("value", "zürich").toString());
		
		
		/*Request request1 = new Request("Hello world!");
		ExtendedRequest extRequest1 = requestProcessor.extend(request1);
		assertEquals(extRequest1.getCalls().isEmpty(), false);
		assertEquals(extRequest1.getCalls().get(0).getPlugin(), "HelloWorld");
		assertEquals(extRequest1.getCalls().get(0).getFeature(), "hello");
		assertEquals(extRequest1.getCalls().get(0).getArguments().values().size(), 0); */
		
		
		
		Request request2 = new Request("Hello, Id like to have the weather forecast for time 2pm location Chur");
		ExtendedRequest extRequest2 = requestProcessor.extend(request2);
		assertEquals(extRequest2.getCalls().isEmpty(), false);
		assertEquals(extRequest2.getCalls().get(0).getPlugin().getName(), "WeatherApp");
		assertEquals(extRequest2.getCalls().get(0).getFeature().getName(), "weather");
		//assertEquals(extRequest2.getCalls().get(0).getArguments().get("time").toString(), (new JSONObject()).put("value", "2pm").toString());
		//assertEquals(extRequest2.getCalls().get(0).getArguments().get("location").toString(), (new JSONObject()).put("value", "chur").toString());
		
		
		
		
		
		/*Request request3 = new Request("Can you tell me the next president of the united states?");
		try {
			@SuppressWarnings("unused")
            ExtendedRequest extRequest3 = requestProcessor.extend(request3);
		    fail("Method didn't throw when I expected it to");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "No module found for this command.");
		} */
		
		Request request4 = new Request("Who will be the president of the United States in 2017?");
		ExtendedRequest extRequest4 = requestProcessor.extend(request4);
		
	}
	
}
