package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

public class Pattern extends TypeRegex {

	public Pattern(Object value) {
		super(value);
	}

	@Override
	public boolean isValid(Object value) {
		if (value instanceof String) {
			return this.constraintPattern.matcher((String) value).matches();
		}
		return false;
	}

}
