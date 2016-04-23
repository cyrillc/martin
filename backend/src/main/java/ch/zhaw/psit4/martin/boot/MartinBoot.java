package ch.zhaw.psit4.martin.boot;

import java.net.ServerSocket;
import java.util.Scanner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

/**
 * Entry point for the application to bootstrap jpf, SPRING
 *
 * @version 0.0.1-SNAPSHOT
 */

@SpringBootApplication
@ImportResource("classpath:Beans.xml")
public class MartinBoot {
	protected static ConfigurableApplicationContext context = null;
	protected static final Log LOG = LogFactory.getLog(MartinBoot.class);
	protected static Scanner scanner = new Scanner(System.in);

	/**
	 * Main application entry point launches MArtIn and used components.
	 * 
	 * @param args
	 *            Command line arguments (unused)
	 */
	public static void main(String[] args) {
		if (MartinBoot.isPortFree(Integer.parseInt(System.getProperty("server.port")))) {
			MartinBoot.runCommand("start");

			boolean run = true;
			while (run) {
				run = MartinBoot.runCommand(null);
			}
		}
	}

	public static boolean runCommand(String command) {
		if (command == null) {
			command = scanner.nextLine();
		}

		if ("start".equals(command)) {
			LOG.info("MArtin is going to start...");
			context = SpringApplication.run(MartinBoot.class);
		}

		if ("restart".equals(command)) {
			LOG.info("MArtin is going to shutdown...");
			context.close();
			LOG.info("MArtin is going to start...");
			context = SpringApplication.run(MartinBoot.class);
		}

		if ("exit".equals(command)) {
			LOG.info("MArtin is going to shutdown...");
			context.close();
			LOG.info("bye");
			return false;
		}

		return true;
	}

	public static boolean isPortFree(Integer portNumber) {
		try {
			(new ServerSocket(portNumber)).close();
			return true;
		} catch (Exception e) {
			LOG.error("Port " + portNumber.toString() + " is already in use.", e);
			return false;
		}
	}
}
