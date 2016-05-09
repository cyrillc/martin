package ch.zhaw.psit4.martin.db;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ExampleCallEntityManager {

	private static EntityManager entityManager;
	private static EntityManagerFactory entityManagerFactory;
	private static final Log LOG = LogFactory.getLog(PluginEntityManager.class);
	

	public static void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory("martinPersistence");
		entityManager = entityManagerFactory.createEntityManager();
	}

	@SuppressWarnings("unchecked")
	public List<ExampleCall> getAll(){
		Query query = entityManager.createQuery("SELECT h FROM ExampleCall h");
	    return (List<ExampleCall>) query.getResultList();
	}
	
	public void persist(ExampleCall ExampleCall){
		entityManager.getTransaction().begin();
		try {
			entityManager.persist(ExampleCall);
		} catch (Exception e){
			entityManager.getTransaction().rollback();
			LOG.error(e);
		} finally {
			entityManager.getTransaction().commit();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ExampleCall> getRandomCalls(){
		Query query = entityManager.createQuery("SELECT h FROM ExampleCall h");
		query.setMaxResults(5);
	    return (List<ExampleCall>) query.getResultList();
	}
	
	
	public static void destroy() {
		if (entityManager != null) {
			entityManager.close();
		}

		if (entityManagerFactory != null) {
			entityManagerFactory.close();
		}
	}
}


