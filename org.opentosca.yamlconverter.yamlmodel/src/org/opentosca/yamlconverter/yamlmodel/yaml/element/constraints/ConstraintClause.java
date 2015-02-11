package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class ConstraintClause {

	public static ConstraintClause toConstraintClause(Map<String, Object> input, Class<?> dataType) {
		final String key = input.keySet().iterator().next();
		final Object value = input.get(key);
		try {
			return ConstraintOperator.toOperator(key).getType().getConstructor(Object.class).newInstance(value);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// should not happen
			e.printStackTrace();
			return null;
		}
	}

	public static Map<String, String> toMap(ConstraintClause input) {
		// TODO
		return new HashMap<String, String>();
	}

	public ConstraintClause(String value) {
	}

	public ConstraintClause(Integer value) {
	}

	public ConstraintClause(Float value) {
	}

	public ConstraintClause(Boolean value) {
	}

	public ConstraintClause(Date value) {
	}

	public abstract boolean isValid(Object value);

	protected final boolean isScalar(Class<?> c) {
		return c.isPrimitive() || c == String.class || c == Integer.class || c == Boolean.class || c == Float.class || c == Long.class
				|| c == Double.class || c == Short.class || c == Byte.class || c == Character.class;
	}

	protected final Object toScalar(Class<?> c) {
		if (c.isPrimitive()) {

		}
		return c.isPrimitive() || c == String.class || c == Integer.class || c == Boolean.class || c == Float.class || c == Long.class
				|| c == Double.class || c == Short.class || c == Byte.class || c == Character.class;
	}
}
