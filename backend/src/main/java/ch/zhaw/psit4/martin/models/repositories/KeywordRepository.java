package ch.zhaw.psit4.martin.models.repositories;

import java.util.List;

import ch.zhaw.psit4.martin.models.Keyword;

public interface KeywordRepository extends BaseRepository<Keyword, Integer>{
	Keyword findByKeywordIgnoreCase(String keyword);
}
