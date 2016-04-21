package ch.zhaw.psit4.martin.pluginlib.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import ch.zhaw.psit4.martin.pluginlib.PluginLibraryBootstrap;

public class PluginFolderAccessor {

    /**
     * Path to folder where plugins reside (either zipped, or unpacked as a simple folder)
     */
    private String folderName;
    /**
     * The plugin configuration file
     */
    private String configFile;
    /**
     * Boots up the module library
     */
    private static final Log LOG = LogFactory.getLog(PluginFolderAccessor.class);

    public PluginFolderAccessor(String folderName, String configFile) {
        this.folderName = folderName;
        this.configFile = configFile;
    }

    /**
     * Gathers paths from config and finds the plugin folder.
     * 
     * @return The plugin folder.
     */
    public File getPluginFolder() {
        File out = getFolderDynamically();
        if (out != null)
            return out;

        // load library config json
        JSONObject libConfig = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(configFile).getFile());
            InputStream is = new FileInputStream(file);
            libConfig = new JSONObject(IOUtils.toString(is));
            is.close();
        } catch (IOException e) {
            LOG.warn("Missing " + configFile + "!", e);
        }

        // search the json registered paths
        if (libConfig != null) {
            out = getFolderFromPaths(libConfig.getJSONArray("paths"));
        } else {
            LOG.error(configFile + " can't be loaded!");
        }

        return out;
    }

    /**
     * Tries to get the plugin folder dynamically
     * 
     * @return The folder if found
     */
    File getFolderDynamically() {
        File out = null;
        // try to get path dynamically
        try {
            String path = PluginLibraryBootstrap.class.getProtectionDomain().getCodeSource()
                    .getLocation().toURI().getPath();
            path += ".." + File.separatorChar + "..";
            out = findFolder(new File(path).getCanonicalPath(), folderName);
        } catch (IOException | URISyntaxException e) {
            LOG.warn("Could not load path dynamically, opt to json paths.", e);
        }
        return out;
    }

    /**
     * Get the plugin folder from an array of paths.
     * 
     * @param paths The paths in a {@link JSONArray} file.
     * @return The found file.
     */
    File getFolderFromPaths(JSONArray paths) {
        File out = null;
        for (int i = 0; i < paths.length(); i++) {
            try {
                out = findFolder(new File(paths.get(i).toString()).getCanonicalPath(), folderName);
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
    File findFolder(String source, String folder) {
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

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getConfigFile() {
        return configFile;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

}
