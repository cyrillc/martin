package ch.zhaw.psit4.martin.common.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
     * @param <id> of the item to find
     * @return the item with the given id
     */
    public HistoryItem getbyId(int id){
        Session session = this.sessionFactory.getCurrentSession();
        HistoryItem historyItem = (HistoryItem) session.get(HistoryItem.class, id);
        return historyItem;
    }
    
    /**
     * 
     * @return a list with all the items saved
     */
    @SuppressWarnings("unchecked")
    public List<HistoryItem> getAll(){
        Session session = this.sessionFactory.getCurrentSession();
        return session.createQuery("from historyItem").list();
    }
    
    /**
     * 
     * @param <historyItem> to update
     */
    public void update(HistoryItem historyItem){
        Session session = this.sessionFactory.getCurrentSession();
        session.update(historyItem);
    }
    
    /**
     * remove the item with the given id
     * 
     * @param <id> of the item to remove
     */
    public void remove(int id){
        Session session = this.sessionFactory.getCurrentSession();
        HistoryItem historyItem = this.getbyId(id);
        if(historyItem != null){
            session.delete(historyItem);
        }
    }
}
