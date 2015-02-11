package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

public class InRange extends TypeDualScalar<Comparable<?>, Comparable<?>> {

	public InRange(Class<Comparable<?>> dataType, Object constraintObject) {
		super(dataType, constraintObject);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean isValid(Comparable value) {
		return value.compareTo(this.constraintArgument1) >= 0 && value.compareTo(this.constraintArgument2) <= 0;
	}
}
