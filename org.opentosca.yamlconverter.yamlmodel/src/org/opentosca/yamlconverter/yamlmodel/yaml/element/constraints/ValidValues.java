package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

public class ValidValues extends TypeList {

	public ValidValues(Object value) {
		super(value);
	}

	@Override
	public boolean isValid(Object value) {
		if (isScalar(value.getClass())) {

		} else {
			for (final String validValue : this.constraintValues) {
				if (validValue.equals(value)) {
					return true;
				}
			}
		}
		return false;
	}

}
