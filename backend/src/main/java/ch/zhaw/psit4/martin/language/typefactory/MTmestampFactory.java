package ch.zhaw.psit4.martin.language.typefactory;

import org.joda.time.Instant;
import org.joda.time.Partial;

import ch.zhaw.psit4.martin.api.language.parts.Phrase;
import ch.zhaw.psit4.martin.api.types.BaseTypeInstanciationException;
import ch.zhaw.psit4.martin.api.types.MTimestamp;
import ch.zhaw.psit4.martin.language.analyis.AnnotatedSentence;
import edu.stanford.nlp.time.SUTime.Temporal;

public class MTmestampFactory {
	public static MTimestamp fromPhrase(Phrase phrase, AnnotatedSentence sentence) throws BaseTypeInstanciationException {
		Temporal temporal = (Temporal)phrase.getPayload();
		MTimestamp timestamp = new MTimestamp(phrase.getValue());
		
		timestamp.setPartial(temporal.getTime().getJodaTimePartial());
		timestamp.setInstant(temporal.getTime().getJodaTimeInstant());
		
		Instant instant = temporal.getTime().getJodaTimeInstant();
		Partial partial = temporal.getTime().getJodaTimePartial();

		return timestamp;
	}
}
