package ch.zhaw.psit4.martin.models.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends CrudRepository<T, ID> {
	T findOne(ID id);
	
	List<T> findAll();
	
	List<T> findAll(Pageable pageable);
}
