package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

public class InRange extends TypeDualScalar<Comparable<?>> {

	public InRange(Class<Comparable<?>> dataType, Object constraintObject) {
		super(dataType, constraintObject);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean isValid(Comparable value) {
		return value.compareTo(this.constraintValue1) >= 0 && value.compareTo(this.constraintValue2) <= 0;
	}
}
