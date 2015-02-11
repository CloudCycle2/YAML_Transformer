package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

import java.util.ArrayList;
import java.util.List;

public abstract class TypeList<T, R> extends ConstraintClause<T> {

	protected final List<R> constraintArguments = new ArrayList<R>();

	@SuppressWarnings("unchecked")
	public TypeList(Class<T> dataType, Object constraintObjects) {
		super(dataType);
		final List<String> consraintObjectsList = (List<String>) constraintObjects;
		for (final String constraintObject : consraintObjectsList) {
			// convert the argument into their real data types, if not already done by yamlbeans
			final R constraintValue = (R) (constraintObject instanceof String ? convert(constraintObject) : constraintObject);
			this.constraintArguments.add(constraintValue);
		}
	}
}
