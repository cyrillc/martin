/**
 * 
 */
package ch.zhaw.psit4.martin.boot;

import ch.zhaw.psit4.martin.modulelib.IPluginLibrary;
import ch.zhaw.psit4.martin.modulelib.PluginLibraryBootstrap;

/**
 * Entry point for the application to bootstrap jpf, SPRING and invoke
 * <code>ModuleLibraryBootstrap.boot()</code>.
 *
 * @author Daniel Fabian
 * @version 0.0.1-SNAPSHOT
 */
public class MartinBoot {

    /*
     * The martin booter singleton
     */
    private static MartinBoot booter;
    /*
     * The martin library singleton
     */
    private static IPluginLibrary library;

    /**
     * Main application entry point launches MArtIn and used components.
     * 
     * @param args
     *            Command line arguments (unused)
     */
    public static void main(String[] args) {
        booter = new MartinBoot();
    }

    private MartinBoot() {
        // boot library
        library = (new PluginLibraryBootstrap()).boot();
    }

    public static IPluginLibrary getPluginLibrary() {
        return library;
    }
}
