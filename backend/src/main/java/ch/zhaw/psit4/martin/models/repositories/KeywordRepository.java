package ch.zhaw.psit4.martin.models.repositories;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import ch.zhaw.psit4.martin.models.Keyword;

public class KeywordRepository {
	@PersistenceContext
	private EntityManager entityManager;
	private static final Log LOG = LogFactory.getLog(PluginRepository.class);

	@SuppressWarnings("unchecked")
	public List<Keyword> getAll() {
		Query query = entityManager.createQuery("SELECT k FROM Keyword k");
		return (List<Keyword>) query.getResultList();
	}

	@Transactional
	public void persist(Keyword keyword) {
		entityManager.persist(keyword);
	}

	public Optional<Keyword> getByName(String name) {
		Query query = entityManager.createQuery("SELECT k FROM Keyword k WHERE k.keyword = '" + name + "'");
		try {
			return Optional.ofNullable((Keyword) query.getSingleResult());
		} catch (Exception e) {
			LOG.debug(e);
			return Optional.ofNullable(null);
		}

	}
}
