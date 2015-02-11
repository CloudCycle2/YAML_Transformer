package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

import java.util.ArrayList;
import java.util.List;

public abstract class TypeList<T> extends ConstraintClause<T> {

	protected final List<T> constraintValues = new ArrayList<T>();

	@SuppressWarnings("unchecked")
	public TypeList(Class<T> dataType, Object constraintObjects) {
		super(dataType);
		final List<String> consraintObjectsList = (List<String>) constraintObjects;
		for (final String constraintObject : consraintObjectsList) {
			final T constraintValue = (T) (constraintObject instanceof String ? convert(constraintObject) : constraintObject);
			this.constraintValues.add(constraintValue);
		}
	}
}
