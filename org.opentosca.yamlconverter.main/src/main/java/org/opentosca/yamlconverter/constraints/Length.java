package org.opentosca.yamlconverter.constraints;

/**
 * Constraint that checks if String has a certain length.
 *
 */
public class Length extends TypeScalar<String, Integer> {

	public Length(Class<String> dataType, Object constraintObject) {
		super(dataType, constraintObject);
	}

	@Override
	public boolean isValid(String value) {
		return this.constraintArgument == value.length();
	}

	@Override
	protected Object convert(String value) {
		// overwrite, as the data type of the constraint argument is not the same
		// as the data type of the constraint value. The length is always integer
		return value.length() == 0 ? null : Integer.decode(value);
	}

	@Override
	public String toString() {
		return "length of " + this.constraintArgument;
	}
}
