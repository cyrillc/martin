package ch.zhaw.psit4.martin.language.typefactory;

import ch.zhaw.psit4.martin.api.language.parts.Phrase;
import ch.zhaw.psit4.martin.api.types.BaseTypeInstanciationException;
import ch.zhaw.psit4.martin.api.types.MDuration;
import ch.zhaw.psit4.martin.language.analyis.AnnotatedSentence;
import edu.stanford.nlp.time.SUTime.DurationWithFields;

public class MDurationFactory {
	
	public static MDuration fromPhrase(Phrase phrase, AnnotatedSentence sentence) throws BaseTypeInstanciationException {
		DurationWithFields temporal = (DurationWithFields)phrase.getPayload();
		MDuration duration = new MDuration(phrase.getValue());
		
		duration.setDuration(temporal.getDuration().getJodaTimeDuration());
		duration.setPeriod(temporal.getDuration().getJodaTimePeriod());
		
		return duration;
	}

}
