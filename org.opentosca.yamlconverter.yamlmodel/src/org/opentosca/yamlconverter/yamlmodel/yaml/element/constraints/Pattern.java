package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

public class Pattern extends TypeRegex<String> {

	public Pattern(Class<String> dataType, Object constraintPattern) {
		super(dataType, constraintPattern);
	}

	@Override
	public boolean isValid(String value) {
		return this.constraintPattern.matcher(value).matches();
	}

}
