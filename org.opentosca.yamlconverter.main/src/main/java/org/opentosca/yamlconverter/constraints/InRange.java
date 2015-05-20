package org.opentosca.yamlconverter.constraints;

/**
 * Constraint that checks if argument is in range.
 *
 */
public class InRange extends TypeDualScalar<Comparable<?>, Comparable<?>> {

	public InRange(Class<Comparable<?>> dataType, Object constraintObject) {
		super(dataType, constraintObject);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean isValid(Comparable value) {
		return value.compareTo(this.constraintArgument1) >= 0 && value.compareTo(this.constraintArgument2) <= 0;
	}

	@Override
	public String toString() {
		return "between " + this.constraintArgument1 + " and " + this.constraintArgument2;
	}

}
