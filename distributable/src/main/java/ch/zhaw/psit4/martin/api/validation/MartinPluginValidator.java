package ch.zhaw.psit4.martin.api.validation;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.zhaw.psit4.martin.api.Feature;
import ch.zhaw.psit4.martin.api.IMartinContext;
import ch.zhaw.psit4.martin.api.MartinPlugin;

/**
 * Tests a possible implementation of {@link MartinPlugin}.
 * 
 * The passed instance will be tested for the ability to be instanced as {@link MartinPlugin} and
 * for use of the context at initialization.
 * 
 * @param <Type> The type of the tested object.
 * @version 0.0.1-SNAPSHOT
 */
public class MartinPluginValidator<Type> {

    private static final Log LOG = LogFactory.getLog(MartinPluginValidator.class);
    private Class<Type> clazz;
    private Type instance;
    private MartinContextAccessorMock context;

    @SuppressWarnings("unchecked")
    public MartinPluginValidator(Type type) {
        this.instance = type;
        this.clazz = (Class<Type>) type.getClass();
        this.context = new MartinContextAccessorMock();
    }

    /**
     * Runs all tests on the instance.
     * 
     * @return {@link MartinAPITestResult}
     */
    public MartinAPITestResult runTests() {
        boolean result = isMartinPlugin();
        if (!result) {
            LOG.error(clazz.toString() + " cannot be instanced to " + MartinPlugin.class.toString()
                    + ".");
            return MartinAPITestResult.ERROR;
        }
        result = checkRequestIDAssigned();
        if (!result) {
            LOG.warn("No feature objects created by " + MartinPlugin.class.toString() + ".");
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
        return instance instanceof MartinPlugin;
    }

    /**
     * Checks if the {@link MartinPlugin} instance registers any Features in the queue.
     * 
     * @return true or false;
     */
    private boolean checkRequestIDAssigned() {
        boolean assigned = false;
        context.clearWorkList();
        ((MartinPlugin) instance).init(context, "null", 0);
        if (context.getNumberOfFeatures() > 0)
            assigned = true;
        context.clearWorkList();
        return assigned;
    }

    /**
     * The MArtIn context access object mock.
     * 
     * This class mocks the real MArtIn context by providing a simplified implementation to test for
     * functionality.
     *
     * @version 0.0.1-SNAPSHOT
     */
    private class MartinContextAccessorMock implements IMartinContext {
        /*
         * the work list.
         */
        private List<Feature> queue;

        public MartinContextAccessorMock() {
            queue = new LinkedList<Feature>();
        }

        /**
         * Registers a {@link WorkItem} in the context.
         * 
         * @param item The {@link WorkItem} to register.
         */
        @Override
        public void registerWorkItem(Feature item) {
            try {
                queue.add(item);
            } catch (Exception e) {
                LOG.error("An error occured at registerWorkItem()", e);
            }
        }

        /**
         * Clears the work list.
         */
        public void clearWorkList() {
            queue.clear();
        }

        public int getNumberOfFeatures() {
            return queue.size();
        }
    }

}
