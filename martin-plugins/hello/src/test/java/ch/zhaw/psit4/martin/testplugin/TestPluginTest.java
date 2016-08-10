package ch.zhaw.psit4.martin.testplugin;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.zhaw.psit4.martin.api.validation.MartinAPITestResult;
import ch.zhaw.psit4.martin.api.validation.MartinPluginValidator;

public class TestPluginTest {
    
    MartinPluginValidator pluginValidator;

    @Before
    public void setUp() {
        pluginValidator = new MartinPluginValidator(TestPlugin.class);
        
        List<String> requests = new ArrayList<>();
        requests.add("name1");
        requests.add("name2");
        pluginValidator.setRequests(requests);
    }
    
    @Test
    public void validate() {
        MartinAPITestResult result = pluginValidator.runTests();
        assertEquals(MartinAPITestResult.OK, result);
    }

}
