package ch.zhaw.psit4.martin.api.validation;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.zhaw.psit4.martin.api.Feature;
import ch.zhaw.psit4.martin.api.MartinPlugin;
import ch.zhaw.psit4.martin.api.types.IMartinType;

/**
 * Tests a possible implementation of {@link Feature}.
 * 
 * The passed instance will be tested for the ability to be instanced as {@link Feature},
 * for execution without exceptions and return value.
 * 
 * @param <Type> The type of the tested object.
 * @version 0.0.1-SNAPSHOT
 */
public class FeatureValidator<Type> {
    
    private static final Log LOG = LogFactory.getLog(FeatureValidator.class);
    private Class<Type> className;
    private Type instance;
    private Map<String, IMartinType> args;

    @SuppressWarnings("unchecked")
    public FeatureValidator(Type type) {
        this.instance = type;
        this.className = (Class<Type>) type.getClass();
    }
    
    /**
     * Runs all tests on the instance.
     * 
     * @return {@link MartinAPITestResult}
     */
    public MartinAPITestResult runTests() {
        boolean result = isMartinPlugin();
        if (!result) {
            LOG.error(className.toString() + " cannot be instanced to " + Feature.class.toString()
                    + ".");
            return MartinAPITestResult.ERROR;
        }
        result = throwsException();
        if(result)
            return MartinAPITestResult.ERROR;
        result = returnsMessage();
        if(!result) {
            LOG.warn(className.toString() + " does not return a message.");
            return MartinAPITestResult.WARNING;
        }
        
        return MartinAPITestResult.OK;
    }
    
    /**
     * Checks if the passed object is an instance of {@link MartinPlugin}.
     * 
     * @return true or false;
     */
    private boolean isMartinPlugin() {
        return instance instanceof Feature;
    }
    
    /**
     * Checks if an exception is thrown while executing plugin.
     * @return true or false
     */
    private boolean throwsException() {
        boolean retVal = false;
        try {
            ((Feature) instance).start(args);
        } catch (Exception e) {
            retVal = true;
            LOG.error("Exception thrown at start.", e);
        }
        try {
            ((Feature) instance).run();
        } catch (Exception e) {
            retVal = true;
            LOG.error("Exception thrown at run.", e);
        }
        try {
            ((Feature) instance).stop();
        } catch (Exception e) {
            retVal = true;
            LOG.error("Exception thrown at stop.", e);
        }
        
        return retVal;
    }
    
    /**
     * Checks if a message is returned after executing plugin.
     * @return true or false
     */
    private boolean returnsMessage() {
        String message = null;
        try {
            ((Feature) instance).start(args);
            ((Feature) instance).run();
            message = ((Feature) instance).stop();
        } catch (Exception e) {
            LOG.error("Exception thrown at test: returnsMessage.", e);
        }
        
        return (message != null) && !message.isEmpty();
    }
    
    public void setExpectedArguments(Map<String, IMartinType> args) {
        this.args = args;
    }
    
}