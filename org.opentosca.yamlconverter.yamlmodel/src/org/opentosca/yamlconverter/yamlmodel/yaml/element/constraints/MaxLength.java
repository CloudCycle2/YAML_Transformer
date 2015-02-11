package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

public class MaxLength extends TypeScalar {

	public MaxLength(Object value) {
		super(value);
	}

	@Override
	public boolean isValid(Object value) {
		if (value instanceof String) {
			final int length = Integer.parseInt(this.constraintValue);
			return ((String) value).length() <= length;
		}
		return false;
	}

}
