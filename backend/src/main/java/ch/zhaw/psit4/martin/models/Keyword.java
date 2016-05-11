package ch.zhaw.psit4.martin.models;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * Contains a Keyword for a Plugin. Either Funtion or Parameter. The class is
 * used to store a retreived keywords of a plugin.
 * 
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "keyword")
public class Keyword extends BaseModel {

	private String keyword;

	// mapped by set in Parameter.java
	@ManyToMany(mappedBy = "parameterKeywords")
	private Set<Parameter> parameter;

	@ManyToMany(mappedBy = "keywords")
	private Set<Function> functions;

	public Keyword() {
	}

	public Keyword(String keyword) {
		this.setKeyword(keyword);
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

	public Set<Function> getFunctions() {
		return functions;
	}

	public void setFunctions(Set<Function> functions) {
		this.functions = functions;
	}

}
