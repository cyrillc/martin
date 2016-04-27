package ch.zhaw.psit4.martin.testplugin;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ch.zhaw.psit4.martin.api.types.IMartinType;
import ch.zhaw.psit4.martin.api.types.Text;
import ch.zhaw.psit4.martin.api.validation.FeatureValidator;
import ch.zhaw.psit4.martin.api.validation.MartinAPITestResult;

public class TestPluginWorkTest {

    FeatureValidator<TestPluginWork> featureValidator;
    
    @Before
    public void setUp() {
        featureValidator = new FeatureValidator<TestPluginWork>(new TestPluginWork(0));
        
        Map<String, IMartinType> args = new HashMap<>();
        args.put("name", new Text("bla"));
        featureValidator.setExpectedArguments(args);
    }
    
    @Test
    public void validate() {
        MartinAPITestResult result = featureValidator.runTests();
        assertEquals(MartinAPITestResult.OK, result);
    }

}
