package ch.zhaw.psit4.martin.models;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "request")
public class MRequest extends BaseModel {

	@NotNull
	private String command;

	public MRequest() {
	}

	public MRequest(String command) {
		setCommand(command);
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof MRequest)) {
			return false;
		}
		final MRequest r = (MRequest) obj;
		if (this.getId() != r.getId() || !this.getCommand().equals(r.getCommand())) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return (int) super.hashCode() * (this.getId() + this.getCommand().hashCode()) * 13;
	}
}
