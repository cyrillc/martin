package ch.zhaw.psit4.martin.api;
/**
 * Distributed plugin interface that handles communication.
 * It is used by application plugin library to call
 * and communicate with plugins.
 *
 * @author Daniel Fabian
 * @version 0.0.1-SNAPSHOT
 */
public interface PluginService {
    
    /**
     * Initializes the plugin with the MArtIn context for API access.
     * @param context The MArtIn context of type {@link IMartinContext}
     */
    void init(IMartinContext context);
}