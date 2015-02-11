package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

public class MaxLength extends TypeScalar<String, Integer> {

	public MaxLength(Class<String> dataType, Object constraintObject) {
		super(dataType, constraintObject);
	}

	@Override
	public boolean isValid(String value) {
		return this.constraintArgument >= value.length();
	}

	@Override
	protected Object convert(String value) {
		// overwrite, as the data type of the constraint argument is not the same
		// as the data type of the constraint value. The length is always integer
		return value.length() == 0 ? null : Integer.decode(value);
	}

}
