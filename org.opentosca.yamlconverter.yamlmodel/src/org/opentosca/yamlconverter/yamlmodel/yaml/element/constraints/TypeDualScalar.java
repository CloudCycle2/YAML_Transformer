package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

import java.util.Map;

public abstract class TypeDualScalar<T, R> extends ConstraintClause<T> {
	protected final R constraintArgument1;
	protected final R constraintArgument2;

	@SuppressWarnings("unchecked")
	public TypeDualScalar(Class<T> dataType, Object constraintObject) {
		super(dataType);
		// yamlbeans parses it into a key value pair within a map.
		// As such the key is the first argument and the value is the second argument
		final Map<Object, Object> dualValues = (Map<Object, Object>) constraintObject;
		if (dualValues.size() > 1) {
			throw new IllegalArgumentException("Dual constraint has more than two scalars.");
		}
		final Object object1 = dualValues.keySet().iterator().next();
		final Object object2 = dualValues.get(object1);
		// convert the arguments into their real data types, if not already done by yamlbeans
		this.constraintArgument1 = (R) (object1 instanceof String ? convert((String) object1) : object1);
		this.constraintArgument2 = (R) (object2 instanceof String ? convert((String) object2) : object2);
	}
}
