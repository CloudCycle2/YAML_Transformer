package org.opentosca.yamlconverter.constraints;

public class Equal<T> extends TypeScalar<T, T> {

	public Equal(Class<T> dataType, Object constraintObject) {
		super(dataType, constraintObject);
	}

	@Override
	public boolean isValid(T value) {
		return this.constraintArgument.equals(value);
	}

}
