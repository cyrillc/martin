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

/**
 * Handles plugin installation.
 * 
 * @version 0.0.1-SNAPSHOT
 */
public class PluginInstaller {

    private static final String CLASSES_FOLDER = "classes";
    private static final String RESOURCES_FOLDER = "res";
    private static final String LIB_FOLDER = "lib";
    private static final String TEMP_FOLDER = "temp";
    private static final String TEMP_META_FILE = "META.dat";

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


    /**
     * Installs a plugin in martin's plugin directory.
     * 
     * @param pluginName The user given name of the plugin.
     * @param plugin The uploaded plugin data.
     * @return Returns a human readable message string.
     */
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


    /**
     * Lists all files of a given directory recursively that match the given filter.
     * 
     * @param source The source directory to search.
     * @param filter The filter of type {@link FilenameFilter} that decides if any given file should
     *        be returned.
     * @return Returns a list of the found elements.
     */
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

    /**
     * Gets the extension name of an uploaded file.
     * 
     * @param file The file to check.
     * @return The extension name.
     */
    String getExtensionName(MultipartFile file) {
        return FilenameUtils.getExtension(file.getOriginalFilename());
    }

    /**
     * Checks if the given filetype is supported by the uploader (Filetypes are defined in pom.xml).
     * 
     * @param file The file to check.
     * @return true or false.
     */
    boolean isFileSupported(MultipartFile file) {
        return getExtensionName(file).equals(supportedType);
    }

    /**
     * Converts an uploaded file into a {@link ZipInputStream}.
     * 
     * @param file The file to convert.
     * @return The generated stream.
     * @throws IOException Throws an {@link IOException} on failiure.
     */
    ZipInputStream convert(MultipartFile file) throws IOException {
        return new ZipInputStream(file.getInputStream());
    }

    /**
     * Creates the full plugin designation that combines user name and java package name.
     * 
     * @param jar The jar file in the temporary folder.
     * @param pluginName The user given plugin name.
     * @return The final plugin name.
     * @throws IOException Throws an {@link IOException} on read failiure or a
     *         {@link NoClassFileException} if the there are no .class files in the jar.
     */
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
        val = val.substring(pluginDesignation + TEMP_FOLDER.length() + 1)
                .replace(File.separatorChar, '.').replace(TEMP_FOLDER + ".", "");
        val = val.replace(".class", "");
        int lastDot = val.lastIndexOf('.');
        return val.substring(0, lastDot);// + "." + pluginName;
    }

    /**
     * Prepares the basic plugin structure.
     * 
     * @param pluginFolder The folder to create the plugin in.
     * @param name The final name of the given plugin.
     * @throws FolderCreationException Throws an exception if the folder structure could not be
     *         created.
     */
    void createFolderStructure(File pluginFolder, String name) throws FolderCreationException {
        String newPluginPath = pluginFolder.getAbsolutePath() + File.separatorChar + name;
        String newClasses = newPluginPath + File.separatorChar + CLASSES_FOLDER;
        String newRes = newPluginPath + File.separatorChar + RESOURCES_FOLDER;
        String newLib = newPluginPath + File.separatorChar + LIB_FOLDER;
        createFolder(newClasses);
        createFolder(newRes);
        createFolder(newLib);
    }

    /**
     * Creates a folder structure with the given path.
     * 
     * @param folderPath The path structure to create.
     * @throws FolderCreationException Throws an exception if the folder structure could not be
     *         created.
     */
    void createFolder(String folderPath) throws FolderCreationException {
        File file = new File(folderPath);
        if (!file.isDirectory()) {
            boolean success = file.mkdirs();
            if (!success) {
                throw new FolderCreationException("Could not create path: " + file.getPath());
            }
        }
    }

    /**
     * Installs the files of the uploaded jar from the temporary into the final plugin directory
     * structure. Implementation is dependent on the plugin framework used.
     * 
     * @param jar The jar to copy.
     * @param pluginName The final plugin name.
     * @throws IOException Throws an exception if any operation encounters an error.
     */
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

    /**
     * Copies files from a list to a given plugin directory, creating any missing folders in the
     * process.
     * 
     * @param files The List of files to copy.
     * @param pluginName The final plugin directory name.
     * @param pluginInternalPath The path inside the plugin folder to copy the files to.
     * @throws IOException Any file operation can throw an exception.
     */
    void copyFiles(List<String> files, String pluginName, String pluginInternalPath)
            throws IOException {
        for (int i = 0; i < files.size(); i++) {
            // found class file -> write
            String sourcePath = files.get(i);
            String destPath = files.get(i).replace(TEMP_FOLDER, pluginName + pluginInternalPath);
            File destFile = new File(destPath);
            if (!destFile.getParentFile().isDirectory())
                createFolder(destFile.getParentFile().getAbsolutePath());
            destFile.createNewFile();
            Files.copy(Paths.get(sourcePath), Paths.get(destPath),
                    StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /**
     * Cleans the given Folder ignoring {@link TEMP_META_FILE}.
     * 
     * @param tempFolder The temporary folder to clean.
     */
    void cleanUpFolder(File tempFolder) {
        File[] files = tempFolder.listFiles();
        // some JVMs return null for empty dirs
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    cleanUpFolder(f);
                    f.delete();
                } else {
                    if (!f.getName().endsWith(TEMP_META_FILE))
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
