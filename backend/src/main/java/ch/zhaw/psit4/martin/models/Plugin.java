package ch.zhaw.psit4.martin.models;

import java.sql.Date;
import java.text.ParseException;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.java.plugin.registry.Extension;

import com.mysql.jdbc.SocketMetadata.Helper;

import ch.zhaw.psit4.martin.common.MartinHelper;
import ch.zhaw.psit4.martin.models.Author;
import ch.zhaw.psit4.martin.models.Function;
import ch.zhaw.psit4.martin.pluginlib.filesystem.PluginDataAccessor;

/**
 * Contains a Paramter for a Plugin Function. The class is used to store
 * plugins, their names and options of a function.
 * 
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "plugin")
public class Plugin extends BaseModel {

	private String uuid;
	private String name;
	private String description;
	private Date date;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(nullable = false)
	private Author author;

	@OneToMany(mappedBy = "plugin", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Function> functions;


	public Plugin(){}
	
	
	public Plugin(String uuid, String name, String description, Date date) {
	    this.name = name;
	    this.uuid = uuid;
	    this.description = description;
	    if(date != null){
	        this.date = date;
	    }
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

	public Set<Function> getFunctions() {
		return this.functions;
	}

	public Author getAuthor() {
		return this.author;
	}

	public void setAuthor(Author author) {
		this.author = author;
		//author.addPlugin(this);
	}

}
