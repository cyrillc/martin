package ch.zhaw.psit4.martin.language.typefactory;

import ch.zhaw.psit4.martin.api.language.parts.Phrase;
import ch.zhaw.psit4.martin.api.types.BaseTypeInstanciationException;
import ch.zhaw.psit4.martin.api.types.MTimestamp;
import ch.zhaw.psit4.martin.language.analyis.AnnotatedSentence;
public class MTimestampFactory {
	
	public MTimestamp fromPhrase(Phrase phrase, AnnotatedSentence sentence) throws BaseTypeInstanciationException {
		MTimestamp martinTimestamp = new MTimestamp(phrase.getValue());
		
		Temporal temporal = new Temporal(phrase.getNormalizedValue());

		return martinTimestamp;
	}

}
