package ch.zhaw.psit4.martin.models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ch.zhaw.psit4.martin.models.MFunction;
import ch.zhaw.psit4.martin.models.MKeyword;
import edu.stanford.nlp.util.ArraySet;

/**
 * Contains a Paramter for a Plugin Function. The class is used to store
 * Parameters, their names and options of a function.
 * 
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "parameter")
public class MParameter extends BaseModel {

	private String name;

	private boolean required;

	private String type;

	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "parameter_has_keyword", joinColumns = {
			@JoinColumn(name = "parameter_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "keyword_id", nullable = false, updatable = false) })
	private Set<MKeyword> parameterKeywords;

	@ManyToOne
	private MFunction function;

	public MParameter() {
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

	public void setParameterKeywords(Set<MKeyword> parameterKeywords) {
		this.parameterKeywords = parameterKeywords;
	}

	public Set<MKeyword> getKeywords() {
		return this.parameterKeywords;
	}

	public MFunction getFunction() {
		return this.function;
	}

	public void setFunction(MFunction function) {
		this.function = function;
	}

    public void addKeyword(MKeyword keyword) {
        if(parameterKeywords == null) {
            parameterKeywords = new ArraySet<>();
        }
        parameterKeywords.add(keyword);
        keyword.addParameter(this);
    }

}