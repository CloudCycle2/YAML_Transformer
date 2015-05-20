package org.opentosca.yamlconverter.constraints;

/**
 * Constraint that checks of arguments are equal.
 *
 * @param <T> Type of arguments
 */
public class Equal<T> extends TypeScalar<T, T> {

	public Equal(Class<T> dataType, Object constraintObject) {
		super(dataType, constraintObject);
	}

	@Override
	public boolean isValid(T value) {
		return this.constraintArgument.equals(value);
	}

	@Override
	public String toString() {
		return "equal to " + this.constraintArgument;
	}

}
