package ch.zhaw.psit4.martin.pluginlib.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Contains an example call for a plugin to send to MArtIn frontend. The class
 * is used to store a retreived example call of a plugin from the database and
 * send it to the website. *
 * 
 * @author Daniel Zuerrer
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "plugins")
public class ExampleCall {

    public ExampleCall() {}

    @Id
    @Column(name = "example_call_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "call")
    private String call;

    @Column(name = "description")
    private String description;

    public ExampleCall(int id) {
        this.setId(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCall(String message) {
        this.call = message;
    }

    public String getCall() {
        return call;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
