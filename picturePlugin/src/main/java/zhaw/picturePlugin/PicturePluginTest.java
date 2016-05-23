package zhaw.picturePlugin;

import ch.zhaw.psit4.martin.api.validation.MartinAPITestResult;
import ch.zhaw.psit4.martin.api.validation.MartinPluginValidator;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PicturePluginTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName
     *            name of the test case
     */
    public PicturePluginTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(PicturePluginTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        MartinPluginValidator validator = new MartinPluginValidator(
                new PicturePlugin());
        assertEquals(MartinAPITestResult.OK, validator.runTests());
    }

}
