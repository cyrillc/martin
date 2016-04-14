package ch.zhaw.psit4.martin.pluginlib.db.keyword;

import ch.zhaw.psit4.martin.pluginlib.db.parameter.*;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

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
    @Column(name = "keyword_id")
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
    
    public Set<Parameter> getParentParameter() {
        return this.parameter;
    }

}
