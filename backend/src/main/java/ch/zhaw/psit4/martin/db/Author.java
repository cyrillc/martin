package ch.zhaw.psit4.martin.db;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ch.zhaw.psit4.martin.db.Plugin;

/**
 * Contains a Paramter for a Plugin Function. The class is used to store Authors, their names and
 * options of a function.
 * 
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "author")
public class Author {

    @Id
    @Column( name="author_id", unique = true, nullable = false, updatable = false )
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "author", cascade = { CascadeType.ALL,CascadeType.PERSIST,CascadeType.MERGE })
    private Set<Plugin> plugins;

    public Author() {}

    public Author(int id) {
        this.setId(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
    
    public Set<Plugin> getPlugins() {
        return plugins;
    }

    public void setPlugins(Set<Plugin> plugins) {
        this.plugins = plugins;
    }

	public void addPlugin(Plugin plugin) {
		plugins.add(plugin);
		
	}

}
