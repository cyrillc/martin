package ch.zhaw.psit4.martin.common.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import ch.zhaw.psit4.martin.common.HistoryItem;

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
}
