package ch.zhaw.psit4.martin.language.typefactory;

import ch.zhaw.psit4.martin.api.types.IBaseType;
import ch.zhaw.psit4.martin.language.analyis.AnnotatedSentence;
import ch.zhaw.psit4.martin.api.language.parts.Phrase;
import ch.zhaw.psit4.martin.api.types.BaseTypeInstanciationException;

public class BaseTypeFactory {
	public static IBaseType fromPhrase(Phrase phrase, AnnotatedSentence sentence) throws BaseTypeInstanciationException {
		switch (phrase.getType()) {
		case NUMBER:
			return MNumberFactory.fromString(phrase.getValue());
		case LOCATION:
			return MLocationFactory.fromString(phrase.getValue());
		case TIMESTAMP:
			return MTmestampFactory.fromPhrase(phrase, sentence);
		default:
			return IBaseType.fromString(phrase.getType(), phrase.getValue());
		}
	}

}
