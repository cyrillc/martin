package ch.zhaw.psit4.martin.pluginlib.db.author;

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
public class AuthorDao {
    
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sf) {
        this.sessionFactory = sf;
    }

    public void add(Author author) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(author);
    }
    
    @SuppressWarnings("unchecked")
    public List<Author> listAuthors() {
        Session session = this.sessionFactory.getCurrentSession();
        return session.createQuery("from Author").list();
    }
    
    public Author getById(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        return (Author) session.get(Author.class,id);
    }
    
    /**
     * USE WITH CAUTION!!
     * @param author
     */
    public void updateAuthor(Author author) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(author);
    }
    
    public void removeAuthor(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        Author hm = this.getById(id);
        if (hm != null) {
            session.delete(hm);
        }
    }
    
}
