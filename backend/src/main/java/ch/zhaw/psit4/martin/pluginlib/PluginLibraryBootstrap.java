/**
 * 
 */
package ch.zhaw.psit4.martin.pluginlib;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.weaver.Iterators;
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
 * @version 0.0.1-SNAPSHOT
 */
public class PluginLibraryBootstrap {
    /**
     * Boots up the module library
     */
    private static final Log LOG = LogFactory
            .getLog(PluginLibraryBootstrap.class);

    public IPluginLibrary boot() {
        // instantiate necessary objects
        final PluginManager manager = ObjectFactory.newInstance()
                .createManager();
        final DefaultPluginsCollector collector = new DefaultPluginsCollector();
        final ExtendedProperties props = new ExtendedProperties();
        
        
        File file = new File(".");
        for(String path : PluginLibrary.PLUGINS_REPOSITORY){
        	if((new File(path)).isDirectory()){
        		file = new File(path);
        	}
        }
     
       
       
        // prepare configuration
        props.setProperty("org.java.plugin.boot.pluginsRepositories",file.getPath());

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
            LOG.error("An error occured at boot()", e);
            lib = null;
        }
        return lib;
    }
}
