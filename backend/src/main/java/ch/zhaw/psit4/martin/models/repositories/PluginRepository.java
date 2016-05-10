package ch.zhaw.psit4.martin.models.repositories;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import ch.zhaw.psit4.martin.models.Plugin;

public class PluginRepository {

	@PersistenceContext
	private EntityManager entityManager;
	private static final Log LOG = LogFactory.getLog(PluginRepository.class);

	@SuppressWarnings("unchecked")
	public List<Plugin> getAll() {
		Query query = entityManager.createQuery("SELECT p FROM Plugin p");
		return (List<Plugin>) query.getResultList();
	}

	public Optional<Plugin> byUUID(String uuid) {
		Query query = entityManager.createQuery("SELECT p FROM Plugin p WHERE uuid = '" + uuid + "'");
		try {
			return Optional.ofNullable((Plugin) query.getSingleResult());
		} catch (Exception e) {
			LOG.debug(e);
			return Optional.ofNullable(null);
		}
	}

	@Transactional
	public void persist(Plugin plugin) {
		entityManager.persist(plugin);
	}

}
