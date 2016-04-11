package ch.zhaw.psit4.martin.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import ch.zhaw.psit4.martin.pluginlib.IPluginLibrary;
import ch.zhaw.psit4.martin.pluginlib.PluginLibraryBootstrap;

/**
 * Entry point for the application to bootstrap jpf, SPRING and invoke
 * <code>ModuleLibraryBootstrap.boot()</code>.
 *
 * @version 0.0.1-SNAPSHOT
 */
@ComponentScan({ "ch.zhaw.psit4.martin.frontend" })
@SpringBootApplication
public class MartinBoot {

    /*
     * The martin library singleton
     */
    private static IPluginLibrary library;

     /**
     * The application context
     */
    private static ApplicationContext context;

    /**
     * Main application entry point launches MArtIn and used components.
     * 
     * @param args
     *            Command line arguments (unused)
     */
    public static void main(String[] args) {
        // boot library
        library = (new PluginLibraryBootstrap()).boot();
        // create beans and add library
        createContext();
        // boot Spring
        SpringApplication.run(MartinBoot.class, args);
    }
    
    /**
     * Creates the applicationcontext and inserts custom objects.
     */
    private static void createContext() {
        GenericApplicationContext mockContext = new GenericApplicationContext();
        mockContext.getBeanFactory().registerSingleton("IPluginLibrary",
                library);
        mockContext.refresh();
        setContext(new ClassPathXmlApplicationContext(
                new String[] {"Beans.xml"}, mockContext));
    }

    /**
     * @return the context
     */
    public static ApplicationContext getContext() {
        return context;
    }

    /**
     * @param context the context to set
     */
    public static void setContext(ApplicationContext context) {
        MartinBoot.context = context;
    }
}
