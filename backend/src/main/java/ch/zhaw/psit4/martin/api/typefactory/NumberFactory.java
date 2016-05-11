package ch.zhaw.psit4.martin.api.typefactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.zhaw.psit4.martin.api.types.IMartinTypeInstanciationException;
import ch.zhaw.psit4.martin.api.types.MartinNumber;
import ch.zhaw.psit4.martin.boot.MartinBoot;

public class NumberFactory {
	private static final Log LOG = LogFactory.getLog(MartinBoot.class);

	public MartinNumber fromString(String rawInput) throws IMartinTypeInstanciationException {
		return this.fromString(rawInput, MartinNumber.RawFormat.UNKNOWN);
	}

	public MartinNumber fromString(String rawInput, MartinNumber.RawFormat rawFormat) throws IMartinTypeInstanciationException {
		MartinNumber martinNumber = new MartinNumber(rawInput);
		martinNumber.setRawFormat(rawFormat);

		if (MartinNumber.RawFormat.NUMERIC.equals(martinNumber.getRawFormat())
				|| MartinNumber.RawFormat.UNKNOWN.equals(martinNumber.getRawFormat())) {
			try {
				// try numeric format (e.g. "1000")
				martinNumber.setDoubleNumber(Optional.ofNullable(Double.parseDouble(rawInput)));
				martinNumber.setIntegerNumber(Optional.ofNullable(Integer.parseInt(rawInput)));
				martinNumber.setRawFormat(MartinNumber.RawFormat.NUMERIC);
			} catch (Exception e) {
				LOG.debug(e);
				martinNumber.setRawFormat(MartinNumber.RawFormat.UNKNOWN);
			}
		}

		if (MartinNumber.RawFormat.WORD_EN.equals(martinNumber.getRawFormat())
				|| MartinNumber.RawFormat.UNKNOWN.equals(martinNumber.getRawFormat())) {
			try {
				// try word format (e.g. "thousand")
				Double result = stringToDouble(rawInput);
				if (result != null) {
					martinNumber.setDoubleNumber(Optional.ofNullable(result));
					martinNumber.setIntegerNumber(Optional.ofNullable(result.intValue()));
					martinNumber.setRawFormat(MartinNumber.RawFormat.WORD_EN);
				}
			} catch (Exception e) {
				LOG.debug(e);
				martinNumber.setRawFormat(MartinNumber.RawFormat.UNKNOWN);
			}
		}

		if (MartinNumber.RawFormat.UNKNOWN.equals(martinNumber.getRawFormat())) {
			throw new IMartinTypeInstanciationException(
					"Could not instanciate Number with raw-data \"" + rawInput + "\"");
		}

		return martinNumber;
	}

	public Double stringToDouble(String rawInput) {
		String input = rawInput;
		boolean isValidInput = true;
		Double result = 0.0;
		Double finalResult = 0.0;
		List<String> allowedStrings = Arrays.asList("zero", "one", "two", "three", "four", "five", "six", "seven",
				"eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen",
				"eighteen", "nineteen", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety",
				"hundred", "thousand", "million", "billion", "trillion");

		if (input != null && input.length() > 0) {
			input = input.replaceAll("-", " ");
			input = input.toLowerCase().replaceAll(" and", " ");
			String[] splittedParts = input.trim().split("\\s+");

			for (String str : splittedParts) {
				if (!allowedStrings.contains(str)) {
					isValidInput = false;
					LOG.debug("Invalid word found : " + str);
					break;
				}
			}
			if (isValidInput) {
				for (String str : splittedParts) {
					if ("zero".equalsIgnoreCase(str)) {
						result += 0;
					} else if ("one".equalsIgnoreCase(str)) {
						result += 1;
					} else if ("two".equalsIgnoreCase(str)) {
						result += 2;
					} else if ("three".equalsIgnoreCase(str)) {
						result += 3;
					} else if ("four".equalsIgnoreCase(str)) {
						result += 4;
					} else if ("five".equalsIgnoreCase(str)) {
						result += 5;
					} else if ("six".equalsIgnoreCase(str)) {
						result += 6;
					} else if ("seven".equalsIgnoreCase(str)) {
						result += 7;
					} else if ("eight".equalsIgnoreCase(str)) {
						result += 8;
					} else if ("nine".equalsIgnoreCase(str)) {
						result += 9;
					} else if ("ten".equalsIgnoreCase(str)) {
						result += 10;
					} else if ("eleven".equalsIgnoreCase(str)) {
						result += 11;
					} else if ("twelve".equalsIgnoreCase(str)) {
						result += 12;
					} else if ("thirteen".equalsIgnoreCase(str)) {
						result += 13;
					} else if ("fourteen".equalsIgnoreCase(str)) {
						result += 14;
					} else if ("fifteen".equalsIgnoreCase(str)) {
						result += 15;
					} else if ("sixteen".equalsIgnoreCase(str)) {
						result += 16;
					} else if ("seventeen".equalsIgnoreCase(str)) {
						result += 17;
					} else if ("eighteen".equalsIgnoreCase(str)) {
						result += 18;
					} else if ("nineteen".equalsIgnoreCase(str)) {
						result += 19;
					} else if ("twenty".equalsIgnoreCase(str)) {
						result += 20;
					} else if ("thirty".equalsIgnoreCase(str)) {
						result += 30;
					} else if ("forty".equalsIgnoreCase(str)) {
						result += 40;
					} else if ("fifty".equalsIgnoreCase(str)) {
						result += 50;
					} else if ("sixty".equalsIgnoreCase(str)) {
						result += 60;
					} else if ("seventy".equalsIgnoreCase(str)) {
						result += 70;
					} else if ("eighty".equalsIgnoreCase(str)) {
						result += 80;
					} else if ("ninety".equalsIgnoreCase(str)) {
						result += 90;
					} else if ("hundred".equalsIgnoreCase(str)) {
						result *= 100;
					} else if ("thousand".equalsIgnoreCase(str)) {
						result *= 1000;
						finalResult += result;
						result = 0.0;
					} else if ("million".equalsIgnoreCase(str)) {
						result *= 1000000;
						finalResult += result;
						result = 0.0;
					} else if ("billion".equalsIgnoreCase(str)) {
						result *= 1000000000;
						finalResult += result;
						result = 0.0;
					} else if ("trillion".equalsIgnoreCase(str)) {
						result *= 1000000000000L;
						finalResult += result;
						result = 0.0;
					}
				}
				return finalResult + result;
			}
		}
		return null;
	}
}
