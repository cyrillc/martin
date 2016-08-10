package ch.zhaw.psit4.martin.testplugin;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ch.zhaw.psit4.martin.api.types.IBaseType;
import ch.zhaw.psit4.martin.api.types.MPerson;
import ch.zhaw.psit4.martin.api.validation.FeatureValidator;
import ch.zhaw.psit4.martin.api.validation.MartinAPITestResult;

public class TestPluginWorkTest {

    FeatureValidator featureValidator;
    
    @Before
    public void setUp() {
        featureValidator = new FeatureValidator(TestPluginWork.class);
        
        Map<String, IBaseType> args = new HashMap<>();
        args.put("name1", new MPerson("bla"));
        featureValidator.setExpectedArguments(args);
    }
    
    @Test
    public void validate() {
        MartinAPITestResult result = featureValidator.runTests();
        assertEquals(MartinAPITestResult.OK, result);
    }

}
