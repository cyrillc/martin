package ch.zhaw.psit4.martin.pluginlib;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.java.plugin.Plugin;
import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.java.plugin.registry.Extension.Parameter;

import ch.zhaw.psit4.martin.api.Feature;
import ch.zhaw.psit4.martin.api.IMartinContext;
import ch.zhaw.psit4.martin.api.MartinPlugin;
import ch.zhaw.psit4.martin.pluginlib.db.ExampleCall;
import ch.zhaw.psit4.martin.pluginlib.db.ExampleCallService;

import ch.zhaw.psit4.martin.common.Call;

import ch.zhaw.psit4.martin.common.ExtendedRequest;
import ch.zhaw.psit4.martin.common.PluginInformation;
import ch.zhaw.psit4.martin.common.Response;

/**
 * PluginLibrary logic entry point.
 * 
 * This class handles Plugin-communication and discovery.
 *
 * @version 0.0.1-SNAPSHOT
 */
public class PluginLibrary extends Plugin implements IPluginLibrary {

    /**
     * File name of the plugin keywords JSON.
     */
    public static final String PLUGIN_KEYWORDS = "keywords.json";
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
    private ExampleCallService exampleCallService;

    @Autowired
    private ch.zhaw.psit4.martin.pluginlib.db.plugin.PluginService pluginService;

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
     * Start the module and initialize components
     */
    @Override
    public void startLibrary() {
        // Get plugins
        pluginExtentions = fetchPlugins(IMartinContext.EXTPOINT_ID);
        LOG.info("Plugin library booted, " + pluginExtentions.size()
                + " plugins loaded.");
    }

    /**
     * Answer a request by searching plugin-library for function and executing
     * them.
     * 
     * @param req
     *            The {@link ExtendedQequest} to answer.
     * 
     * @return The generated {@link Response}.
     */
    @Override
    public Response executeRequest(ExtendedRequest req) {
        Call call = req.getCalls().get(0);
        String pluginID = ((Integer)call.getPlugin().getId()).toString();
        String featureID = ((Integer)call.getFeature().getId()).toString();
        MartinPlugin service = pluginExtentions.get(pluginID);

        // if service exists, execute call
        if (service != null) {
            service.init(martinContextAccessor, featureID, 0);

            Response response = new Response(executeCall(call, 0));
            return response;
        } else {
            LOG.error("Could not find a plugin that matches request call.");
            return new Response("ERROR: no plugin found!");
        }
    }


    /**
     * Returns a list of example calls read from the plugin database. Is usually
     * only called from the AI controller when the user first loads the MArtIn
     * frontend.
     * 
     * @return a list of example calls
     */
    @Override
    public List<ExampleCall> getExampleCalls() {
        return exampleCallService.listExampleCalls();
    }

    @Override
    public List<ExampleCall> getRandomExampleCalls() {
        return exampleCallService.getRandomExcampleCalls();
    }

    public Map<String, MartinPlugin> getPluginExtentions() {
        return pluginExtentions;
    }

    public void setPluginExtentions(
            Map<String, MartinPlugin> pluginExtentions) {
        this.pluginExtentions = pluginExtentions;
    }

    /*
     * Fetches all extensions for the given extension point qualifiers.
     * 
     * @param extPointId The extension point id to gather plugins for
     * 
     * @return The gathered plugins in a LinkedList
     */
    @SuppressWarnings({ "unchecked", "unused" })
    protected Map<String, MartinPlugin> fetchPlugins(final String extPointId) {

        Map<String, MartinPlugin> plugins = new HashMap<String, MartinPlugin>();
        PluginManager manager = this.getManager();

        ExtensionPoint extPoint = manager.getRegistry()
                .getExtensionPoint(this.getDescriptor().getId(), extPointId);

        // iterate through found plugins
        for (Extension extension : extPoint.getConnectedExtensions()) {
            try {
                PluginDescriptor extensionDescriptor = extension
                        .getDeclaringPluginDescriptor();
                manager.activatePlugin(extensionDescriptor.getId());
                ClassLoader classLoader = manager
                        .getPluginClassLoader(extensionDescriptor);

                String id = extension.getExtendedPointId().toString();

                // metadata-parsing (mandatory)
                Parameter pluginClassName = extension.getParameter("class");
                Parameter pluginNAme = extension.getParameter("name");

                // metadata-parsing (optional)
                Parameter pluginDesctibtion = extension
                        .getParameter("description");
                Parameter pluginAuthor = extension.getParameter("author");
                Parameter pluginMail = extension.getParameter("e-mail");
                Parameter pluginHomepage = extension.getParameter("homepage");
                Parameter pluginDate = extension.getParameter("date");

                // keywords JSON loading
                URL keywordsUrl = classLoader.getResource(PLUGIN_KEYWORDS);
                InputStream is = keywordsUrl.openStream();
                JSONObject jsonKeywords = new JSONObject(IOUtils.toString(is));
                is.close();

                // plugin loading
                Class<MartinPlugin> pluginClass = (Class<MartinPlugin>) classLoader
                        .loadClass(pluginClassName.valueAsString());
                MartinPlugin pluginInstance = pluginClass.newInstance();
                plugins.put(id, pluginInstance);
                LOG.info("Plugin \""
                        + extension.getParameter("name").valueAsString()
                        + "\" loaded");

            } catch (Exception e) {
                LOG.error("An Error occured at fetchPlugins: ", e);
            }
        }

        return plugins;
    }

    /**
     * Executes the feature of a call by passing call arguments
     * 
     * @param call
     *            The call to execute features for.
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
                ret += "ERROR at start()! ";
                break;
            }

            try {
                feature.run();
            } catch (Exception e) {
                LOG.error("Could not run plugin feature.", e);
                ret += "ERROR during run()! ";
                break;
            }

            try {
                ret += feature.stop();
            } catch (Exception e) {
                LOG.error("Could not stop plugin feature.", e);
                ret += "ERROR at stop()";
                break;
            }

            ret += "\n";
            feature = martinContextAccessor.fetchWorkItem(requestID);
        }

        return ret;
    }

    @Override
    public List<PluginInformation> getPluginInformation() {
        List<ch.zhaw.psit4.martin.pluginlib.db.plugin.Plugin> pluginList = pluginService
                .listPlugins();
        List<PluginInformation> pluginInformationList = new ArrayList<PluginInformation>();
        for (ch.zhaw.psit4.martin.pluginlib.db.plugin.Plugin plugin : pluginList) {
            pluginInformationList.add(new PluginInformation(plugin.getName(),
                    plugin.getDescription(), plugin.getFunctions()));
        }
        return pluginInformationList;
    }
}
