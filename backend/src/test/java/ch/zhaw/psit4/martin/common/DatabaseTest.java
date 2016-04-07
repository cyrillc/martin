package ch.zhaw.psit4.martin.common;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.After;

/**
 * This class can be used to create database-tests. 
 */
public abstract class DatabaseTest {
	protected static Connection connection;
	protected static Liquibase liquibase;
	
	private String databaseFile;
	private String databaseName;
	private String changeset;
	
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
		this.databaseFile = "tmp/" + this.databaseName + ".db";
		
		File dir = new File("tmp");
		dir.mkdir();
		
		// Create H2 Database
		Class.forName("org.h2.Driver");
		connection = DriverManager.getConnection("jdbc:h2:file:" + this.databaseFile, "sa", "sa");
		Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
		
		// Inserts the provided changeset into the database
		liquibase = new Liquibase(changeset, new FileSystemResourceAccessor(), database);
		liquibase.update("");
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
			FileUtils.deleteDirectory(new File("tmp"));	
		} catch(Exception e){
			
		}
	}
	
	protected Connection getConnection(){
		return DatabaseTest.connection;
	}
	
	protected Liquibase getLiquibase(){
		return DatabaseTest.liquibase;
	}
	
	protected void setChangeset(String changeset){
		this.changeset = changeset;
	}

}