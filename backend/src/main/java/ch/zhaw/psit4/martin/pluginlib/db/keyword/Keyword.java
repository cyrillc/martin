package ch.zhaw.psit4.martin.pluginlib.db.keyword;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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


    public Keyword(int id) {
        this.setId(id);
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

}
