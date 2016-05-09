package ch.zhaw.psit4.martin.db;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class KeywordEntityManager {

	private static EntityManager entityManager;
	private static EntityManagerFactory entityManagerFactory;
	private static final Log LOG = LogFactory.getLog(PluginEntityManager.class);
	

	public static void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory("martinPersistence");
		entityManager = entityManagerFactory.createEntityManager();
	}

	@SuppressWarnings("unchecked")
	public List<Keyword> getAll(){
		Query query = entityManager.createQuery("SELECT k FROM Keyword k");
	    return (List<Keyword>) query.getResultList();
	}
	
	public void persist(Keyword keyword){
		entityManager.getTransaction().begin();
		try {
			entityManager.persist(keyword);
		} catch (Exception e){
			entityManager.getTransaction().rollback();
			LOG.error(e);
		} finally {
			entityManager.getTransaction().commit();
		}
	}
	
	
	public Keyword getByName(String name){
		Query query = entityManager.createQuery("SELECT k FROM Keyword k WHERE k.keyword = '" + name + "'");
	    return (Keyword)query.getSingleResult();
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
