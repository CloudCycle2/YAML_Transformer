package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

public abstract class TypeRegex extends ConstraintClause {
	protected final java.util.regex.Pattern constraintPattern;

	public TypeRegex(Object value) {
		super(value);
		this.constraintPattern = java.util.regex.Pattern.compile((String) value);
	}

}
