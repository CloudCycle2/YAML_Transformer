package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

public class MinLength extends TypeScalar<String, Integer> {

	public MinLength(Class<String> dataType, Object constraintObject) {
		super(dataType, constraintObject);
	}

	@Override
	public boolean isValid(String value) {
		return this.constraintValue <= value.length();
	}

	@Override
	protected Object convert(String value) {
		return value.length() == 0 ? null : Integer.decode(value);
	}

}
