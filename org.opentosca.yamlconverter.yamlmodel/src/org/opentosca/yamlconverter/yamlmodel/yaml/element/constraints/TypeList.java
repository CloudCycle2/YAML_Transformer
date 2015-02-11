package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

import java.util.List;

public abstract class TypeList extends ConstraintClause {
	protected final List<String> constraintValues;

	@SuppressWarnings("unchecked")
	public TypeList(Object value) {
		super(value);
		this.constraintValues = (List<String>) value;
	}
}
