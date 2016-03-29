package ch.zhaw.psit4.martin.modulelib;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.java.plugin.Plugin;
import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;

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
     * Plugin id of the core module, defined in it's plugin.xml class attribute
     */
    public static final String CORE_PLUGIN_ID = "ch.zhaw.psit4.martin.modulelib";
    /*
     * Plugin extention point that is distributed to module programmers
     */
    public static final String EXTPOINT_ID_MENUBAR = "PluginService";

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
    public void start() {
        // Get plugins
        List<PluginService> plugins = new LinkedList<PluginService>();
        try {
            plugins = fetchPlugins(EXTPOINT_ID_MENUBAR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out
                .println("Hello World\n" + plugins.size() + " plugins loaded.");
    }

    /*
     * Fetches all extensions for the given extension point qualifiers.
     * 
     * @param extPointId The extension point id to gather plugins for
     * 
     * @return The gathered plugins in a LinkedList
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> fetchPlugins(final String extPointId) throws Exception {

        final List<T> plugins = new LinkedList<T>();
        final PluginManager manager = this.getManager();

        final ExtensionPoint extPoint = manager.getRegistry()
                .getExtensionPoint(this.getDescriptor().getId(), extPointId);
        for (final Extension extension : extPoint.getConnectedExtensions()) {
            final PluginDescriptor extensionDescriptor = extension
                    .getDeclaringPluginDescriptor();
            manager.activatePlugin(extensionDescriptor.getId());
            final ClassLoader classLoader = manager
                    .getPluginClassLoader(extensionDescriptor);
            final String pluginClassName = extension.getParameter("class")
                    .valueAsString();
            final Class<T> pluginClass = (Class<T>) classLoader
                    .loadClass(pluginClassName);
            final T pluginInstance = pluginClass.newInstance();
            plugins.add(pluginInstance);
        }

        return Collections.unmodifiableList(plugins);
    }
}
