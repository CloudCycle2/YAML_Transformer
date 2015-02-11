package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

public abstract class TypeScalar<T, R> extends ConstraintClause<T> {
	protected final R constraintValue;

	@SuppressWarnings("unchecked")
	public TypeScalar(Class<T> dataType, Object constraintObject) {
		super(dataType);
		this.constraintValue = (R) (constraintObject instanceof String ? convert((String) constraintObject) : constraintObject);
	}
}
