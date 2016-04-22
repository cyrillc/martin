package ch.zhaw.psit4.martin.boot;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ch.zhaw.psit4.martin.requestProcessor.RequestProcessor;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Entry point for the application to bootstrap jpf, SPRING 
 *
 * @version 0.0.1-SNAPSHOT
 */

@SpringBootApplication
@ImportResource("classpath:Beans.xml")
public class MartinBoot {
    /**
     * Main application entry point launches MArtIn and used components.
     * 
     * @param args
     *            Command line arguments (unused)
     */
    public static void main(String[] args) {
    	Log logger = LogFactory.getLog(MartinBoot.class);
    	try {
    		SpringApplication.run(MartinBoot.class, args);
    	} catch(Exception e){
    		logger.debug(e);
    		logger.error(e.getLocalizedMessage());
    	}	
    }
}
