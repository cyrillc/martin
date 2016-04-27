package ch.zhaw.psit4.martin.testplugin;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ch.zhaw.psit4.martin.api.validation.MartinAPITestResult;
import ch.zhaw.psit4.martin.api.validation.MartinPluginValidator;

public class TestPluginTest {
    
    MartinPluginValidator<TestPlugin> pluginValidator;

    @Before
    public void setUp() {
        pluginValidator = new MartinPluginValidator<TestPlugin>(new TestPlugin());
    }
    
    @Test
    public void validate() {
        MartinAPITestResult result = pluginValidator.runTests();
        assertEquals(MartinAPITestResult.OK, result);
    }

}
