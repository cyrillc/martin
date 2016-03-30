package ch.zhaw.psit4.martin.api;

import java.util.Map;

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
     * Initializes the plugin by providing a map of arguments.
     * @param map {@link HashMap} filled with initialization arguments.
     */
    void init(Map<String,String> map);
    
    /**
     * Provides the plugin with the MArtIn context for API access.
     * @param context The MArtIn context of type {@link MartinContext}
     */
    void contextualize(MartinContext context);
}