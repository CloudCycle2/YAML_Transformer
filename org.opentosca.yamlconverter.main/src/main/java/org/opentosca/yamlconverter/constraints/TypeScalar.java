package org.opentosca.yamlconverter.constraints;

public abstract class TypeScalar<T, R> extends ConstraintClause<T> {
	protected final R constraintArgument;

	@SuppressWarnings("unchecked")
	public TypeScalar(Class<T> dataType, Object constraintObject) {
		super(dataType);
		// convert the argument into their real data types, if not already done by yamlbeans
		this.constraintArgument = (R) (constraintObject instanceof String ? convert((String) constraintObject) : constraintObject);
	}
}
