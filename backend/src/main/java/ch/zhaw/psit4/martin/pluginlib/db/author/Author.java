package ch.zhaw.psit4.martin.pluginlib.db.author;

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
import javax.persistence.Table;

import org.hibernate.annotations.ManyToAny;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import ch.zhaw.psit4.martin.pluginlib.db.function.Function;
import ch.zhaw.psit4.martin.pluginlib.db.keyword.Keyword;

/**
 * Contains a Paramter for a Plugin Function. The class
 * is used to store Authors, their names and options of a function. 
 * 
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "author")
public class Author {


    @Id
    @Column(name = "author_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;
    
    @Column(name = "type")
    private String type;
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "author_has_keyword", joinColumns = { 
            @JoinColumn(name = "author_id", nullable = false, updatable = false) }, 
            inverseJoinColumns = { @JoinColumn(name = "keyword_id", 
                    nullable = false, updatable = false) })
    private Set<Keyword> authorKeywords;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "function_id", nullable = false)
    private Function function;

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

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public Set<Keyword> getKeywords(){
        return this.authorKeywords;
    }
    
    public Function getFunction(){
        return this.function;
    }
    
    public void setFunction(Function function){
        this.function = function;
    }

}
