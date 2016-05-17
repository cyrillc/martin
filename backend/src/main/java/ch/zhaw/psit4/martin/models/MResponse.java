package ch.zhaw.psit4.martin.models;

import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import ch.zhaw.psit4.martin.api.types.output.MOutput;
import ch.zhaw.psit4.martin.api.types.output.MOutputType;
import ch.zhaw.psit4.martin.timing.TimingInfo;


@Entity
@Table(name = "response")
public class MResponse extends BaseModel {

    private List<MOutput> responses;

    public void setResponseList(List<MOutput> responseList) {
        this.responses = responses;
    }

    @Transient
    private List<TimingInfo> timingInfo = new ArrayList<>();


    public MResponse() {}

    public MResponse(List<MOutput> responseList) {
        this.responses = responseList;
    }

    public List<MOutput> getResponseText() {
        return responses;
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

    public void setResponseErrorText(String errorMessage) {
        setSingleResponse(MOutputType.ERROR, errorMessage);
    }


    @NotNull
    @Column(name = "responseText")
    private String saveResponseText() {
        StringBuilder pStringBuilder = new StringBuilder();
        responses.stream().forEach(r -> pStringBuilder.append(r.toJSON()));
        return pStringBuilder.toString();
    }

    public void setSingleResponse(MOutputType type,String text) {
        List<MOutput> response = new ArrayList<>();
        response.add(new MOutput(type, text));
        this.responses = response;
    }

}
