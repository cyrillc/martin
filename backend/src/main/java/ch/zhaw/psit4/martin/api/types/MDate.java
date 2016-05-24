package ch.zhaw.psit4.martin.api.types;

import org.joda.time.Instant;
import org.joda.time.Partial;

public class MDate extends BaseType {
	private String timexExpression;
	private Partial partial;
	private Instant instant;
	
	public MDate(String data) {
		super(data);
	}
	
	public String getTimexExpression() {
		return timexExpression;
	}

	public void setTimexExpression(String timexExpression) {
		this.timexExpression = timexExpression;
	}
	
	public Partial getPartial() {
		return partial;
	}

	public void setPartial(Partial partial) {
		this.partial = partial;
	}

	public Instant getInstant() {
		return instant;
	}

	public void setInstant(Instant instant) {
		this.instant = instant;
	}

}
