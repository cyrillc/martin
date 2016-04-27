package ch.zhaw.psit4.martin.api.typefactory;

import ch.zhaw.psit4.martin.api.types.EMartinType;
import ch.zhaw.psit4.martin.api.types.IMartinType;
import ch.zhaw.psit4.martin.api.types.IMartinTypeInstanciationException;

public class MartinTypeFactory {
	public static IMartinType fromType(EMartinType type, String rawValue) throws IMartinTypeInstanciationException {
		switch (type) {
		case NUMBER:
			NumberFactory numberFactory = new NumberFactory();
			return numberFactory.fromString(rawValue);
		case TIMESTAMP:
			TimestampFactory timestampFactory = new TimestampFactory();
			return timestampFactory.fromString(rawValue);
		case LOCATION:
			LocationFactory locationFactory = new LocationFactory();
			return locationFactory.fromString(rawValue);
		default:
			return IMartinType.fromString(type, rawValue);
		}
	}

}
