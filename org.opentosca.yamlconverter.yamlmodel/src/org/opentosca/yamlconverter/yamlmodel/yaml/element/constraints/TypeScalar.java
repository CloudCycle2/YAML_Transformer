package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

public abstract class TypeScalar<T> extends ConstraintClause<T> {
	protected final T constraintValue;

	@SuppressWarnings("unchecked")
	public TypeScalar(Class<T> dataType, Object constraintObject) {
		super(dataType);
		this.constraintValue = (T) (constraintObject instanceof String ? convert((String) constraintObject) : constraintObject);
	}
}
