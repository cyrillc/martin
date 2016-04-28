package ch.zhaw.psit4.martin.db.keyword;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ch.zhaw.psit4.martin.db.parameter.*;

/**
 * Contains a Keyword for a Plugin. Either Funtion or Parameter. The class
 * is used to store a retreived keywords of a plugin. 
 * 
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "keyword")
public class Keyword {

    public Keyword() {}

    @Id
    @Column(name="keyword_id", unique = true, nullable = false, updatable = false )
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "keyword")
    private String keyword;

    // mapped by set in Parameter.java
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "parameterKeywords")
    private Set<Parameter> parameter;


    public Keyword(int id) {
        this.setId(id);
    }

    public Keyword(String keyword ) {
        this.setKeyword(keyword);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }
    
    public Set<Parameter> getParameter() {
        return parameter;
    }

    public void setParameter(Set<Parameter> parameter) {
        this.parameter = parameter;
    }
    
    public Set<Parameter> getParentParameter() {
        return this.parameter;
    }

}
