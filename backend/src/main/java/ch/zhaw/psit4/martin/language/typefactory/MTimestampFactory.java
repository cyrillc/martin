package ch.zhaw.psit4.martin.language.typefactory;

import java.util.Optional;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.wareninja.opensource.strtotime.Str2Time;

import ch.zhaw.psit4.martin.api.types.BaseTypeInstanciationException;
import ch.zhaw.psit4.martin.api.types.MTimestamp;

public class MTimestampFactory {
	public MTimestamp fromString(String rawInput) throws BaseTypeInstanciationException {
		MTimestamp martinTimestamp = new MTimestamp(rawInput);

		if(Str2Time.convert(rawInput) != null){			
			DateTimeZone timeZone = DateTimeZone.forID("Europe/Zurich");
			DateTime dateTime = new DateTime(Str2Time.convert(rawInput), timeZone);
			martinTimestamp.setDatetime(Optional.ofNullable(dateTime));
		}

		return martinTimestamp;
	}
}
