package ch.zhaw.psit4.martin.api.typefactory;

import java.util.Optional;

import com.wareninja.opensource.strtotime.Str2Time;

import ch.zhaw.psit4.martin.api.types.IMartinTypeInstanciationException;
import ch.zhaw.psit4.martin.api.types.Timestamp;

public class TimestampFactory {
	public Timestamp fromString(String rawInput) throws IMartinTypeInstanciationException {
		Timestamp timestamp = new Timestamp(rawInput);
		
		timestamp.setDatetime(Optional.ofNullable(Str2Time.convert(rawInput)));
	
		return timestamp;
	}
}
