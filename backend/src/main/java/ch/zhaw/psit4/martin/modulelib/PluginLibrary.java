package ch.zhaw.psit4.martin.modulelib;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.java.plugin.Plugin;
import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.Extension.Parameter;

import ch.zhaw.psit4.martin.api.MartinContext;
import ch.zhaw.psit4.martin.api.PluginService;

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
     * Start the module and initially gather all plugins.
     */
    public void startLibrary() {
        // Get the log
        log = LogFactory.getLog(PluginLibrary.class);
        // Get plugins
        pluginExtentions = fetchPlugins(MartinContext.EXTPOINT_ID);
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
    public <T> List<T> fetchPlugins(final String extPointId) {

        List<T> plugins = new LinkedList<T>();
        PluginManager manager = this.getManager();

        ExtensionPoint extPoint = manager.getRegistry()
                .getExtensionPoint(this.getDescriptor().getId(), extPointId);
        for (Extension extension : extPoint.getConnectedExtensions()) {
            try {
                PluginDescriptor extensionDescriptor = extension
                        .getDeclaringPluginDescriptor();
                manager.activatePlugin(extensionDescriptor.getId());
                ClassLoader classLoader = manager
                        .getPluginClassLoader(extensionDescriptor);
                Parameter pluginClassName = extension.getParameter("class");
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
}
