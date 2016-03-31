/**
 * 
 */
package ch.zhaw.psit4.martin.modulelib;

import org.java.plugin.ObjectFactory;
import org.java.plugin.PluginManager;
import org.java.plugin.PluginManager.PluginLocation;
import org.java.plugin.boot.DefaultPluginsCollector;
import org.java.plugin.util.ExtendedProperties;

import ch.zhaw.psit4.martin.api.IMartinContext;

/**
 * Entry point for the module library to bootstrap jpf and invoke
 * <code>PluginLibrary.start()</code>.
 *
 * @author Daniel Fabian
 * @version 0.0.1-SNAPSHOT
 */
public class PluginLibraryBootstrap {
    /**
     * Boots up the module library
     */
    public IPluginLibrary boot() {
        // instantiate necessary objects
        final PluginManager manager = ObjectFactory.newInstance()
                .createManager();
        final DefaultPluginsCollector collector = new DefaultPluginsCollector();
        final ExtendedProperties props = new ExtendedProperties();

        // prepare configuration
        props.setProperty("org.java.plugin.boot.pluginsRepositories",
                PluginLibrary.PLUGINS_REPOSITORY);

        // try to initialize the library
        IPluginLibrary lib;
        try {
            collector.configure(props);
            // examine plugins repository for plugins
            manager.publishPlugins(collector.collectPluginLocations()
                    .toArray(new PluginLocation[] {}));

            // finally retrieve the core plugin and start it up
            lib = (IPluginLibrary) manager
                    .getPlugin(IMartinContext.CORE_PLUGIN_ID);
            lib.startLibrary();
        } catch (Exception e) {
            e.printStackTrace();
            lib = null;
        }
        return lib;
    }
}
