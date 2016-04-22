package ch.zhaw.psit4.martin.pluginlib.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * This class handles searching for plugins in the file-system of the backend server.
 *
 * @version 0.0.1-SNAPSHOT
 */
public class PluginFolderAccessor {

    private static final String[] paths = {
            "./",
            "../",
            "../../",
            "../../../",
            "/var/lib/jenkins/workspace/MArtIn/"
            };
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
        File out = null;
        // load library config json
        JSONObject libConfig = null;
        try {
            Resource resource = new ClassPathResource(configFile);
            System.out.println("---------------------------------------------" + resource.getFile());
            InputStream resourceInputStream = resource.getInputStream();
            /*
            ClassLoader classLoader = PluginFolderAccessor.class.getClassLoader();
            URL urlPath = classLoader.getResource(configFile);
            if (urlPath == null)
                throw new IOException("URL could not be loaded.");
            String path = urlPath.toString().replace("jar:", "").replace("folder:", "");
            System.out.println(path);
            File file = new File(path);
            InputStream is = new FileInputStream(file);
            */
            libConfig = new JSONObject(IOUtils.toString(resourceInputStream));
            resourceInputStream.close();
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
     * Get the plugin folder from an array of paths.
     * 
     * @param paths The paths in a {@link JSONArray} file.
     * @return The found file.
     */
    File getFolderFromPaths(JSONArray paths) {
        File out = null;
        for (int i = 0; i < paths.length(); i++) {
            try {
                out = checkFolder(new File(paths.get(i).toString()).getCanonicalPath(), folderName);
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
     * Check if the folder is the searched folder.
     * 
     * @param source The source path to search.
     * @param folder The folder name to search.
     * @return The found folder or null if no folder was found.
     */
    File checkFolder(String source, String folder) {
        File out = null;
        String[] sourceParts = source.split("\\\\");
        if(sourceParts[sourceParts.length - 1].equals(folder))
            out = new File(source);
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
