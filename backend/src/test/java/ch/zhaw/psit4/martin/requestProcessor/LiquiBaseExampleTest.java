package ch.zhaw.psit4.martin.requestProcessor;

import static org.junit.Assert.*;

import java.sql.DriverManager;
import java.sql.SQLException;

import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;

public class LiquiBaseExampleTest {

	private static Connection conn;
	private static Liquibase liquibase;

	@BeforeClass
	public static void createTestData() throws SQLException,ClassNotFoundException, LiquibaseException {
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
	    DriverManagerDataSource dataSource = (DriverManagerDataSource)context.getBean("dataSource");
	   
		Connection conn = DriverManager.getConnection(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword());
		
		Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
		
		liquibase = new Liquibase("db/testdata/db.testdata.xml",new FileSystemResourceAccessor(), database);
		liquibase.dropAll();

	}
	
	@Test
	public void testExtendRequestPluginAndFeature() throws Exception {
		assertTrue(true);
	}

	@AfterClass
	public static void removeTestData() throws LiquibaseException, SQLException {
		liquibase.rollback(1000, null);
		conn.close();
	}

}
