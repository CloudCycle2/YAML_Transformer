package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;

/**
 * Represents a single constraint clause. Must be initialized with the factory method {@link #toConstraintClause(Map, Class)}.
 *
 * @param <T> type of values to check against
 */
public abstract class ConstraintClause<T> {
	private final Class<?> dataType;

	/**
	 * Creates a constraint clause. Input should be a map with one single entry. The key of this entry is the constraint operator in string
	 * representation. The value of this entry is the constraint argument. The constraint argument can be a scalar value, a dual scalar
	 * value, a list or a regex.
	 *
	 * @param input key = constraint operator; value = constraint argument
	 * @param dataType the data type of the constraint. Can be any primitive or {@link Date}
	 * @return the specific constraint clause implementation
	 */
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

	public static Map<String, Object> toMap(ConstraintClause<?> input) {
		// TODO is it even needed?
		throw new UnsupportedOperationException("Not yet implemented");
	}

	public ConstraintClause(Class<?> dataType) {
		this.dataType = dataType;
	}

	/**
	 * Checks whether the given value is valid in terms of this constraint.
	 *
	 * @param value the value for checking its validity
	 * @return whether this constraint accepts the given value.
	 */
	public abstract boolean isValid(T value);

	/**
	 * Converts the given string representation of a value into its data type specific value
	 *
	 * @param representedValue String representation of a value to convert to a data type specific value
	 * @return the given string converted into its respective data type.
	 */
	protected Object convert(String representedValue) {
		final Class<?> type = this.dataType;
		Object convertedValue = null;
		if (type == String.class) {
			convertedValue = representedValue;
		} else if (type == Integer.TYPE) {
			convertedValue = representedValue.length() == 0 ? 0 : Integer.decode(representedValue);
		} else if (type == Integer.class) {
			convertedValue = representedValue.length() == 0 ? null : Integer.decode(representedValue);
		} else if (type == Boolean.TYPE) {
			convertedValue = representedValue.length() == 0 ? false : Boolean.valueOf(representedValue);
		} else if (type == Boolean.class) {
			convertedValue = representedValue.length() == 0 ? null : Boolean.valueOf(representedValue);
		} else if (type == Float.TYPE) {
			convertedValue = representedValue.length() == 0 ? 0 : Float.valueOf(representedValue);
		} else if (type == Float.class) {
			convertedValue = representedValue.length() == 0 ? null : Float.valueOf(representedValue);
		} else if (type == Double.TYPE) {
			convertedValue = representedValue.length() == 0 ? 0 : Double.valueOf(representedValue);
		} else if (type == Double.class) {
			convertedValue = representedValue.length() == 0 ? null : Double.valueOf(representedValue);
		} else if (type == Long.TYPE) {
			convertedValue = representedValue.length() == 0 ? 0 : Long.decode(representedValue);
		} else if (type == Long.class) {
			convertedValue = representedValue.length() == 0 ? null : Long.decode(representedValue);
		} else if (type == Short.TYPE) {
			convertedValue = representedValue.length() == 0 ? 0 : Short.decode(representedValue);
		} else if (type == Short.class) {
			convertedValue = representedValue.length() == 0 ? null : Short.decode(representedValue);
		} else if (type == Character.TYPE) {
			convertedValue = representedValue.length() == 0 ? 0 : representedValue.charAt(0);
		} else if (type == Character.class) {
			convertedValue = representedValue.length() == 0 ? null : representedValue.charAt(0);
		} else if (type == Byte.TYPE) {
			convertedValue = representedValue.length() == 0 ? 0 : Byte.decode(representedValue);
		} else if (type == Byte.class) {
			convertedValue = representedValue.length() == 0 ? null : Byte.decode(representedValue);
		} else if (type == Date.class) {
			// TODO date parser
		}
		return convertedValue;
	}
}
