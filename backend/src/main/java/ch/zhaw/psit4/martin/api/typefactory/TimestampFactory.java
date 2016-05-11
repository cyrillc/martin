package ch.zhaw.psit4.martin.api.typefactory;

import java.util.Optional;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.wareninja.opensource.strtotime.Str2Time;

import ch.zhaw.psit4.martin.api.types.IMartinTypeInstanciationException;
import ch.zhaw.psit4.martin.api.types.MartinTimestamp;

public class TimestampFactory {
	public MartinTimestamp fromString(String rawInput) throws IMartinTypeInstanciationException {
		MartinTimestamp martinTimestamp = new MartinTimestamp(rawInput);

		if(Str2Time.convert(rawInput) != null){			
			DateTimeZone timeZone = DateTimeZone.forID("Europe/Zurich");
			DateTime dateTime = new DateTime(Str2Time.convert(rawInput), timeZone);
			martinTimestamp.setDatetime(Optional.ofNullable(dateTime));
		}

		return martinTimestamp;
	}
}
