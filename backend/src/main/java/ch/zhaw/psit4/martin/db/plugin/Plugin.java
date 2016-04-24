package ch.zhaw.psit4.martin.db.plugin;

import java.sql.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ch.zhaw.psit4.martin.db.author.Author;
import ch.zhaw.psit4.martin.db.function.Function;

/**
 * Contains a Paramter for a Plugin Function. The class
 * is used to store plugins, their names and options of a function. 
 * 
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "plugin")
public class Plugin {


    @Id
    @Column(name = "plugin_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "date")
    private Date date;
    

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "plugin")
    private Set<Function> functions;
    
    
    
    public Plugin() {}

    public Plugin(int id) {
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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Set<Function> getFunctions(){
        return this.functions;
    }
    
    public Author getAuthor(){
        return this.author;
    }
    
    public void setAuthor(Author author){
        this.author = author;
    }

}
