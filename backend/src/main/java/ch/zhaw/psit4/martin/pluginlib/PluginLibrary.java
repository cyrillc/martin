package ch.zhaw.psit4.martin.pluginlib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.java.plugin.Plugin;
import org.java.plugin.PluginLifecycleException;
import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;
import org.springframework.beans.factory.annotation.Autowired;

import org.java.plugin.registry.Extension.Parameter;

import ch.zhaw.psit4.martin.api.Feature;
import ch.zhaw.psit4.martin.api.IMartinContext;
import ch.zhaw.psit4.martin.api.MartinPlugin;
import ch.zhaw.psit4.martin.common.Call;

import ch.zhaw.psit4.martin.common.ExtendedRequest;
import ch.zhaw.psit4.martin.common.PluginInformation;
import ch.zhaw.psit4.martin.models.*;
import ch.zhaw.psit4.martin.models.repositories.ExampleCallRepository;
import ch.zhaw.psit4.martin.models.repositories.PluginRepository;
import ch.zhaw.psit4.martin.pluginlib.filesystem.KeywordsJSONMissingException;
import ch.zhaw.psit4.martin.pluginlib.filesystem.PluginDataAccessor;

/**
 * PluginLibrary logic entry point.
 * 
 * This class handles Plugin-communication and discovery.
 *
 * @version 0.0.1-SNAPSHOT
 */
public class PluginLibrary extends Plugin implements IPluginLibrary {
    /*
     * List of all the plugins currently registered
     */
    private Map<String, MartinPlugin> pluginExtentions;
    /*
     * Log from the common logging api
     */
    private static final Log LOG = LogFactory.getLog(PluginLibrary.class);

    @Autowired
    private MartinContextAccessor martinContextAccessor;
    
    @Autowired
    private PluginDataAccessor pluginDataAccessor;
    
    @Autowired
    private PluginRepository pluginRepository;

    @Autowired
    private ExampleCallRepository exampleCallRepository;


