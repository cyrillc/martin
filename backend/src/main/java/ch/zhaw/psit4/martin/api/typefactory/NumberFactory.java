package ch.zhaw.psit4.martin.api.typefactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import ch.zhaw.psit4.martin.api.types.IMartinTypeInstanciationException;
import ch.zhaw.psit4.martin.api.types.Number;

public class NumberFactory {
	public Number fromString(String rawInput) throws IMartinTypeInstanciationException {
		return this.fromString(rawInput, "unknown");
	}

	public Number fromString(String rawInput, String rawFormat) throws IMartinTypeInstanciationException {
		Number number = new Number(rawInput);
		number.setRawFormat(rawFormat);

		if ("numeric".equals(number.getRawFormat()) || "unknown".equals(number.getRawFormat())) {
			try {
				// try numeric format (e.g. "1000")
				number.setDoubleNumber(Optional.ofNullable(Double.parseDouble(rawInput)));
				number.setIntegerNumber(Optional.ofNullable(Integer.parseInt(rawInput)));
				number.setRawFormat("numeric");
			} catch (Exception e) {
				number.setRawFormat("unknown");
			}
		}

		if ("words-en".equals(number.getRawFormat()) || "unknown".equals(number.getRawFormat())) {
			try {
				// try word format (e.g. "thousand")
				Double result = stringToDouble(rawInput);
				if (result != null) {
					number.setDoubleNumber(Optional.ofNullable(result));
					number.setIntegerNumber(Optional.ofNullable(result.intValue()));
					number.setRawFormat("words-en");
				}
			} catch (Exception e) {
				number.setRawFormat("unknown");
			}
		}

		if ("unknown".equals(number.getRawFormat())) {
			throw new IMartinTypeInstanciationException(
					"Could not instanciate Number with raw-data \"" + rawInput + "\"");
		}

		return number;
	}

	public Double stringToDouble(String input) {
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
					System.out.println("Invalid word found : " + str);
					break;
				}
			}
			if (isValidInput) {
				for (String str : splittedParts) {
					if (str.equalsIgnoreCase("zero")) {
						result += 0;
					} else if (str.equalsIgnoreCase("one")) {
						result += 1;
					} else if (str.equalsIgnoreCase("two")) {
						result += 2;
					} else if (str.equalsIgnoreCase("three")) {
						result += 3;
					} else if (str.equalsIgnoreCase("four")) {
						result += 4;
					} else if (str.equalsIgnoreCase("five")) {
						result += 5;
					} else if (str.equalsIgnoreCase("six")) {
						result += 6;
					} else if (str.equalsIgnoreCase("seven")) {
						result += 7;
					} else if (str.equalsIgnoreCase("eight")) {
						result += 8;
					} else if (str.equalsIgnoreCase("nine")) {
						result += 9;
					} else if (str.equalsIgnoreCase("ten")) {
						result += 10;
					} else if (str.equalsIgnoreCase("eleven")) {
						result += 11;
					} else if (str.equalsIgnoreCase("twelve")) {
						result += 12;
					} else if (str.equalsIgnoreCase("thirteen")) {
						result += 13;
					} else if (str.equalsIgnoreCase("fourteen")) {
						result += 14;
					} else if (str.equalsIgnoreCase("fifteen")) {
						result += 15;
					} else if (str.equalsIgnoreCase("sixteen")) {
						result += 16;
					} else if (str.equalsIgnoreCase("seventeen")) {
						result += 17;
					} else if (str.equalsIgnoreCase("eighteen")) {
						result += 18;
					} else if (str.equalsIgnoreCase("nineteen")) {
						result += 19;
					} else if (str.equalsIgnoreCase("twenty")) {
						result += 20;
					} else if (str.equalsIgnoreCase("thirty")) {
						result += 30;
					} else if (str.equalsIgnoreCase("forty")) {
						result += 40;
					} else if (str.equalsIgnoreCase("fifty")) {
						result += 50;
					} else if (str.equalsIgnoreCase("sixty")) {
						result += 60;
					} else if (str.equalsIgnoreCase("seventy")) {
						result += 70;
					} else if (str.equalsIgnoreCase("eighty")) {
						result += 80;
					} else if (str.equalsIgnoreCase("ninety")) {
						result += 90;
					} else if (str.equalsIgnoreCase("hundred")) {
						result *= 100;
					} else if (str.equalsIgnoreCase("thousand")) {
						result *= 1000;
						finalResult += result;
						result = 0.0;
					} else if (str.equalsIgnoreCase("million")) {
						result *= 1000000;
						finalResult += result;
						result = 0.0;
					} else if (str.equalsIgnoreCase("billion")) {
						result *= 1000000000;
						finalResult += result;
						result = 0.0;
					} else if (str.equalsIgnoreCase("trillion")) {
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
