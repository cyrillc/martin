package ch.zhaw.psit4.martin.models;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "response")
public class Response extends BaseModel {

	@NotNull
	private String responseText;

	public Response() {
	}

	public Response(String content) {
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
		if (!(obj instanceof Response)) {
			return false;
		}
		final Response r = (Response) obj;
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
