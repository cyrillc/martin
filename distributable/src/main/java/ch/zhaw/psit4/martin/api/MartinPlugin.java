package ch.zhaw.psit4.martin.api;
/**
 * Distributed plugin interface that handles communication.
 * It is used by application plugin library to call
 * and communicate with plugins.
 *
 * @version 0.0.1-SNAPSHOT
 */
public interface MartinPlugin {
    
    /**
     * Initializes the plugin with the MArtIn context for API access.
     * @param context The MArtIn context of type {@link IMartinContext}
     * @param feature The feature designation in Form of a String
     * @param requestID The unique ID of the request
     */
    void init(IMartinContext context, String feature, long requestID);
}