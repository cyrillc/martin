package ch.zhaw.psit4.martin.pluginlib.db.historyitem;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service just to test some work.. should waiting for a better solution
 * 
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class HistoryItemService {
    
    private HistoryItemDAO historyItemDAO;

    public void setHistoryItemDAO(HistoryItemDAO historyItemDAO) {
        this.historyItemDAO = historyItemDAO;
    }
    
    @Transactional
    public void addHistoryItem(HistoryItem historyItem){
        this.historyItemDAO.add(historyItem);
    }
    
    /**
     * 
     * @return A list of all History
     */
    @Transactional
    public List<HistoryItem> getHistory(){
        return this.historyItemDAO.getAll();
    }
    
    /**
     * 
     * @return A list of the newest History
     *
     * @param amount the amount of historyItems to get
     */
    @Transactional
    public List<HistoryItem> getLimitedHistory(int amount){
        return this.historyItemDAO.getNewsest(amount);
    }
}
