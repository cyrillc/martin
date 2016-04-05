package ch.zhaw.psit4.martin.common;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * A HistoryItem is used to keep track of the Requests sent to Martin and the
 * relative Responses.
 *
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "historyItem")
public class HistoryItem {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "date")
    @NotNull
    private Timestamp date;

    @Column(name = "request")
    @NotNull
    private Request request;

    @Column
    @NotNull
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
}
