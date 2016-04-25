package ch.zhaw.psit4.martin.db.historyitem;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import ch.zhaw.psit4.martin.db.request.Request;
import ch.zhaw.psit4.martin.db.response.Response;

/**
 * A HistoryItem is used to keep track of the Requests sent to Martin and the
 * relative Responses.
 *
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "historyItem")
public class HistoryItem {

    public HistoryItem() {}

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "date")
    @NotNull
    private Timestamp date;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "request")
    private Request request;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "response")
    private Response response;

    public HistoryItem(Request request, Response response) {
        this.request = request;
        this.response = response;
        this.date = new Timestamp(new Date().getTime());
    }

    public int getId() {
        return id;
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
        if (this.id != item.id || !this.getDate().equals(item.getDate())
                || !this.request.equals(item.getRequest())
                || !this.response.equals(item.getResponse())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this.id + this.getDate().hashCode()
                * this.getRequest().hashCode() * this.getResponse().hashCode();
    }
}
