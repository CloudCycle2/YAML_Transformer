package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

public class Equal extends TypeScalar {

	public Equal(Object value) {
		super(value);
	}

	@Override
	public boolean isValid(Object value) {
		if (value instanceof String) {
			return this.constraintValue.equals(value);
		} else {

		}
		return false;
	}

}
