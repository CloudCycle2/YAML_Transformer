package org.opentosca.yamlconverter.constraints;

/**
 * Constraint that checks if arguments are <=.
 *
 *
 */
public class LessOrEqual extends TypeScalar<Comparable<?>, Comparable<?>> {

	public LessOrEqual(Class<Comparable<?>> dataType, Object constraintObject) {
		super(dataType, constraintObject);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean isValid(Comparable value) {
		return value.compareTo(this.constraintArgument) <= 0;
	}

	@Override
	public String toString() {
		return "less than or equal to " + this.constraintArgument;
	}
}
