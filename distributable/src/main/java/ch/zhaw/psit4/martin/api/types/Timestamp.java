package ch.zhaw.psit4.martin.api.types;

import java.util.Optional;

import org.joda.time.DateTime;
import org.json.JSONObject;

public class Timestamp extends MartinType {
	private Optional<DateTime> datetime;

	public Timestamp(String data) {
		super(data);
	}

	public Optional<DateTime> getDatetime() {
		return datetime;
	}

	public void setDatetime(Optional<DateTime> datetime) {
		this.datetime = datetime;
	}

	public Optional<Long> getTimestamp() {
		if (datetime.isPresent()) {
			return Optional.ofNullable(datetime.get().getMillis());
		} else {
			return Optional.ofNullable(null);
		}
	}

	@Override
	public String toJson() {
		JSONObject json = new JSONObject();
		json.put("type", this.getClass().getName());
		json.put("data", data);

		if (datetime.isPresent()) {
			json.put("datetime", datetime.get().toString());
			json.put("timestamp", datetime.get().getMillis());
		}

		return json.toString(4);
	}
}
