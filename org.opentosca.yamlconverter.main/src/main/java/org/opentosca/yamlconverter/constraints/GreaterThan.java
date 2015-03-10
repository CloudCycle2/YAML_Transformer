package org.opentosca.yamlconverter.constraints;

public class GreaterThan extends TypeScalar<Comparable<?>, Comparable<?>> {

	public GreaterThan(Class<Comparable<?>> dataType, Object constraintObject) {
		super(dataType, constraintObject);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean isValid(Comparable value) {
		return value.compareTo(this.constraintArgument) > 0;
	}
}
