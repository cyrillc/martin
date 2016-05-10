package ch.zhaw.psit4.martin.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "request")
public class Request {

    public Request() {}

    @Id
    @Column(name = "request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "command")
    @NotNull
    private String command;

    public Request(String command) {
        setCommand(command);
    }

    public int getId() {
        return this.id;
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
        if (!(obj instanceof Request)) {
            return false;
        }
        final Request r = (Request) obj;
        if (this.getId() != r.getId()
                || !this.getCommand().equals(r.getCommand())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return (int) super.hashCode() * (this.id + this.getCommand().hashCode()) * 13;
    }
}
