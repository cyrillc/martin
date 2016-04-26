package ch.zhaw.psit4.martin.db.function;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ch.zhaw.psit4.martin.db.keyword.Keyword;
import ch.zhaw.psit4.martin.db.parameter.Parameter;
import ch.zhaw.psit4.martin.db.plugin.Plugin;

/**
 * Contains a Paramter for a Plugin Function. The class
 * is used to store Functions, their names and options of a function. 
 * 
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "function")
public class Function {


    @Id
    @Column( name="function_id", unique = true, nullable = false, updatable = false )
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;
    
    @Column(name = "description")
    private String description;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "function", cascade = { CascadeType.ALL,CascadeType.PERSIST,CascadeType.MERGE })
    private Set<Parameter> parameter;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "function_has_keyword", joinColumns = { 
            @JoinColumn(name = "function_id", nullable = false, updatable = false) }, 
            inverseJoinColumns = { @JoinColumn(name = "keyword_id", 
                    nullable = false, updatable = false) })
    private Set<Keyword> functionKeywords;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plugin_id", nullable = false)
    private Plugin plugin;

    public Function() {}

    public Function(int id) {
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
    
    public void setParameter(Set<Parameter> parameter) {
        this.parameter = parameter;
    }
    
    public Set<Parameter> getParameter(){
        return this.parameter;
    }

    public Set<Keyword> getKeywords(){
        return this.functionKeywords;
    }
    
    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }
    
    public Plugin getPlugin(){
    	return this.plugin;
    }

}
