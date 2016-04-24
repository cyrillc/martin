package ch.zhaw.psit4.martin.db.keyword;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class provides methods to easily save and read Keywords from the
 * database.
 * 
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class KeywordService {
    
    private KeywordDao keywordDao;
    
    public void setKeywordDao(KeywordDao keywordDao) {
        this.keywordDao = keywordDao;
    }

    @Transactional
    public void addKeyword(Keyword keyword) {
        this.keywordDao.add(keyword);
    }

    @Transactional
    public List<Keyword> listKeywords() {
        return this.keywordDao.listKeywords();
    }

    @Transactional
    public Keyword getKeywordById(int id) {
        return keywordDao.getById(id);
    }

    @Transactional
    public void updateKeyword(Keyword keyword) {
        this.keywordDao.updateKeyword(keyword);
    }

    @Transactional
    public void removeKeyword(int id) {
        this.keywordDao.removeKeyword(id);
    }

}

