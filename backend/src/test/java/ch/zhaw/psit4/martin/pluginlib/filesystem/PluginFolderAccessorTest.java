package ch.zhaw.psit4.martin.pluginlib.filesystem;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class PluginFolderAccessorTest {
    
    PluginFolderAccessor validFolderAccessor;
    PluginFolderAccessor noJSONFolderAccessor;
    PluginFolderAccessor noFolderAccessor;
    PluginFolderAccessor faultyFolderAccessor;

    @Before
    public void setUp() {
        String pluginsFolder = "plugins";
        String pluginsJSON = "library-test.cfg.json";
        String missingFolder = "missingFolder";
        String missingJSON = "missing-lib.cfg.json";
        validFolderAccessor = new PluginFolderAccessor(pluginsFolder, pluginsJSON);
        noJSONFolderAccessor = new PluginFolderAccessor(pluginsFolder, missingJSON);
        noFolderAccessor = new PluginFolderAccessor(missingFolder, pluginsJSON);
        faultyFolderAccessor = new PluginFolderAccessor(missingFolder, missingJSON);
    }

    public void testGetPluginFolder() {
        File valid = validFolderAccessor.getPluginFolder();
        File noJSON = noJSONFolderAccessor.getPluginFolder();
        File noFolder = noFolderAccessor.getPluginFolder();
        File faulty = faultyFolderAccessor.getPluginFolder();
        
        assertNotNull(valid);
        assertNotNull(noJSON);
        assertNull(noFolder);
        assertNull(faulty);
    }

}
