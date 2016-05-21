package ch.zhaw.psit4.martin.language.typefactory;

import ch.zhaw.psit4.martin.api.types.IBaseType;
import ch.zhaw.psit4.martin.language.analyis.AnnotatedSentence;
import ch.zhaw.psit4.martin.api.language.parts.Phrase;
import ch.zhaw.psit4.martin.api.types.BaseTypeInstanciationException;

public class BaseTypeFactory {
	public static IBaseType fromPhrase(Phrase phrase, AnnotatedSentence sentence) throws BaseTypeInstanciationException {
		switch (phrase.getType()) {
		case NUMBER:
			MNumberFactory numberFactory = new MNumberFactory();
			return numberFactory.fromString(phrase.getValue());
		case TIMESTAMP:
			MTimestampFactory timestampFactory = new MTimestampFactory();
			return timestampFactory.fromPhrase(phrase, sentence);
		case LOCATION:
			MLocationFactory locationFactory = new MLocationFactory();
			return locationFactory.fromString(phrase.getValue());
		default:
			return IBaseType.fromString(phrase.getType(), phrase.getValue());
		}
	}

}
