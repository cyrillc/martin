package ch.zhaw.psit4.martin.db;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HistoryItemEntityManager {

	private static EntityManager entityManager;
	private static EntityManagerFactory entityManagerFactory;
	private static final Log LOG = LogFactory.getLog(PluginEntityManager.class);
	

	public static void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory("martinPersistence");
		entityManager = entityManagerFactory.createEntityManager();
	}

	@SuppressWarnings("unchecked")
	public List<HistoryItem> getAll(){
		Query query = entityManager.createQuery("SELECT h FROM HistoryItem h");
	    return (List<HistoryItem>) query.getResultList();
	}
	
	public List<HistoryItem> getLimitedHistory(int amount){
		Query query = entityManager.createQuery("SELECT h FROM HistoryItem h ORDER BY h.date DESC");
		query.setMaxResults(amount);
	    return (List<HistoryItem>) query.getResultList();
	}
	

	public void persist(HistoryItem historyItem){
		entityManager.getTransaction().begin();
		try {
			entityManager.persist(historyItem);
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

