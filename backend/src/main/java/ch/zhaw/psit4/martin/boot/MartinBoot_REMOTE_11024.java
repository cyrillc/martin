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
import ch.zhaw.psit4.martin.pluginlib.db.keyword.KeywordService;

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
    /*
     * Service to access example calls in database
     */
    public static ExampleCallService exampleCallService;
    
    /*
     * Service to access Keywords in DB
     */
    public static KeywordService keywordService;
    
    public static ApplicationContext context;

    /**
     * Main application entry point launches MArtIn and used components.
     * 
     * @param args
     *            Command line arguments (unused)
     */
    public static void main(String[] args) {
        context = new ClassPathXmlApplicationContext("Beans.xml");
        exampleCallService = (ExampleCallService) context.getBean("exampleCallService");
        keywordService = (KeywordService) context.getBean("keywordService");
        
        // boot Spring
        SpringApplication.run(MartinBoot.class, args);
        // boot library
        library = (new PluginLibraryBootstrap()).boot();
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
