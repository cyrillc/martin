package ch.zhaw.psit4.martin.db;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class PluginEntityManager {

	private static EntityManager entityManager;
	

	private static EntityManagerFactory entityManagerFactory;
	

	public static void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory("martinPersistence");
		entityManager = entityManagerFactory.createEntityManager();

	}

	
	
	@SuppressWarnings("unchecked")
	public List<Plugin> getAll(){
		Query query = entityManager.createQuery("SELECT p FROM Plugin p");
	    return (List<Plugin>) query.getResultList();
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
