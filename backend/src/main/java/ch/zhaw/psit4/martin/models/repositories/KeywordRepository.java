package ch.zhaw.psit4.martin.models.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.zhaw.psit4.martin.models.Keyword;

public class KeywordRepository {
	@PersistenceContext
	private EntityManager entityManager;
	private static final Log LOG = LogFactory.getLog(PluginRepository.class);
	
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
}
