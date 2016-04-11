package ch.zhaw.psit4.martin.common;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.logging.LogLevel;
import liquibase.resource.FileSystemResourceAccessor;

import org.junit.Before;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;


import org.hibernate.cfg.Configuration;

/**
 * This class can be used to create database-tests. 
 */
public abstract class DatabaseTest {
	private Connection connection;
	private Liquibase liquibase;
	private SessionFactory hibernateSessionFactory;
	
	private static final String DATABASE_USER = "sa";
	private static final String DATABASE_PASSWORD = "sa";
	private static final String DATABASE_DRIVER = "org.h2.Driver";
	private static final String DATABASE_DIALECT = "org.hibernate.dialect.H2Dialect";
	private static final String DATABASE_DIRECTORY = "tmp";
	private static final String HIBERNATE_CONFIG = "hibernate.cfg.xml";
	
	private String databaseFile;
	private String databaseName;
	private String databaseURL;
	private File changeset;
	
	
	
	/**
	 * Sets the environment up: Creates a new H2 in-memory database with the provided chanteset.
	 * 
	 * @param changeset The provided changeset to be applied to the database.
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws LiquibaseException
	 * @throws Exception
	 */
	@Before
	public void setUp() throws SQLException, ClassNotFoundException, LiquibaseException, Exception {
		this.databaseName = UUID.randomUUID().toString();
		this.databaseFile = DatabaseTest.DATABASE_DIRECTORY + "/" + this.databaseName + ".db";
		this.databaseURL = "jdbc:h2:file:" + this.databaseFile;
	
		
		File dir = new File(DatabaseTest.DATABASE_DIRECTORY);
		dir.mkdir();
		
		// Create H2 Database
		Class.forName(DatabaseTest.DATABASE_DRIVER);
		connection = DriverManager.getConnection(this.databaseURL, DatabaseTest.DATABASE_USER, DatabaseTest.DATABASE_PASSWORD);
		Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
		
		// Inserts the provided changeset into the database
		liquibase = new Liquibase(changeset.getPath(), new FileSystemResourceAccessor(), database);
		liquibase.getLog().setLogLevel(LogLevel.WARNING);
		liquibase.update("");

		
		// Create Hibernate Session
		Configuration hibernateConfiguration = new Configuration();
		hibernateConfiguration.configure(DatabaseTest.HIBERNATE_CONFIG);
		hibernateConfiguration
				.setProperty("hibernate.dialect", DatabaseTest.DATABASE_DIALECT)
				.setProperty("hibernate.connection.driver_class", DatabaseTest.DATABASE_DRIVER)
				.setProperty("hibernate.connection.url", this.databaseURL)
				.setProperty("hibernate.connection.username", DatabaseTest.DATABASE_USER)
				.setProperty("hibernate.connection.password", DatabaseTest.DATABASE_PASSWORD)
				.setProperty("hibernate.current_session_context_class", "thread");
		hibernateSessionFactory = hibernateConfiguration.buildSessionFactory();
	}
	
	/**
	 * Cleans up the database.
	 * @throws LiquibaseException
	 * @throws SQLException
	 */
	@After
	public void tearDown() throws LiquibaseException, SQLException {
		connection.close();
		try {
			FileUtils.deleteDirectory(new File(DatabaseTest.DATABASE_DIRECTORY));	
		} catch(Exception e){
			
		}
	}
	
	protected Connection getConnection(){
		return connection;
	}
	
	protected Liquibase getLiquibase(){
		return liquibase;
	}
	
	protected Session getHibernateSession(){
		return hibernateSessionFactory.getCurrentSession();
	}
	
	protected SessionFactory getHibernateSessionFactory(){
		return hibernateSessionFactory;
	}
	
	protected void setChangesetPath(String changeSetPath){
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		this.changeset = new File(classLoader.getResource(changeSetPath).getPath());
	}

}