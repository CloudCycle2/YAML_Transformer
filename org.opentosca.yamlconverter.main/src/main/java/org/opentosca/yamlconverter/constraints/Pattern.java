package org.opentosca.yamlconverter.constraints;

/**
 * Constraint that checks if String matches a certain regular expression.
 *
 */
public class Pattern extends TypeRegex {

	public Pattern(Class<String> dataType, Object constraintPattern) {
		super(dataType, constraintPattern);
	}

	@Override
	public boolean isValid(String value) {
		return this.constraintPattern.matcher(value).matches();
	}

	@Override
	public String toString() {
		return "matches pattern " + this.constraintPattern;
	}
}
