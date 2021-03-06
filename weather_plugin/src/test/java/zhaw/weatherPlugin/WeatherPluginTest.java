package zhaw.weatherPlugin;

import java.util.ArrayList;
import java.util.List;

import ch.zhaw.psit4.martin.api.validation.MartinAPITestResult;
import ch.zhaw.psit4.martin.api.validation.MartinPluginValidator;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class WeatherPluginTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public WeatherPluginTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(WeatherPluginTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
        MartinPluginValidator validator = new MartinPluginValidator(new WeatherPlugin());
        List<String> requests = new ArrayList<>();
        requests.add("weather");
        validator.setRequests(requests);
        
        assertEquals(MartinAPITestResult.OK, validator.runTests());
    }
}
