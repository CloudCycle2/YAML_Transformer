package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class ConstraintClause<T> {

	@SuppressWarnings("unchecked")
	public static ConstraintClause<Object> toConstraintClause(Map<String, Object> input, Class<?> dataType) {
		final String key = input.keySet().iterator().next();
		final Object value = input.get(key);
		try {
			return ConstraintOperator.toOperator(key).getType().getConstructor(Class.class, Object.class).newInstance(dataType, value);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// should not happen
			e.printStackTrace();
			return null;
		}
	}

	private final Class<T> dataType;

	public static Map<String, Object> toMap(ConstraintClause input) {
		// TODO
		return new HashMap<String, Object>();
	}

	public ConstraintClause(Class<T> dataType) {
		this.dataType = dataType;
	}

	public abstract boolean isValid(T value);

	protected final Object convert(String value) {
		final Class<T> type = this.dataType;
		Object convertedValue = null;
		if (type == String.class) {
			convertedValue = value;
		} else if (type == Integer.TYPE) {
			convertedValue = value.length() == 0 ? 0 : Integer.decode(value);
		} else if (type == Integer.class) {
			convertedValue = value.length() == 0 ? null : Integer.decode(value);
		} else if (type == Boolean.TYPE) {
			convertedValue = value.length() == 0 ? false : Boolean.valueOf(value);
		} else if (type == Boolean.class) {
			convertedValue = value.length() == 0 ? null : Boolean.valueOf(value);
		} else if (type == Float.TYPE) {
			convertedValue = value.length() == 0 ? 0 : Float.valueOf(value);
		} else if (type == Float.class) {
			convertedValue = value.length() == 0 ? null : Float.valueOf(value);
		} else if (type == Double.TYPE) {
			convertedValue = value.length() == 0 ? 0 : Double.valueOf(value);
		} else if (type == Double.class) {
			convertedValue = value.length() == 0 ? null : Double.valueOf(value);
		} else if (type == Long.TYPE) {
			convertedValue = value.length() == 0 ? 0 : Long.decode(value);
		} else if (type == Long.class) {
			convertedValue = value.length() == 0 ? null : Long.decode(value);
		} else if (type == Short.TYPE) {
			convertedValue = value.length() == 0 ? 0 : Short.decode(value);
		} else if (type == Short.class) {
			convertedValue = value.length() == 0 ? null : Short.decode(value);
		} else if (type == Character.TYPE) {
			convertedValue = value.length() == 0 ? 0 : value.charAt(0);
		} else if (type == Character.class) {
			convertedValue = value.length() == 0 ? null : value.charAt(0);
		} else if (type == Byte.TYPE) {
			convertedValue = value.length() == 0 ? 0 : Byte.decode(value);
		} else if (type == Byte.class) {
			convertedValue = value.length() == 0 ? null : Byte.decode(value);
		} else if (type == Date.class) {
			// TODO date parser
		}
		return convertedValue;
	}
}
