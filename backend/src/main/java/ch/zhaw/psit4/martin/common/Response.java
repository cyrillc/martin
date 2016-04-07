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

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "responsetext")
    @NotNull
    private final String content;

    public Response(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
