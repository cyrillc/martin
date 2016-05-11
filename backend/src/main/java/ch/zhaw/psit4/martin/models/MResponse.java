package ch.zhaw.psit4.martin.models;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "response")
public class MResponse extends BaseModel {

	@NotNull
	private String responseText;

	public MResponse() {
	}

	public MResponse(String content) {
		this.responseText = content;
	}

	public String getResponseText() {
		return responseText;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof MResponse)) {
			return false;
		}
		final MResponse r = (MResponse) obj;
		if (this.getId() != r.getId() || !this.getResponseText().equals(r.getResponseText())) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return super.hashCode() * (this.getId() + this.getResponseText().hashCode()) * 7;
	}
}
