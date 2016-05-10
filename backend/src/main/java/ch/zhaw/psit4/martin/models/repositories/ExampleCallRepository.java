package ch.zhaw.psit4.martin.models.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.zhaw.psit4.martin.models.ExampleCall;

public class ExampleCallRepository{
	@PersistenceContext
	private EntityManager entityManager;
	private static final Log LOG = LogFactory.getLog(PluginRepository.class);

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
}


