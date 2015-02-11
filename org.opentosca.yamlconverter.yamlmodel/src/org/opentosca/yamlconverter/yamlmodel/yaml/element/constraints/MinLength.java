package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

public class MinLength extends TypeScalar<String> {

	public MinLength(Class<String> dataType, Object constraintObject) {
		super(dataType, constraintObject);
	}

	@Override
	public boolean isValid(String value) {
		return this.constraintValue.length() <= value.length();
	}

}
