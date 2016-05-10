package ch.zhaw.psit4.martin.models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
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
public class Function extends BaseModel {

	private String name;
	private String description;

	@OneToMany(mappedBy = "function", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Parameter> parameters;

	@ManyToMany
	@JoinTable(name = "function_has_keyword", joinColumns = {
			@JoinColumn(name = "function_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "keyword_id", nullable = false, updatable = false) })
	private Set<Keyword> keywords;

	@ManyToOne
	@JoinColumn(nullable = false)
	private Plugin plugin;

	public Function() {
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
