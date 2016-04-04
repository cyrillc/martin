package ch.zhaw.psit4.martin.pluginlib;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
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
import org.java.plugin.registry.Extension.Parameter;

import ch.zhaw.psit4.martin.api.IMartinContext;
import ch.zhaw.psit4.martin.api.PluginService;
import ch.zhaw.psit4.martin.boot.MartinBoot;
import ch.zhaw.psit4.martin.pluginlib.db.ExampleCall;

/**
 * PluginLibrary logic entry point.
 * 
 * This class handles Plugin-communication and discovery.
 *
 * @author Daniel Fabian
 * @version 0.0.1-SNAPSHOT
 */
public class PluginLibrary extends Plugin implements IPluginLibrary {

    /**
     * Path to folder where plugins reside (either zipped, or unpacked as a
     * simple folder)
     */
    public static final String PLUGINS_REPOSITORY = "../plugins";
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
    private List<PluginService> pluginExtentions;
    /*
     * Log from the common logging api
     */
    private Log log;

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
        // Get the log
        log = LogFactory.getLog(PluginLibrary.class);
        // Get plugins
        pluginExtentions = fetchPlugins(IMartinContext.EXTPOINT_ID);
        log.info("Plugin library booted, " + pluginExtentions.size()
                + " plugins loaded.");
    }

    /*
     * Fetches all extensions for the given extension point qualifiers.
     * 
     * @param extPointId The extension point id to gather plugins for
     * 
     * @return The gathered plugins in a LinkedList
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> fetchPlugins(final String extPointId) {

        List<T> plugins = new LinkedList<T>();
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

                // metadata-parsing (mandatory)
                Parameter pluginClassName = extension.getParameter("class");
                Parameter pluginNAme = extension.getParameter("name");
                Parameter pluginArgNAmes = extension.getParameter("arguments");
                Parameter pluginArgTypes = extension
                        .getParameter("argument-types");

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
                Class<T> pluginClass = (Class<T>) classLoader
                        .loadClass(pluginClassName.valueAsString());
                T pluginInstance = pluginClass.newInstance();
                plugins.add(pluginInstance);
                log.info("Plugin \""
                        + extension.getParameter("name").valueAsString()
                        + "\" loaded");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return Collections.unmodifiableList(plugins);
    }

    /**
     * Querry all plugins by keyword and return matching pluginIDs.
     * 
     * @param keyword
     *            The keyword to search.
     * @return {@link ArrayList} of found plugins sorted by probability (highest
     *         first).
     */
    @Override
    public List<String> queryPluginsByKeyword(String keyword) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Get a {@link Map} filled with all required parameters for a plugin and
     * the argument types.
     * 
     * @param pluginID
     *            The pluginID to querry.
     * @return A {@link Map} of arguments with key = ({@link String}) argument
     *         name and value = ({@link String}) Argument type (from
     *         {@link ch.zhaw.psit4.martin.api.types})
     */
    @Override
    public <T> Map<String, String> queryPluginArguments(String pluginID) {
        // TODO Auto-generated method stub
        return null;
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
     * Returns a list of example calls read from the plugin database. Is usually
     * only called from the AI controller when the user first loads the MArtIn
     * frontend.
     * 
     * @return a list of example calls
     */
    public List<ExampleCall> getExampleCalls() {
        List<ExampleCall> exampleCallList = new ArrayList<ExampleCall>();
        exampleCallList = MartinBoot.exampleCallService.listExampleCalls();
        return exampleCallList;
    }
}
