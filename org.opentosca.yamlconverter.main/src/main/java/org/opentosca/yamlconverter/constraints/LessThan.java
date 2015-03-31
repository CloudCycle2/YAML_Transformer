package org.opentosca.yamlconverter.constraints;

public class LessThan extends TypeScalar<Comparable<?>, Comparable<?>> {

	public LessThan(Class<Comparable<?>> dataType, Object constraintObject) {
		super(dataType, constraintObject);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean isValid(Comparable value) {
		return value.compareTo(this.constraintArgument) < 0;
	}

	@Override
	public String toString() {
		return "less than " + this.constraintArgument;
	}
}
