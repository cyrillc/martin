/**
 * 
 */
package ch.zhaw.psit4.martin.pluginlib.db;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

/**
 * DAO for ExampleCalls to manage database access. The class defines the methods
 * to access the plugin database.
 * 
 * 
 * @author Daniel Zuerrer
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public class ExampleCallDao {
    
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sf) {
        this.sessionFactory = sf;
    }

    public void add(ExampleCall exampleCall) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(exampleCall);
    }
    
    @SuppressWarnings("unchecked")
    public List<ExampleCall> listExampleCalls() {
        Session session = this.sessionFactory.getCurrentSession();
        return session.createQuery("from plugins").list();
    }
    
    public ExampleCall getById(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        ExampleCall helloMartin = (ExampleCall) session.get(ExampleCall.class,
                id);
        return helloMartin;
    }
    
    public void updateExampleCall(ExampleCall exampleCall) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(exampleCall);
    }
    
    public void removeExampleCall(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        ExampleCall hm = this.getById(id);
        if (hm != null) {
            session.delete(hm);
        }
    }
    
}
