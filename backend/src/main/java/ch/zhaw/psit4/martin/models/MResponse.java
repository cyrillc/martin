package ch.zhaw.psit4.martin.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import ch.zhaw.psit4.martin.timing.TimingInfo;


@Entity
@Table(name = "response")
public class MResponse extends BaseModel {

	@NotNull
	private String responseText;
	
	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}

	@Transient
	private List<TimingInfo> timingInfo = new ArrayList<>();

	
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
	
	public List<TimingInfo> getTimingInfo() {
		return timingInfo;
	}

	public void setTimingInfo(List<TimingInfo> timingInfo) {
		this.timingInfo = timingInfo;
	}

}
