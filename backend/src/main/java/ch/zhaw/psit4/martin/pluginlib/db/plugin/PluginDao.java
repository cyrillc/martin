package ch.zhaw.psit4.martin.pluginlib.db.plugin;

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
public class PluginDao {
    
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sf) {
        this.sessionFactory = sf;
    }

    public void add(Plugin plugin) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(plugin);
    }
    
    @SuppressWarnings("unchecked")
    public List<Plugin> listPlugins() {
        Session session = this.sessionFactory.getCurrentSession();
        return session.createQuery("from Plugin").list();
    }
    
    public Plugin getById(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        return (Plugin) session.get(Plugin.class,id);
    }
    
    /**
     * USE WITH CAUTION!!
     * @param plugin
     */
    public void updatePlugin(Plugin plugin) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(plugin);
    }
    
    public void removePlugin(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        Plugin hm = this.getById(id);
        if (hm != null) {
            session.delete(hm);
        }
    }
    
}
