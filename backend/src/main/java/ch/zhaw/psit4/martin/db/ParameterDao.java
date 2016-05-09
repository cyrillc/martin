package ch.zhaw.psit4.martin.db;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

/**
 * DAO for Paramter to manage database access. The class defines the methods
 * to access the plugin database.
 * 
 * 
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public class ParameterDao {
    
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sf) {
        this.sessionFactory = sf;
    }

    public void add(Parameter parameter) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(parameter);
    }
    
    @SuppressWarnings("unchecked")
    public List<Parameter> listParameters() {
        Session session = this.sessionFactory.getCurrentSession();
        return session.createQuery("from Parameter").list();
    }
    
    public Parameter getById(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        return (Parameter) session.get(Parameter.class,id);
    }
    
    /**
     * USE WITH CAUTION!!
     * @param parameter
     */
    public void updateParameter(Parameter parameter) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(parameter);
    }
    
    public void removeParameter(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        Parameter hm = this.getById(id);
        if (hm != null) {
            session.delete(hm);
        }
    }
    
}
