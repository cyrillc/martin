package ch.zhaw.psit4.martin.language.typefactory;

import java.util.Optional;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.wareninja.opensource.strtotime.Str2Time;

import ch.zhaw.psit4.martin.api.types.BaseTypeInstanciationException;
import ch.zhaw.psit4.martin.api.types.MTimestamp;
import ch.zhaw.psit4.martin.language.analyis.AnnotatedSentence;

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.time.*;
import edu.stanford.nlp.util.CoreMap;

public class MTimestampFactory {

	public MTimestamp fromString(String rawInput, AnnotatedSentence sentence) throws BaseTypeInstanciationException {
		MTimestamp martinTimestamp = new MTimestamp(rawInput);

		if (Str2Time.convert(rawInput) != null) {
			DateTimeZone timeZone = DateTimeZone.forID("Europe/Zurich");
			DateTime dateTime = new DateTime(Str2Time.convert(rawInput), timeZone);
			martinTimestamp.setDatetime(Optional.ofNullable(dateTime));
		}

		return martinTimestamp;
	}

}
