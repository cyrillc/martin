package ch.zhaw.psit4.martin.pluginlib.filesystem;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import ch.zhaw.psit4.martin.pluginlib.FolderCreationException;

public class PluginInstaller {

    /**
     * The file ending of a supported MrtinPlugin upload file
     */
    private String supportedType;

    @Autowired
    PluginFolderAccessor pluginFolderAccessor;

    private static final Log LOG = LogFactory.getLog(PluginFolderAccessor.class);

    public PluginInstaller(String supportedType) {
        this.supportedType = supportedType;
    }

    public String installPlugin(String name, MultipartFile plugin) {
        // get the jar
        File jar = null;
        try {
            if (isFileSupported(plugin)) {
                jar = convert(plugin);
            } else {
                LOG.error("File format " + getExtensionName(plugin) + " not supported.");
                return "File format not supported.";
            }
        } catch (IOException e) {
            LOG.error("File upload failed.", e);
            return "File upload plugin failed.";
        }

        // get the plugin folder
        File pluginFolder = pluginFolderAccessor.getPluginFolder();
        if (pluginFolderAccessor == null) {
            LOG.error("Plugin folder not found.");
            return "Plugin folder not found.";
        }

        // try to create folder structure
        try {
            createFolderStructure(pluginFolder, name);
        } catch (FolderCreationException e) {
            LOG.error(e);
            return "Folder structure could not be created.";
        }
        
        install(name, jar);
        LOG.info("Plugin " + name + " installed correctly.");
        return "Plugin installed correctly.";
    }

    String getExtensionName(MultipartFile file) {
        return FilenameUtils.getExtension(file.getOriginalFilename());
    }

    boolean isFileSupported(MultipartFile file) {
        return getExtensionName(file).equals(supportedType);
    }

    File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(convFile));
        FileCopyUtils.copy(file.getInputStream(), stream);
        stream.close();
        return convFile;
    }

    void createFolderStructure(File pluginFolder, String name) throws FolderCreationException {
        String newPluginPath = pluginFolder.getAbsolutePath() + File.pathSeparatorChar + name;
        String newClasses = newPluginPath + File.pathSeparatorChar + "classes";
        String newRes = newPluginPath + File.pathSeparatorChar + "res";
        String newLib = newPluginPath + File.pathSeparatorChar + "lib";
        createFolder(newClasses);
        createFolder(newRes);
        createFolder(newLib);
    }

    void createFolder(String folderPath) throws FolderCreationException {
        File file = new File(folderPath);
        if (!file.isDirectory()) {
            boolean success = file.mkdirs();
            if (!success) {
                throw new FolderCreationException("Could not create path: " + file.getPath());
            }
        }
    }

    void install(String name, File jar) {
        
    }

    public String getSupportedType() {
        return supportedType;
    }

    public void setSupportedType(String supportedType) {
        this.supportedType = supportedType;
    }

}
