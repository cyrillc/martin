package ch.zhaw.psit4.martin.db;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PluginEntityManager {

	private static EntityManager entityManager;
	private static EntityManagerFactory entityManagerFactory;
	private static final Log LOG = LogFactory.getLog(PluginEntityManager.class);
	

	public static void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory("martinPersistence");
		entityManager = entityManagerFactory.createEntityManager();
	}

	@SuppressWarnings("unchecked")
	public List<Plugin> getAll(){
		Query query = entityManager.createQuery("SELECT p FROM Plugin p");
	    return (List<Plugin>) query.getResultList();
	}
	
	public Plugin byUUID(String uuid){
		Query query = entityManager.createQuery("SELECT p FROM Plugin p WHERE uuid = '" + uuid + "'");
		return (Plugin) query.getSingleResult();
	}
	
	public void persist(Plugin plugin){
		entityManager.getTransaction().begin();
		try {
			entityManager.persist(plugin);
		} catch (Exception e){
			entityManager.getTransaction().rollback();
			LOG.error(e);
		} finally {
			entityManager.getTransaction().commit();
		}
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
