package ch.zhaw.psit4.martin.models;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import ch.zhaw.psit4.martin.models.Request;
import ch.zhaw.psit4.martin.models.Response;

/**
 * A HistoryItem is used to keep track of the Requests sent to Martin and the
 * relative Responses.
 *
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "history_item")
public class HistoryItem extends BaseModel {

	@NotNull
	private Timestamp date;

	@OneToOne(cascade = CascadeType.ALL)
	private Request request;

	@OneToOne(cascade = CascadeType.ALL)
	private Response response;

	public HistoryItem() {
	}

	public HistoryItem(Request request, Response response) {
		this.request = request;
		this.response = response;
		this.date = new Timestamp(new Date().getTime());
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof HistoryItem)) {
			return false;
		}
		final HistoryItem item = (HistoryItem) obj;
		if (this.getId() != item.getId() || !this.getDate().equals(item.getDate())
				|| !this.request.equals(item.getRequest()) || !this.response.equals(item.getResponse())) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return this.getId() + this.getDate().hashCode() * this.getRequest().hashCode() * this.getResponse().hashCode();
	}
}
