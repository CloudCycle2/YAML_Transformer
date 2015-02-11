package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

public abstract class TypeRegex<T> extends ConstraintClause<T> {

	protected final java.util.regex.Pattern constraintPattern;

	public TypeRegex(Class<T> dataType, Object constraintPattern) {
		super(dataType);
		this.constraintPattern = java.util.regex.Pattern.compile((String) constraintPattern);
	}

}
