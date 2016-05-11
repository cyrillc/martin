package ch.zhaw.psit4.martin.pluginlib.filesystem;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

public class PluginInstaller {

    private static final String CLASSES_FOLDER = "classes";
    private static final String RESOURCES_FOLDER = "res";
    private static final String LIB_FOLDER = "lib";
    private static final String TEMP_FOLDER = "temp";

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

    public String installPlugin(String pluginName, MultipartFile plugin) {
        // get the plugin folder
        final FilenameFilter tempFilter = (dir, name) -> name.endsWith(TEMP_FOLDER);
        File pluginFolder = pluginFolderAccessor.getPluginFolder();
        if (pluginFolderAccessor == null) {
            LOG.error("Plugin folder not found.");
            return "Plugin folder not found.";
        }

        // get the jar as file
        ZipInputStream jarStream = null;
        File jar = null;
        try {
            if (isFileSupported(plugin)) {
                jarStream = convert(plugin);
            } else {
                LOG.error("File format " + getExtensionName(plugin) + " not supported.");
                return "File format not supported.";
            }
            ZipToFileUtility unzipper = new ZipToFileUtility(jarStream,
                    pluginFolder.getCanonicalPath() + File.separatorChar + TEMP_FOLDER);
            jar = unzipper.unzip();
        } catch (IOException e) {
            // clean temporary folder
            cleanUpFolder(pluginFolder.listFiles(tempFilter)[0]);
            LOG.error("File upload failed.", e);
            return "File upload plugin failed.";
        } finally {
            try {
                jarStream.close();
            } catch (NullPointerException | IOException e) {
                LOG.error("Closing resource failed.", e);
            }
        }

        // create folder structure
        try {
            pluginName = getPluginDesignation(jar, pluginName);
            createFolderStructure(pluginFolder, pluginName);
        } catch (IOException e) {
            // clean temporary folder
            cleanUpFolder(pluginFolder.listFiles(tempFilter)[0]);
            LOG.error("Plugin space allocation failed.", e);
            return "Plugin space allocation failed.";
        }

        // install files
        try {
            installFiles(jar, pluginName);
        } catch (IOException e) {
            // clean temporary folder
            cleanUpFolder(pluginFolder.listFiles(tempFilter)[0]);
            LOG.error("Plugin installation failed.", e);
            return "Plugin installation failed.";
        }
        
        // clean temporary folder
        cleanUpFolder(pluginFolder.listFiles(tempFilter)[0]);

        LOG.info("Plugin " + pluginName + " installed correctly.");
        return "Plugin installed correctly.";
    }

    public List<String> listFiles(File source, FilenameFilter filter) {
        List<String> foundFiles = new ArrayList<>();
        // get all the files from a directory
        File[] fList = source.listFiles();
        for (File file : fList) {
            if (file.isFile() && filter.accept(source, file.getAbsolutePath())) {
                foundFiles.add(file.getAbsolutePath());
            } else if (file.isDirectory()) {
                foundFiles.addAll(listFiles(file, filter));
            }
        }
        return foundFiles;
    }

    String getExtensionName(MultipartFile file) {
        return FilenameUtils.getExtension(file.getOriginalFilename());
    }

    boolean isFileSupported(MultipartFile file) {
        return getExtensionName(file).equals(supportedType);
    }

    ZipInputStream convert(MultipartFile file) throws IOException {
        return new ZipInputStream(file.getInputStream());
    }

    String getPluginDesignation(File jar, String pluginName) throws IOException {
        String val;
        FilenameFilter filter = (dir, name) -> name.toLowerCase().endsWith(".class");
        List<String> classes = listFiles(jar, filter);
        if (classes.size() > 0)
            val = classes.get(0);
        else
            throw new NoClassFileException(
                    supportedType + " file does not contain any class files.");
        int pluginDesignation = val.lastIndexOf(TEMP_FOLDER);
        val = val.substring(pluginDesignation + TEMP_FOLDER.length() + 1).replace(File.separatorChar, '.')
                .replace(TEMP_FOLDER + ".", "");
        val = val.replace(".class", "");
        int lastDot = val.lastIndexOf('.');
        return val.substring(0, lastDot) + "." + pluginName;
    }

    void createFolderStructure(File pluginFolder, String name) throws FolderCreationException {
        String newPluginPath = pluginFolder.getAbsolutePath() + File.separatorChar + name;
        String newClasses = newPluginPath + File.separatorChar + CLASSES_FOLDER;
        String newRes = newPluginPath + File.separatorChar + RESOURCES_FOLDER;
        String newLib = newPluginPath + File.separatorChar + LIB_FOLDER;
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

    void installFiles(File jar, String pluginName) throws IOException {
        final FilenameFilter classFilter = (dir, name) -> name.endsWith(".class");
        final FilenameFilter pluginXMLFilter = (dir, name) -> name.contains("plugin.xml");
        final FilenameFilter pluginDTDFilter = (dir, name) -> name.contains("plugin.dtd");
        final FilenameFilter libsFilter = (dir, name) -> name.endsWith(".jar");
        final FilenameFilter resourceFiles =
                (dir, name) -> !name.endsWith(".class") & !name.endsWith(".jar")
                        & !name.contains("plugin.dtd") & !name.contains("plugin.xml");
        copyFiles(listFiles(jar, classFilter), pluginName, File.separatorChar + CLASSES_FOLDER);
        copyFiles(listFiles(jar, pluginXMLFilter), pluginName, "");
        copyFiles(listFiles(jar, pluginDTDFilter), pluginName, "");
        copyFiles(listFiles(jar, libsFilter), pluginName, File.separatorChar + LIB_FOLDER);
        copyFiles(listFiles(jar, resourceFiles), pluginName, File.separatorChar + RESOURCES_FOLDER);
    }

    void copyFiles(List<String> classes, String pluginName, String pluginInternalPath)
            throws IOException {
        for (int i = 0; i < classes.size(); i++) {
            // found class file -> write
            String sourcePath = classes.get(i);
            String destPath = classes.get(i).replace(TEMP_FOLDER, pluginName + pluginInternalPath);
            File destFile = new File(destPath);
            if (!destFile.getParentFile().isDirectory())
                destFile.getParentFile().mkdirs();
            destFile.createNewFile();
            Files.copy(Paths.get(sourcePath), Paths.get(destPath),
                    StandardCopyOption.REPLACE_EXISTING);
        }
    }
    
    void cleanUpFolder(File tempFolder) {
        File[] files = tempFolder.listFiles();
        //some JVMs return null for empty dirs
        if(files != null) {
            for(File f: files) {
                if(f.isDirectory()) {
                    cleanUpFolder(f);
                    f.delete();
                } else {
                    f.delete();
                }
            }
        }
    }

    public String getSupportedType() {
        return supportedType;
    }

    public void setSupportedType(String supportedType) {
        this.supportedType = supportedType;
    }

}
