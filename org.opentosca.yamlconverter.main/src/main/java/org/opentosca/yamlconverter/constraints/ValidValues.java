package org.opentosca.yamlconverter.constraints;

public class ValidValues<T> extends TypeList<T, T> {

	public ValidValues(Class<T> dataType, Object constraintObjects) {
		super(dataType, constraintObjects);
	}

	@Override
	public boolean isValid(T constraintValue) {
		for (final T validValue : this.constraintArguments) {
			if (validValue.equals(constraintValue)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "one of " + this.constraintArguments;
	}
}
