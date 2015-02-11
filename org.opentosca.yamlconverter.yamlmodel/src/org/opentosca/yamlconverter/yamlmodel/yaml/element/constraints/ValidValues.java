package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

public class ValidValues<T> extends TypeList<T> {

	public ValidValues(Class<T> dataType, Object constraintObjects) {
		super(dataType, constraintObjects);
	}

	@Override
	public boolean isValid(T constraintValue) {
		for (final T validValue : this.constraintValues) {
			if (validValue.equals(constraintValue)) {
				return true;
			}
		}
		return false;
	}

}
