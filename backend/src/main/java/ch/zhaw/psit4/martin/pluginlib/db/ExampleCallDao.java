/**
 * 
 */
package ch.zhaw.psit4.martin.pluginlib.db;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * DAO for ExampleCalls to manage database access. The class defines the methods to access the
 * plugin database.
 * 
 * 
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public class ExampleCallDao {

    @Autowired
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
        return session.createQuery("from ExampleCall").list();
    }

    /**
     * Get Random Rows from Example_Call Table in DB.
     * @return List of 5 randomly choosen example calls.
     */
    @SuppressWarnings("unchecked")
    public List<ExampleCall> getRandomExampleCalls() {
        Session session = this.sessionFactory.getCurrentSession();
        return session.createSQLQuery("SELECT * FROM example_call ORDER BY RAND() LIMIT 5")
                .addEntity(ExampleCall.class).list();
    }



}
