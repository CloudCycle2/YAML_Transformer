package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

public class InRange extends TypeDualScalar {

	public InRange(Object value) {
		super(value);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean isValid(Object value) {
		if (value instanceof Integer) {
			final Integer comparedValued1 = Integer.parseInt(this.constraintValue1);
			final Integer comparedValued2 = Integer.parseInt(this.constraintValue2);
			return comparedValued1 <= (Integer) value && (Integer) value <= comparedValued2;
		}
		return false;
	}

}
