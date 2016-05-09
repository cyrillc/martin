package ch.zhaw.psit4.martin.db;

import java.sql.Date;
import java.util.Set;

import javax.persistence.CascadeType;
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

import ch.zhaw.psit4.martin.db.Author;
import ch.zhaw.psit4.martin.db.Function;

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
    @Column( name="plugin_id", unique = true, nullable = false, updatable = false )
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name="plugin_uuid")
    private String uuid;

    @Column(name = "name")
    private String name;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "date")
    private Date date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "plugin", cascade = { CascadeType.ALL,CascadeType.PERSIST,CascadeType.MERGE }, orphanRemoval = true)
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
    
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    public void setFunctions(Set<Function> functions) {
        this.functions = functions;
        functions.stream().forEach(f -> f.setPlugin(this));
    }

    public Set<Function> getFunctions(){
        return this.functions;
    }
    
    public Author getAuthor(){
        return this.author;
    }
    
    public void setAuthor(Author author){
        this.author = author;
        author.addPlugin(this);
    }

}
