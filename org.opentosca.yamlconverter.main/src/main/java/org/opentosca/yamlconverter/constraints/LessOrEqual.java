package org.opentosca.yamlconverter.constraints;

public class LessOrEqual extends TypeScalar<Comparable<?>, Comparable<?>> {

	public LessOrEqual(Class<Comparable<?>> dataType, Object constraintObject) {
		super(dataType, constraintObject);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean isValid(Comparable value) {
		return value.compareTo(this.constraintArgument) <= 0;
	}
}
