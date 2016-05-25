package ch.zhaw.psit4.martin.language.typefactory;

import ch.zhaw.psit4.martin.api.types.IBaseType;
import ch.zhaw.psit4.martin.language.analyis.AnnotatedSentence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.zhaw.psit4.martin.api.language.parts.Phrase;
import ch.zhaw.psit4.martin.api.types.BaseTypeInstanciationException;

public class BaseTypeFactory {
	private static final Log LOG = LogFactory.getLog(BaseTypeFactory.class);
	public static IBaseType fromPhrase(Phrase phrase, AnnotatedSentence sentence) throws BaseTypeInstanciationException {
		
		LOG.info(phrase.getType());
		
		switch (phrase.getType()) {
		case NUMBER:
			return MNumberFactory.fromString(phrase.getValue());
		case LOCATION:
			return MLocationFactory.fromString(phrase.getValue());
		case TIMESTAMP:
			return MTimestampFactory.fromPhrase(phrase, sentence);
		case DURATION:
			return MDurationFactory.fromPhrase(phrase, sentence);
		case SET:
			return MSetFactory.fromPhrase(phrase, sentence);
		default:
			return IBaseType.fromString(phrase.getType(), phrase.getValue());
		}
	}

}
