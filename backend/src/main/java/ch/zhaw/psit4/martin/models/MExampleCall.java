package ch.zhaw.psit4.martin.models;

import javax.persistence.Entity;
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
@Table(name = "example_call")
public class MExampleCall extends BaseModel {

	private String example_call;
	private String description;

	public MExampleCall() {
	}

	public void setCall(String message) {
		this.example_call = message;
	}

	public String getCall() {
		return example_call;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
