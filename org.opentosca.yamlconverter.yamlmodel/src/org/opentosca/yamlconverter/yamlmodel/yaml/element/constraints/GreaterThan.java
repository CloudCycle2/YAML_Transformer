package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

public class GreaterThan extends TypeScalar<Comparable<?>, Comparable<?>> {

	public GreaterThan(Class<Comparable<?>> dataType, Object constraintObject) {
		super(dataType, constraintObject);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean isValid(Comparable value) {
		return value.compareTo(this.constraintValue) > 0;
	}
}
