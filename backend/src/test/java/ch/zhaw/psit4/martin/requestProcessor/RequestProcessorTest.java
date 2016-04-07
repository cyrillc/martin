package ch.zhaw.psit4.martin.requestProcessor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.zhaw.psit4.martin.api.util.Pair;
import ch.zhaw.psit4.martin.common.ExtendedRequest;
import ch.zhaw.psit4.martin.common.Request;
import ch.zhaw.psit4.martin.pluginlib.PluginLibrary;

public class RequestProcessorTest {
	private PluginLibrary library;

	@Before
	public void setUp() {
		// Create Mock for plugin library
		library = Mockito.mock(PluginLibrary.class);
		
		// Results for function queries
		List<Pair<String, String>> result0 = new ArrayList<Pair<String, String>>();
		result0.add(new Pair<String, String>("Weather App", "forecast"));
		result0.add(new Pair<String, String>("Weather App", "weather"));

		List<Pair<String, String>> result1 = new ArrayList<Pair<String, String>>();
		result1.add(new Pair<String, String>("HelloWorld", "hello"));
		
		List<Pair<String, String>> result2 = new ArrayList<Pair<String, String>>();
		result2.add(new Pair<String, String>("Weather App", "forecast"));
		
		List<Pair<String, String>> result3 = new ArrayList<Pair<String, String>>();
		
		when(library.queryFunctionsByKeyword(any(String.class))).thenReturn(result3);
		when(library.queryFunctionsByKeyword("weather")).thenReturn(result0);
		when(library.queryFunctionsByKeyword("hello")).thenReturn(result1);
		when(library.queryFunctionsByKeyword("forecast")).thenReturn(result2);
		
		
		// Results for function queries
		Map<String, String> functionResult0 = new HashMap<String, String>();
		functionResult0.put("location", "Text");
		
	
		Map<String, String> functionResult1 = new HashMap<String, String>();
		functionResult1.put("location", "Text");
		functionResult1.put("time", "Text");
		
	
		Map<String, String> functionResult2 = new HashMap<String, String>();
		
		
		when(library.queryFunctionArguments("Weather App", "weather")).thenReturn(functionResult0);
		when(library.queryFunctionArguments("Weather App", "forecast")).thenReturn(functionResult1);
		when(library.queryFunctionArguments("HelloWorld", "hello")).thenReturn(functionResult2);
	}

	@Test
	public void testExtendRequestPluginAndFeature() throws Exception {
		RequestProcessor requestProcessor = new RequestProcessor();
		requestProcessor.setLibrary(library);
		
		
		Request request0 = new Request("Weather for time tomorrow location Zürich");
		ExtendedRequest extRequest0 = requestProcessor.extend(request0);
		assertEquals(extRequest0.getCalls().isEmpty(), false);
		assertEquals(extRequest0.getCalls().get(0).getPlugin(), "Weather App");
		assertEquals(extRequest0.getCalls().get(0).getFeature(), "forecast");
		assertEquals(extRequest0.getCalls().get(0).getArguments().get("time").toString(), (new JSONObject()).put("value", "tomorrow").toString());
		assertEquals(extRequest0.getCalls().get(0).getArguments().get("location").toString(), (new JSONObject()).put("value", "zürich").toString());
		
		
		Request request1 = new Request("Hello world!");
		ExtendedRequest extRequest1 = requestProcessor.extend(request1);
		assertEquals(extRequest1.getCalls().isEmpty(), false);
		assertEquals(extRequest1.getCalls().get(0).getPlugin(), "HelloWorld");
		assertEquals(extRequest1.getCalls().get(0).getFeature(), "hello");
		assertEquals(extRequest1.getCalls().get(0).getArguments().values().size(), 0);
		
		
		
		Request request2 = new Request("Hello, I'd like to have the weather forecast for time 2pm location Chur");
		ExtendedRequest extRequest2 = requestProcessor.extend(request2);
		assertEquals(extRequest2.getCalls().isEmpty(), false);
		assertEquals(extRequest2.getCalls().get(0).getPlugin(), "Weather App");
		assertEquals(extRequest2.getCalls().get(0).getFeature(), "forecast");
		assertEquals(extRequest2.getCalls().get(0).getArguments().get("time").toString(), (new JSONObject()).put("value", "2pm").toString());
		assertEquals(extRequest2.getCalls().get(0).getArguments().get("location").toString(), (new JSONObject()).put("value", "chur").toString());
		
		
		
		Request request3 = new Request("Can you tell me the next president of the united states?");
		try {
			ExtendedRequest extRequest3 = requestProcessor.extend(request3);
		    fail("Method didn't throw when I expected it to");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "No module found for this command.");
		}
		
	}
	
}
