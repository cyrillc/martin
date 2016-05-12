package ch.zhaw.psit4.martin.pluginlib;

import java.util.List;
import java.util.Map;

import org.java.plugin.boot.DefaultPluginsCollector;
import org.java.plugin.registry.Extension.Parameter;

import ch.zhaw.psit4.martin.api.MartinPlugin;
import ch.zhaw.psit4.martin.api.validation.MartinAPITestResult;
import ch.zhaw.psit4.martin.common.ExtendedRequest;
import ch.zhaw.psit4.martin.common.PluginInformation;
import ch.zhaw.psit4.martin.models.*;

/**
 * Interface for class <code>PluginLibrary</code>.
 *
 * @version 0.0.1-SNAPSHOT
 */
public interface IPluginLibrary {
    
    /*
     * Initializes the library
     */
    public void initialize(DefaultPluginsCollector collector);
    
    /*
     * Start the module and initialize components
     */
    public void startLibrary();
    
    /*
     * Fetches all extensions for the given extension point qualifiers.
     * 
     * @param extPointId The extension point id to gather plugins for
     * 
     * @return The gathered plugins in a LinkedList
     */
    public void loadAllPlugins(final String extPointId);
    
    /*
     * Fetches new extensions for the given extension point qualifiers.
     * 
     * @param extPointId The extension point id to gather plugins for
     * 
     * @return A human readable string
     */
    public String loadNewPlugin(final String extPointId);
    
    /**
     * Loads a plugin via framework and returns the {@link MartinPlugin} interface
     * 
     * @param classLoader The framework classloader singleton.
     * @param pluginClassName The java name of the class to load.
     * @return The loaded class or null, if an error occurred
     */
    public MartinPlugin loadPlugin(ClassLoader classLoader, Parameter pluginClassName);
    
    
    /**
     * Checks a plugin for validity.
     * 
     * @param plugin The plugin to check
     * @param The level to test against, this method will return true if the result is greater or
     *        equal than the testLevel
     * @return true or false
     */
    public boolean isValidPlugin(MartinPlugin plugin, MartinAPITestResult testLevel);

    /**
     * Answer a request by searching plugin-library for function and executing
     * them.
     * 
     * @param req The {@link ExtendedQequest} to answer.
     * 
     * @return The generated {@link MResponse}.
     */
    public MResponse executeRequest(ExtendedRequest req);

    
    public List<PluginInformation> getPluginInformation();
    
    /**
     * Returns a list of example calls read from the plugin database. Is usually
     * only called from the AI controller when the user first loads the MArtIn
     * frontend.
     * 
     * @return a list of example calls
     */
    public List<MExampleCall> getExampleCalls();
    
    /**
     * Returns a list of 5 randomly choosen example calls read from the plugin database. Is usually
     * only called from the AI controller when the user first loads the MArtIn
     * frontend.
     * 
     * @return a list of 5 randomly choosen example calls
     */
    public List<MExampleCall> getRandomExampleCalls();
    
    public Map<String, MartinPlugin> getPluginExtentions();
}
