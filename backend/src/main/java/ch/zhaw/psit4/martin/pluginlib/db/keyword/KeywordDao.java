package ch.zhaw.psit4.martin.pluginlib.db.keyword;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

/**
 * DAO for Keywords to manage database access. The class defines the methods
 * to access the plugin database.
 * 
 * 
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public class KeywordDao {
    
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sf) {
        this.sessionFactory = sf;
    }

    public void add(Keyword keyword) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(keyword);
    }
    
    @SuppressWarnings("unchecked")
    public List<Keyword> listKeywords() {
        Session session = this.sessionFactory.getCurrentSession();
        return session.createQuery("from Keyword").list();
    }
    
    public Keyword getById(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        Keyword helloMartin = (Keyword) session.get(Keyword.class,
                id);
        return helloMartin;
    }
    
    public void updateKeyword(Keyword keyword) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(keyword);
    }
    
    public void removeKeyword(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        Keyword hm = this.getById(id);
        if (hm != null) {
            session.delete(hm);
        }
    }
    
}
