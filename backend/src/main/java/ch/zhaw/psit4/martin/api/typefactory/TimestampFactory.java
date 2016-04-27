package ch.zhaw.psit4.martin.api.typefactory;

import java.util.Optional;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.wareninja.opensource.strtotime.Str2Time;

import ch.zhaw.psit4.martin.api.types.IMartinTypeInstanciationException;
import ch.zhaw.psit4.martin.api.types.Timestamp;

public class TimestampFactory {
	public Timestamp fromString(String rawInput) throws IMartinTypeInstanciationException {
		Timestamp timestamp = new Timestamp(rawInput);

		if(Str2Time.convert(rawInput) != null){			
			DateTimeZone timeZone = DateTimeZone.forID("Europe/Zurich");
			DateTime dateTime = new DateTime(Str2Time.convert(rawInput), timeZone);
			timestamp.setDatetime(Optional.ofNullable(dateTime));
		}

		return timestamp;
	}
}
