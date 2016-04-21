/**
 * 
 */
package ch.zhaw.psit4.martin.pluginlib;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.io.IOUtils;
import org.java.plugin.ObjectFactory;
import org.java.plugin.PluginManager;
import org.java.plugin.PluginManager.PluginLocation;
import org.java.plugin.boot.DefaultPluginsCollector;
import org.java.plugin.util.ExtendedProperties;
import org.json.JSONArray;
import org.json.JSONObject;

import ch.zhaw.psit4.martin.api.IMartinContext;

/**
 * Entry point for the module library to bootstrap jpf and invoke <code>PluginLibrary.start()</code>
 * .
 *
 * @version 0.0.1-SNAPSHOT
 */
public class PluginLibraryBootstrap {
    /**
     * Path to folder where plugins reside (either zipped, or unpacked as a simple folder)
     */
    private static final String PLUGINS_FOLDER = "plugins";
    /**
     * The plugin configuration file
     */
    private static final String PLUGINS_CONFIG = "library.cfg.json";
    /**
     * Boots up the module library
     */
    private static final Log LOG = LogFactory.getLog(PluginLibraryBootstrap.class);
    /**
     * The plugin Manager from org.java.plugin
     */
    private final PluginManager manager;
    /**
     * The plugin Collector from org.java.plugin.boot
     */
    private final DefaultPluginsCollector collector;
    /**
     * The ExtendedProperties object from org.java.plugin.util
     */
    private final ExtendedProperties props;

    public PluginLibraryBootstrap() {
        // instantiate necessary objects
        manager = ObjectFactory.newInstance().createManager();
        collector = new DefaultPluginsCollector();
        props = new ExtendedProperties();
    }

    /**
     * Boots the plugin library by calling the plugin api and returns it once booted.
     * 
     * @return The booted plugin Library
     */
    public IPluginLibrary boot() {
        // get the plugin folder
        File file = getPluginFolder();

        // prepare configuration
        props.setProperty("org.java.plugin.boot.pluginsRepositories", file.getPath());

        // try to initialize the library
        IPluginLibrary lib;
        try {
            lib = initializeLibrary();
            lib.startLibrary();
        } catch (Exception e) {
            LOG.error("Plugin library could not be initialized, empty library returned!", e);
            throw new PluginLibraryNotFoundException(e);
        }

        // finally return library
        return lib;
    }

    /**
     * Gathers paths from config and finds the plugin folder.
     * 
     * @return The plugin folder.
     */
    private File getPluginFolder() {
        File out = null;
        JSONObject libConfig;
        try {
            // load library config json
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(PLUGINS_CONFIG).getFile());
            InputStream is = new FileInputStream(file);
            libConfig = new JSONObject(IOUtils.toString(is));
            is.close();
        } catch (IOException e) {
            LOG.error("Missing " + PLUGINS_CONFIG + ", empty library returned!", e);
            throw new PluginLibraryNotFoundException(e);
        }

        // search the configured paths
        JSONArray paths = libConfig.getJSONArray("paths");
        for (int i = 0; i < paths.length(); i++) {
            try {
                out = findFolder(new File(paths.get(i).toString()).getCanonicalPath(),
                        PLUGINS_FOLDER);
                
                // if a folder was found, return
                if (out != null)
                    return out;
            } catch (IOException e) {
                LOG.warn("Path " + paths.get(i).toString() + " could not be accessed, skipped.", e);
                continue;
            }
        }
        return out;
    }

    /**
     * Find a sub-folder recursively in a given file path.
     * 
     * @param source The source path to search.
     * @param folder The folder name to search.
     * @return The found folder or null if no folder was found.
     */
    private File findFolder(String source, String folder) {
        File out = null;
        String[] subFolders = (new File(source)).list();
        for (String name : subFolders) {
            // file is not a directory -> skip
            if (!(new File(source + File.separatorChar + name)).isDirectory())
                continue;

            // check path and recursively call this method if dir was not found
            if (!(source + File.separatorChar + name)
                    .equals(source + File.separatorChar + folder)) {
                out = findFolder(source + File.separatorChar + name, folder);
                if (out != null)
                    return out;
            } else {
                return new File(source + File.separatorChar + folder);
            }

        }
        return out;
    }

    /**
     * Initializes the library by configuring the Plugincollector and querrying the PluginManager
     * 
     * @return The plugin library interface
     * @throws Exception
     */
    private IPluginLibrary initializeLibrary() throws Exception {
        collector.configure(props);
        // examine plugins repository for plugins
        manager.publishPlugins(collector.collectPluginLocations().toArray(new PluginLocation[] {}));

        // finally retrieve the core plugin and start it up
        return (IPluginLibrary) manager.getPlugin(IMartinContext.CORE_PLUGIN_ID);
    }
}
