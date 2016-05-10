package ch.zhaw.psit4.martin.models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ch.zhaw.psit4.martin.models.Keyword;
import ch.zhaw.psit4.martin.models.Parameter;
import ch.zhaw.psit4.martin.models.Plugin;

/**
 * Contains a Paramter for a Plugin Function. The class is used to store
 * Functions, their names and options of a function.
 * 
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "function")
public class Function {

	@Id
	@Column(name = "function_id", unique = true, nullable = false, updatable = false)
	@GeneratedValue
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "function", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private Set<Parameter> parameters;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "function_has_keyword", joinColumns = {
	@JoinColumn(name = "function_id", nullable = false, updatable = false) }, inverseJoinColumns = {
	@JoinColumn(name = "keyword_id", nullable = false, updatable = false) })
	private Set<Keyword> keywords;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "plugin_id", nullable = false)
	private Plugin plugin;

	public Function() {
	}

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
		parameter.stream().forEach(p -> p.setFunction(this));
		this.parameters = parameter;
	}

	public Set<Parameter> getParameters() {
		return this.parameters;
	}

	public Set<Keyword> getKeywords() {
		return this.keywords;
	}

	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}

	public Plugin getPlugin() {
		return this.plugin;
	}

	public void addParameters(Set<Parameter> parameter) {
		// needs to be set, otherwise it can not be persisted
		parameter.stream().forEach(p -> p.setFunction(this));
		this.parameters.addAll(parameter);
	}

}
