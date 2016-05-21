package ch.zhaw.psit4.martin.language.typefactory;

import java.util.Optional;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.wareninja.opensource.strtotime.Str2Time;

import ch.zhaw.psit4.martin.api.language.parts.Phrase;
import ch.zhaw.psit4.martin.api.types.BaseTypeInstanciationException;
import ch.zhaw.psit4.martin.api.types.MTimestamp;
import ch.zhaw.psit4.martin.language.analyis.AnnotatedSentence;
public class MTimestampFactory {
	
	public MTimestamp fromPhrase(Phrase phrase, AnnotatedSentence sentence) throws BaseTypeInstanciationException {
		MTimestamp martinTimestamp = new MTimestamp(phrase.getValue());

		if (Str2Time.convert(phrase.getValue()) != null) {
			DateTimeZone timeZone = DateTimeZone.forID("Europe/Zurich");
			DateTime dateTime = new DateTime(Str2Time.convert(phrase.getValue()), timeZone);
			martinTimestamp.setDatetime(Optional.ofNullable(dateTime));
		}

		return martinTimestamp;
	}

}
