package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

public class Length extends TypeScalar {

	public Length(Object value) {
		super(value);
	}

	@Override
	public boolean isValid(Object value) {
		if (value instanceof String) {
			final int length = Integer.parseInt(this.constraintValue);
			return ((String) value).length() == length;
		}
		return false;
	}

}
