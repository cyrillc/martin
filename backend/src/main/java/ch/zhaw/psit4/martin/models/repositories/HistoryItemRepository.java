package ch.zhaw.psit4.martin.models.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import ch.zhaw.psit4.martin.models.HistoryItem;

public class HistoryItemRepository {

	@PersistenceContext
	private EntityManager entityManager;
	@SuppressWarnings("unused")
	private static final Log LOG = LogFactory.getLog(PluginRepository.class);

	@SuppressWarnings("unchecked")
	public List<HistoryItem> getAll(){
		Query query = entityManager.createQuery("SELECT h FROM HistoryItem h");
	    return (List<HistoryItem>) query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<HistoryItem> getLimitedHistory(int amount){
		Query query = entityManager.createQuery("SELECT h FROM HistoryItem h ORDER BY h.date DESC");
		query.setMaxResults(amount);
	    return (List<HistoryItem>) query.getResultList();
	}
	
	@Transactional
	public void persist(HistoryItem historyItem){
		entityManager.persist(historyItem);
	}
}

