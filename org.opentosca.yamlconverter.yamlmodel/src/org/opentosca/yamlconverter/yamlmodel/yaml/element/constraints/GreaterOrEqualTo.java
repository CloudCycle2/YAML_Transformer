package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

public class GreaterOrEqualTo extends TypeScalar {

	public GreaterOrEqualTo(Object value) {
		super(value);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean isValid(Object value) {
		if (value instanceof Integer) {
			final Integer comparedValued = Integer.parseInt(this.constraintValue);
			return comparedValued.compareTo((Integer) value) >= 0;
		}
		return false;
	}

}
