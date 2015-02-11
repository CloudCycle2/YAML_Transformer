package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

public class MaxLength extends TypeScalar<String> {

	public MaxLength(Class<String> dataType, Object constraintObject) {
		super(dataType, constraintObject);
	}

	@Override
	public boolean isValid(String value) {
		return this.constraintValue.length() >= value.length();
	}

}
