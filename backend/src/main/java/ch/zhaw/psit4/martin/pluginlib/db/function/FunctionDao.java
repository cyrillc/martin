package ch.zhaw.psit4.martin.pluginlib.db.function;

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
public class FunctionDao {
    
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sf) {
        this.sessionFactory = sf;
    }

    public void add(Function function) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(function);
    }
    
    @SuppressWarnings("unchecked")
    public List<Function> listFunctions() {
        Session session = this.sessionFactory.getCurrentSession();
        return session.createQuery("from Function").list();
    }
    
    public Function getById(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        return (Function) session.get(Function.class,id);
    }
    
    public List<Object[]> getByKeyword(String keyword){
    	Session session = this.sessionFactory.getCurrentSession();
    
    	return session.createQuery("FROM Function f JOIN f.functionKeywords k WHERE LOWER(k.keyword) = LOWER('" + keyword + "')").list();
    }
    
    /**
     * USE WITH CAUTION!!
     * @param function
     */
    public void updateFunction(Function function) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(function);
    }
    
    public void removeFunction(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        Function hm = this.getById(id);
        if (hm != null) {
            session.delete(hm);
        }
    }
    
}
