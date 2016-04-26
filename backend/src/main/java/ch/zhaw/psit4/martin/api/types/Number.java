package ch.zhaw.psit4.martin.api.types;

import java.util.Optional;

public class Number extends MartinType {
	private Optional<Integer> integerNumber = Optional.ofNullable(null);
	private Optional<Double> doubleNumber = Optional.ofNullable(null);
	private String rawFormat = "unknown";

	public Number(String data) {
		super(data);
	}

	public String getRawFormat() {
		return rawFormat;
	}

	public void setRawFormat(String rawFormat) {
		this.rawFormat = rawFormat;
	}

	public Optional<Integer> getIntegerNumber() {
		return integerNumber;
	}

	public void setIntegerNumber(Optional<Integer> integerNumber) {
		this.integerNumber = integerNumber;
	}

	public Optional<Double> getDoubleNumber() {
		return doubleNumber;
	}

	public void setDoubleNumber(Optional<Double> doubleNumber) {
		this.doubleNumber = doubleNumber;
	}

}