    /*
     * (non-Javadoc)
     * 
     * @see org.java.plugin.Plugin#doStart()
     */
    @Override
    protected void doStart() throws Exception {
        // nothing to do here
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.java.plugin.Plugin#doStop()
     */
    @Override
    protected void doStop() throws Exception {
        // nothing to do here
    }

    /*
     * Start the module and initialize components.
     * 
     * It will start after all beans have been initialized to prevent crashes during bean
     * initialization and for looser dependencies.
     */
    @PostConstruct
    @Override
    public void startLibrary() {
        // Get plugins
        pluginExtentions = fetchPlugins(IMartinContext.EXTPOINT_ID);
        LOG.info("Plugin library booted, " + pluginExtentions.size() + " plugins loaded.");
    }

    /*
     * Fetches all extensions for the given extension point qualifiers.
     * 
     * @param extPointId The extension point id to gather plugins for
     * 
     * @return The gathered plugins in a LinkedList
     */
    protected Map<String, MartinPlugin> fetchPlugins(final String extPointId) {

        Map<String, MartinPlugin> plugins = new HashMap<>();
        PluginManager manager = this.getManager();

        ExtensionPoint extPoint =
                manager.getRegistry().getExtensionPoint(this.getDescriptor().getId(), extPointId);

        // iterate through found plugins
        for (Extension extension : extPoint.getConnectedExtensions()) {
            PluginDescriptor extensionDescriptor = extension.getDeclaringPluginDescriptor();
            try {
                manager.activatePlugin(extensionDescriptor.getId());
            } catch (PluginLifecycleException e) {
                LOG.error("An Error occured while activating plugin.", e);
                continue;
            }
            ClassLoader classLoader = manager.getPluginClassLoader(extensionDescriptor);

            // parameter access + storage
            Parameter pluginClassName = extension.getParameter("class");
            String uuid = extension.getId();
            
            // plugin loading
            MartinPlugin pluginInstance = loadPlugin(classLoader, pluginClassName);
            if (pluginInstance == null)
                continue;
            
            // update DB and memory
            /**try {
                pluginDataAccessor.savePluginInDB(extension, classLoader);
                plugins.put(uuid, pluginInstance);
            } catch (KeywordsJSONMissingException e) {
                LOG.warn("Plugin could not be loaded.", e);
            } **/
        }

        return plugins;
    }
    
    /**
     * Loads a plugin via framework and returns the {@link MartinPlugin} interface
     * 
     * @param classLoader The framework classloader singleton.
     * @param pluginClassName The java name of the class to load.
     * @return The loaded class or null, if an error occurred
     */
    @SuppressWarnings("unchecked")
    public MartinPlugin loadPlugin(ClassLoader classLoader, Parameter pluginClassName) {
        MartinPlugin pluginInstance = null;
        Class<MartinPlugin> pluginClass = null;
        try {
            pluginClass =
                    (Class<MartinPlugin>) classLoader.loadClass(pluginClassName.valueAsString());
            pluginInstance = pluginClass.newInstance();
            LOG.info("Plugin \"" + pluginClassName.valueAsString() + "\" loaded");
        } catch (ClassNotFoundException e) {
            LOG.error("Plugin class could not be found.", e);
        } catch (InstantiationException | ClassCastException e) {
            LOG.error("Plugin class could not be instanced to \"MartinPlugin\".", e);
        } catch (IllegalAccessException e) {
            LOG.error("Plugin class could not be accessed.", e);
        }
        return pluginInstance;
    }

    /**
     * Answer a request by searching plugin-library for function and executing them.
     * 
     * @param req The {@link ExtendedQequest} to answer.
     * 
     * @return The generated {@link Response}.
     */
    @Override
    public Response executeRequest(ExtendedRequest req) {
        Call call = req.getCalls().get(0);
        String pluginID = call.getPlugin().getUuid();
        String functionName = call.getFunction().getName();
        MartinPlugin service = pluginExtentions.get(pluginID);

        // if service exists, execute call
        
        if (service != null) {
            service.init(martinContextAccessor, functionName, 0);

            return new Response(executeCall(call, 0));
        } else {
            LOG.error("Could not find a plugin that matches request call.");
            return new Response("ERROR: no plugin found!");
        }
    }

    /**
     * Executes the feature of a call by passing call arguments
     * 
     * @param call The call to execute features for.
     * @return The return value as string.
     */
    private String executeCall(Call call, long requestID) {
        Feature feature = martinContextAccessor.fetchWorkItem(requestID);
        String ret = "";
        while (feature != null) {
            try {
                feature.start(call.getArguments());
            } catch (Exception e) {
                LOG.error("Could not start plugin feature.", e);
                ret = "I'm sorry, I can not understand you.";
                break;
            }

            try {
                feature.run();
            } catch (Exception e) {
                LOG.error("Could not run plugin feature.", e);
                ret = "I'm sorry, I can not understand you.";
                break;
            }

            try {
                String stopValue = feature.stop();
                if(stopValue == null || stopValue.isEmpty()) {
                    ret = "I'm sorry, I can not understand you.";
                } else {
                    ret = stopValue;
                }
            } catch (Exception e) {
                LOG.error("Could not stop plugin feature.", e);
                ret = "I'm sorry, I can not understand you.";
                break;
            }

            ret += "\n";
            feature = martinContextAccessor.fetchWorkItem(requestID);
        }

        return ret;
    }

    @Override
    public List<PluginInformation> getPluginInformation() {
    	
    	
    	
        List<ch.zhaw.psit4.martin.models.Plugin> pluginList = pluginRepository.getAll();
        List<PluginInformation> pluginInformationList = new ArrayList<PluginInformation>();
        for (ch.zhaw.psit4.martin.models.Plugin plugin : pluginList) {
            pluginInformationList.add(new PluginInformation(plugin.getName(),
                    plugin.getDescription(), plugin.getFunctions()));
        }
        return pluginInformationList;
    }

    /**
     * Returns a list of example calls read from the plugin database. Is usually only called from
     * the AI controller when the user first loads the MArtIn frontend.
     * 
     * @return a list of example calls
     */
    @Override
    public List<ExampleCall> getExampleCalls() {
        return exampleCallRepository.getAll();
        
    }

    @Override
    public List<ExampleCall> getRandomExampleCalls() {
        return exampleCallRepository.getRandomCalls();
    }

    public Map<String, MartinPlugin> getPluginExtentions() {
        return pluginExtentions;
    }

    public void setPluginExtentions(Map<String, MartinPlugin> pluginExtentions) {
        this.pluginExtentions = pluginExtentions;
    }
}
