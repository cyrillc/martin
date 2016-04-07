/**
 * 
 */
package ch.zhaw.psit4.martin.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ch.zhaw.psit4.martin.frontend.FrontendController;
import ch.zhaw.psit4.martin.frontend.IFrontendController;
import ch.zhaw.psit4.martin.pluginlib.IPluginLibrary;
import ch.zhaw.psit4.martin.pluginlib.PluginLibraryBootstrap;
import ch.zhaw.psit4.martin.pluginlib.db.ExampleCallService;

/**
 * Entry point for the application to bootstrap jpf, SPRING and invoke
 * <code>ModuleLibraryBootstrap.boot()</code>.
 *
 * @version 0.0.1-SNAPSHOT
 */
@ComponentScan({"ch.zhaw.psit4.martin.frontend", "ch.zhaw.psit4.martin.pluginlib.db"})
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
    
    public static ApplicationContext context;

    /**
     * Main application entry point launches MArtIn and used components.
     * 
     * @param args
     *            Command line arguments (unused)
     */
    public static void main(String[] args) {
        
        
        context = new ClassPathXmlApplicationContext("Beans.xml");

        // boot library
        library = (new PluginLibraryBootstrap()).boot();
        
        // boot Spring
        SpringApplication.run(MartinBoot.class, args);
        

        
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
