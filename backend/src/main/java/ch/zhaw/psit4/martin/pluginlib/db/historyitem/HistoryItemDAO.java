package ch.zhaw.psit4.martin.pluginlib.db.historyitem;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

/**
 * This class is used to save HistoryItems into DB
 *
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public class HistoryItemDAO {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sf) {
        this.sessionFactory = sf;
    }

    /**
     * Persist a HistoryItem.
     * 
     * @param <historyItem>
     *            to save
     * 
     */
    public void add(HistoryItem historyItem) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(historyItem);
    }
    
    /**
     * 
     * @return a list with all the items saved
     */
    @SuppressWarnings("unchecked")
    public List<HistoryItem> getAll(){
        Session session = this.sessionFactory.getCurrentSession();
        return session.createQuery("from HistoryItem").list();
    }
    
    /**
     * 
     * @return a list with the newest historyItems saved
     * 
     * @param amount the amount of historyItems to get
     */
    @SuppressWarnings("unchecked")
    public List<HistoryItem> getNewsest(int amount){
        Session session = this.sessionFactory.getCurrentSession();
        return session.createSQLQuery("(SELECT * FROM historyItem ORDER BY id DESC LIMIT " + amount +") ORDER BY id ASC").addEntity(HistoryItem.class).list();
    }
}
