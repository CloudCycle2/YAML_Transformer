package org.opentosca.yamlconverter.constraints;

public abstract class TypeRegex extends ConstraintClause<String> {

	protected final java.util.regex.Pattern constraintPattern;

	public TypeRegex(Class<String> dataType, Object constraintPattern) {
		super(dataType);
		this.constraintPattern = java.util.regex.Pattern.compile((String) constraintPattern);
	}

}
