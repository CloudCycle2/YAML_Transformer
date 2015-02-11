package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

public class LessThan extends TypeScalar {

	public LessThan(Object value) {
		super(value);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean isValid(Object value) {
		if (value.getClass().equals(this.constraintValue.getClass()) && this.constraintValue instanceof Comparable) {
			return ((Comparable) this.constraintValue).compareTo(value) < 0;
		}
		return false;
	}

}
