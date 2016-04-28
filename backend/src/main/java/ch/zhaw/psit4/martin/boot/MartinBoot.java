package ch.zhaw.psit4.martin.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

import ch.zhaw.psit4.martin.pluginlib.IPluginLibrary;

import org.springframework.context.ApplicationContext;

/**
 * Entry point for the application to bootstrap jpf, SPRING 
 *
 * @version 0.0.1-SNAPSHOT
 */

@SpringBootApplication
@ImportResource("classpath:Beans.xml")
public class MartinBoot {
    /**
     * The application context
     */
    @SuppressWarnings("unused")
	private static ApplicationContext context;
    
    //@Autowired
    //private static IPluginLibrary pluginLibrary;

    /**
     * Main application entry point launches MArtIn and used components.
     * 
     * @param args
     *            Command line arguments (unused)
     */
    public static void main(String[] args) {
    	MartinBoot.context = SpringApplication.run(MartinBoot.class, args);
    	//pluginLibrary.startLibrary();
    }
}
