package ch.zhaw.psit4.martin.api;

/**
 * The MArtIn Context provided to MArtIn Plugins.
 * 
 * This class handles communication of a plugin with the main 
 * application by providing the application with an 
 * object, that MArtIn is aware of.
 *
 * @version 0.0.1-SNAPSHOT
 */
public interface IMartinContext {
    /**
     * Plugin id of the core module, defined in it's plugin.xml class attribute
     */
    public static final String CORE_PLUGIN_ID = "ch.zhaw.psit4.martin.api";
    /*
     * Plugin extention point that is distributed to module programmers
     */
    public static final String EXTPOINT_ID = "MartinPlugin";
    
    
    /**Registers a work item in the context.
     * @param item The item to register.
     */
    public void registerWorkItem(Feature item);
}
