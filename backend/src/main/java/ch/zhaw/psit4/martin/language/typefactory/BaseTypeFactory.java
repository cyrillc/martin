package ch.zhaw.psit4.martin.language.typefactory;

import ch.zhaw.psit4.martin.api.types.EBaseType;
import ch.zhaw.psit4.martin.api.types.IBaseType;
import ch.zhaw.psit4.martin.language.analyis.AnnotatedSentence;
import ch.zhaw.psit4.martin.api.language.parts.ISentence;
import ch.zhaw.psit4.martin.api.types.BaseTypeInstanciationException;

public class BaseTypeFactory {
	public static IBaseType fromType(EBaseType type, String rawValue, AnnotatedSentence sentence) throws BaseTypeInstanciationException {
		switch (type) {
		case NUMBER:
			MNumberFactory numberFactory = new MNumberFactory();
			return numberFactory.fromString(rawValue);
		case TIMESTAMP:
			MTimestampFactory timestampFactory = new MTimestampFactory();
			return timestampFactory.fromString(rawValue, sentence);
		case LOCATION:
			MLocationFactory locationFactory = new MLocationFactory();
			return locationFactory.fromString(rawValue);
		default:
			return IBaseType.fromString(type, rawValue);
		}
	}

}
