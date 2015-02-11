package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

public class Length extends TypeScalar<String> {

	public Length(Class<String> dataType, Object constraintObject) {
		super(dataType, constraintObject);
	}

	@Override
	public boolean isValid(String value) {
		return this.constraintValue.length() == value.length();
	}
}
