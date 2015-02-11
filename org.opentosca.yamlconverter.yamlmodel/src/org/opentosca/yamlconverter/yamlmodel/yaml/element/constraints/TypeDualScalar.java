package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

import java.util.Map;

public abstract class TypeDualScalar extends ConstraintClause {
	protected final String constraintValue1;
	protected final String constraintValue2;

	@SuppressWarnings("unchecked")
	public TypeDualScalar(Object value) {
		super(value);
		final Map<Object, Object> dualValues = (Map<Object, Object>) value;
		if (dualValues.size() > 1) {
			throw new IllegalArgumentException("Dual constraint has more than two scalars.");
		}
		this.constraintValue1 = (String) dualValues.keySet().iterator().next();
		this.constraintValue2 = (String) dualValues.get(this.constraintValue1);
	}
}
