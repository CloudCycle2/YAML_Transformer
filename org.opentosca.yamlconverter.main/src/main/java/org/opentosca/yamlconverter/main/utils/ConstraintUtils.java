package org.opentosca.yamlconverter.main.utils;

import java.util.Date;
import java.util.List;

import org.opentosca.yamlconverter.constraints.ConstraintClause;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.Input;

/**
 * This class helps you to handle Constraints.
 *
 * @author Jonas Heinisch
 *
 */
public class ConstraintUtils {
	/**
	 * Converts the constraints of the {@link Input} to a {@link List} of {@link ConstraintClause}.
	 *
	 * @param input The defined {@link Input}.
	 * @return The {@link List} of {@link ConstraintClause}.
	 */
	public static List<ConstraintClause<Object>> toConstraints(Input input) {
		final Class<?> dataTypeCls = PropertyType.get(input.getType());
		return ConstraintClause.toConstraints(input.getConstraints(), dataTypeCls);
	}

	/**
	 * Checks if an userinput matches the Constraints of an {@link Input}. Including typecheck.
	 *
	 * @param userinput The input made by the user.
	 * @param input The required {@link Input}.
	 * @return Is the userinput matching the constraints?
	 */
	public static boolean matchesConstraints(String userinput, Input input) {
		final Object value = convert(userinput, input.getType());
		if (value == null) {
			return false;
		}
		boolean valid = true;
		for (final ConstraintClause<Object> constraint : toConstraints(input)) {
			if (!constraint.isValid(value)) {
				valid = false;
			}
		}
		return valid;
	}

	/**
	 * Converts a string to a specific type. Returns null if conversion fails.
	 *
	 * @param string The string to convert.
	 * @param type The type to convert to. See {@link PropertyType} for valid values.
	 * @return The converted value.
	 */
	public static Object convert(String representedValue, String type) {
		return convert(representedValue, PropertyType.get(type.toLowerCase()));
	}

	/**
	 * Converts a string to a specific type. Returns null if conversion fails.
	 *
	 * @param string The string to convert.
	 * @param type The type to convert to.
	 * @return The converted value.
	 */
	public static Object convert(String representedValue, Class<?> type) {
		try {
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
				convertedValue = YamlTimestampFormatter.parse(representedValue).getTime();
			}
			return convertedValue;
		} catch (final Exception e) {
		}
		return null;
	}
}
