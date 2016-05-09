package ch.zhaw.psit4.martin.db;

import java.util.Set;

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
import javax.persistence.Transient;

import ch.zhaw.psit4.martin.db.Function;
import ch.zhaw.psit4.martin.db.Keyword;

/**
 * Contains a Paramter for a Plugin Function. The class
 * is used to store Parameters, their names and options of a function. 
 * 
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "parameter")
public class Parameter {


    @Id
    @Column(name="parameter_id", unique = true, nullable = false, updatable = false )
    @GeneratedValue
    private int id;

    @Column(name = "name")
    private String name;
    
    @Column(name = "required")
    private boolean required;

    @Column(name = "type")
    private String type;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "parameter_has_keyword", joinColumns = { 
            @JoinColumn(name = "parameter_id", nullable = false, updatable = false) }, 
            inverseJoinColumns = { @JoinColumn(name = "keyword_id", 
                    nullable = false, updatable = false) })
    private Set<Keyword> parameterKeywords;

    @ManyToOne
    @JoinColumn(name = "function_id", nullable = false)
    private Function function;


    public Parameter() {}

    public Parameter(int id) {
        this.setId(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
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
    
    public void setParameterKeywords(Set<Keyword> parameterKeywords) {
        this.parameterKeywords = parameterKeywords;
    }

    public Set<Keyword> getKeywords(){
        return this.parameterKeywords;
    }
    
    public Function getFunction(){
        return this.function;
    }
    
    public void setFunction(Function function){
        this.function = function;
    }

}

