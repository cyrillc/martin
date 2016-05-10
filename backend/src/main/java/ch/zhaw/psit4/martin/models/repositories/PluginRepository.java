package ch.zhaw.psit4.martin.models.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.zhaw.psit4.martin.models.Plugin;

public class PluginRepository {

	@PersistenceContext
	private EntityManager entityManager;
	private static final Log LOG = LogFactory.getLog(PluginRepository.class);


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

}
