/**
 * 
 */
package ch.zhaw.psit4.martin.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ch.zhaw.psit4.martin.frontend.FrontendController;
import ch.zhaw.psit4.martin.frontend.IFrontendController;
import ch.zhaw.psit4.martin.modulelib.IPluginLibrary;
import ch.zhaw.psit4.martin.modulelib.PluginLibraryBootstrap;

/**
 * Entry point for the application to bootstrap jpf, SPRING and invoke
 * <code>ModuleLibraryBootstrap.boot()</code>.
 *
 * @author Daniel Fabian
 * @version 0.0.1-SNAPSHOT
 */
@SpringBootApplication
public class MartinBoot {

    /*
     * The martin library singleton
     */
    private static IPluginLibrary library;
    /*
     * The frontend controller singleton
     */
    private static IFrontendController frontendController;

    /**
     * Main application entry point launches MArtIn and used components.
     * 
     * @param args
     *            Command line arguments (unused)
     */
    public static void main(String[] args) {
        // boot Spring
        SpringApplication.run(MartinBoot.class, args);
        // boot library
        //library = (new PluginLibraryBootstrap()).boot();
        // boot frontend controller
        frontendController = new FrontendController();
        frontendController.start();
        // TODO: Boot other components
    }

    public static IPluginLibrary getPluginLibrary() {
        return library;
    }
    
    public static IFrontendController getFrontendController() {
        return frontendController;
    }
}
