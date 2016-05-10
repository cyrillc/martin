package ch.zhaw.psit4.martin.models.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import ch.zhaw.psit4.martin.models.HistoryItem;

public interface HistoryItemRepository extends BaseRepository<HistoryItem, Integer> {
	@Query("SELECT h FROM HistoryItem h ORDER BY h.date DESC")
	List<HistoryItem> getLimitedHistory(Pageable pageable);
}
