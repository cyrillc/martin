package ch.zhaw.psit4.martin.language.typefactory;

import org.joda.time.Interval;

import ch.zhaw.psit4.martin.api.language.parts.Phrase;
import ch.zhaw.psit4.martin.api.types.BaseTypeInstanciationException;
import ch.zhaw.psit4.martin.api.types.MDuration;
import ch.zhaw.psit4.martin.language.analyis.AnnotatedSentence;
import edu.stanford.nlp.time.SUTime.Temporal;

public class MDurationFactory {
	
	public static MDuration fromPhrase(Phrase phrase, AnnotatedSentence sentence) throws BaseTypeInstanciationException {
		Temporal temporal = (Temporal)phrase.getPayload();
		MDuration duration = new MDuration(phrase.getValue());
		
		Interval start = temporal.getRange().getJodaTimeInterval();	
		Interval interval = new Interval(start.getStartMillis(), start.getStartMillis() + temporal.getDuration().getJodaTimeDuration().getMillis());
	
		duration.setDuration(temporal.getDuration().getJodaTimeDuration());
		duration.setInterval(interval);
		duration.setPeriod(temporal.getDuration().getJodaTimePeriod());
		
		return duration;
	}

}
