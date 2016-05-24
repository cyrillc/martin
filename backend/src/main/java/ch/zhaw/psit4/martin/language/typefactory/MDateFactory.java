package ch.zhaw.psit4.martin.language.typefactory;

import org.joda.time.Instant;
import org.joda.time.Partial;

import ch.zhaw.psit4.martin.api.language.parts.Phrase;
import ch.zhaw.psit4.martin.api.types.BaseTypeInstanciationException;
import ch.zhaw.psit4.martin.api.types.MDate;
import ch.zhaw.psit4.martin.language.analyis.AnnotatedSentence;
import edu.stanford.nlp.time.SUTime.Temporal;

public class MDateFactory {
	public static MDate fromPhrase(Phrase phrase, AnnotatedSentence sentence) throws BaseTypeInstanciationException {
		Temporal temporal = (Temporal)phrase.getPayload();
		MDate date = new MDate(phrase.getValue());
		date.setTimexExpression(phrase.getNormalizedValue());
		
		date.setPartial(temporal.getTime().getJodaTimePartial());
		date.setInstant(temporal.getTime().getJodaTimeInstant());
		
		Instant instant = temporal.getTime().getJodaTimeInstant();
		Partial partial = temporal.getTime().getJodaTimePartial();

		return date;
	}
}
