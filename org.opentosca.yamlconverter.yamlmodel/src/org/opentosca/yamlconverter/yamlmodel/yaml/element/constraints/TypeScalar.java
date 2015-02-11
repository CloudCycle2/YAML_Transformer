package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

public abstract class TypeScalar extends ConstraintClause {
	protected final String constraintValue;

	public TypeScalar(Object value) {
		super(value);
		this.constraintValue = (String) value;
	}
}
