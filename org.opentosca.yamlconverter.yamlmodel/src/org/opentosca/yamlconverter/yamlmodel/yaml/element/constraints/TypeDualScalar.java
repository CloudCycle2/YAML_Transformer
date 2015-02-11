package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

import java.util.Map;

public abstract class TypeDualScalar<T, R> extends ConstraintClause<T> {
	protected final R constraintValue1;
	protected final R constraintValue2;

	@SuppressWarnings("unchecked")
	public TypeDualScalar(Class<T> dataType, Object constraintObject) {
		super(dataType);
		final Map<Object, Object> dualValues = (Map<Object, Object>) constraintObject;
		if (dualValues.size() > 1) {
			throw new IllegalArgumentException("Dual constraint has more than two scalars.");
		}
		final Object object1 = dualValues.keySet().iterator().next();
		final Object object2 = dualValues.get(object1);
		this.constraintValue1 = (R) (object1 instanceof String ? convert((String) object1) : object1);
		this.constraintValue2 = (R) (object2 instanceof String ? convert((String) object2) : object2);
	}
}
