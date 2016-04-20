package ch.zhaw.psit4.martin.pluginlib;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import ch.zhaw.psit4.martin.api.PluginService;
import ch.zhaw.psit4.martin.pluginlib.db.ExampleCall;
import ch.zhaw.psit4.martin.pluginlib.db.ExampleCallService;
import ch.zhaw.psit4.martin.api.util.Pair;

import ch.zhaw.psit4.martin.common.Call;

import ch.zhaw.psit4.martin.common.ExtendedRequest;
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
     * Path to folder where plugins reside (either zipped, or unpacked as a
     * simple folder)
     */
    public static final String PLUGINS_REPOSITORY = "plugins";
    /**
     * File name of the plugin keywords JSON.
     */
    public static final String PLUGIN_KEYWORDS = "keywords.json";
    /**
     * The maximum number of characters that can be read from JSON file
     */
    public static final int KEYWORDS_JSON_MAXLEN = 2048;
    /*
     * List of all the plugins currently registered
     */
    private Map<String, PluginService> pluginExtentions;
    /*
     * Log from the common logging api
     */
    private static final Log LOG = LogFactory.getLog(PluginLibrary.class);
    
    @Autowired
    private MartinContextAccessor martinContextAccessor;
    
    @Autowired
    private ExampleCallService exampleCallService;

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
        String pluginID = call.getPlugin();
        String featureID = call.getFeature();
        PluginService service = pluginExtentions.get(pluginID);

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
     * Querry all plugins by keyword and return matching pluginIDs.
     * 
     * @param keyword
     *            The keyword to search.
     * @return {@link Pair} of found plugins sorted by probability (highest
     *         first). The first element is the Plugin ID the second is the
     *         feature ID
     */
    public List<Pair<String, String>> queryFunctionsByKeyword(String keyword) {
        return new ArrayList<Pair<String, String>>();
    }

    /**
     * Get a {@link Map} filled with all required parameters for a plugin and
     * the argument types.
     * 
     * @param plugin
     *            The pluginID to querry.
     * @param The
     *            feature designator to querry.
     * @return A {@link Map} of arguments with key = ({@link String}) argument
     *         name and value = ({@link String}) Argument type (from
     *         {@link ch.zhaw.psit4.martin.api.types})
     */
    public Map<String, String> queryFunctionArguments(String plugin,
            String feature) {
        return new HashMap<String, String>();
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

    public Map<String, PluginService> getPluginExtentions() {
        return pluginExtentions;
    }

    public void setPluginExtentions(
            Map<String, PluginService> pluginExtentions) {
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
    protected Map<String, PluginService> fetchPlugins(final String extPointId) {

        Map<String, PluginService> plugins = new HashMap<String, PluginService>();
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
                JSONObject jsonKeywords = new JSONObject(readUrl(keywordsUrl));

                // plugin loading
                Class<PluginService> pluginClass = (Class<PluginService>) classLoader
                        .loadClass(pluginClassName.valueAsString());
                PluginService pluginInstance = pluginClass.newInstance();
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
     * Reads the content of a File by URL and returns it as a string. Warning:
     * Only {@link KEYWORDS_JSON_MAXLEN} bytes can be read!
     * 
     * @param url
     *            The URL of the file.
     * @return The parsed string.
     * @throws Exception
     *             Exception if the file can't be found or the length exceeds
     *             the limit.
     */
    private String readUrl(URL url) throws Exception {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[KEYWORDS_JSON_MAXLEN];
            while ((read = reader.read(chars)) != -1) {
                if (read == ' ' || read == '\t' || read == '\n' || read == '\r')
                    continue;
                buffer.append(chars, 0, read);
            }
            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
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
}
