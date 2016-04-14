package ch.zhaw.psit4.martin.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "response")
public class Response {

    public Response() {}

    @Id
    @Column(name = "response_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "responsetext")
    @NotNull
    private String content;

    public int getId() {
        return this.id;
    }

    public Response(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
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
        if (this.getId() != r.getId()
                || !this.getContent().equals(r.getContent())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this.id + this.getContent().hashCode();
    }
}
