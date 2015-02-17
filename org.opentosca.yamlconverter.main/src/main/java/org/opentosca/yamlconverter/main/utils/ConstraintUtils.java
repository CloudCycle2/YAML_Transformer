package org.opentosca.yamlconverter.main.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.opentosca.yamlconverter.yamlmodel.yaml.element.Input;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.PropertyType;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints.ConstraintClause;

/**
 * This class helps you to handle Constraints.
 * 
 * @author Jonas Heinisch
 *
 */
public class ConstraintUtils {

	/**
	 * The default format of a date. TODO: What is the default format used in the spec?
	 */
	private static final String DATEFORMAT = "EEE MMM d HH:mm:ss zzz yyyy";

	/**
	 * Converts constraints from yamlbeans to a {@link List} of {@link ConstraintClause}.
	 * 
	 * @param constraintsListMap The list/map from yamlbeans.
	 * @param dataType The expected datatype.
	 * @return A list of {@link ConstraintClause}.
	 */
	public static List<ConstraintClause<Object>> toConstraints(List<Map<String, Object>> constraintsListMap, String dataType) {
		final List<ConstraintClause<Object>> constraints = new ArrayList<ConstraintClause<Object>>();
		for (final Map<String, Object> constraint : constraintsListMap) {
			final Class<?> dataTypeCls = PropertyType.get(dataType.toLowerCase());
			final ConstraintClause<Object> constraintClause = ConstraintClause.toConstraintClause(constraint, dataTypeCls);
			constraints.add(constraintClause);
		}
		return constraints;
	}

	/**
	 * Converts the constraints of the {@link Input} to a {@link List} of {@link ConstraintClause}.
	 * 
	 * @param input The defined {@link Input}.
	 * @return The {@link List} of {@link ConstraintClause}.
	 */
	public static List<ConstraintClause<Object>> toConstraints(Input input) {
		return toConstraints(input.getConstraints(), input.getType());
	}

	/**
	 * Checks if an userinput matches the Constraints of an {@link Input}. Including typecheck.
	 * 
	 * @param userinput The input made by the user.
	 * @param input The required {@link Input}.
	 * @return Is the userinput matching the constraints?
	 */
	public static boolean matchesConstraints(String userinput, Input input) {
		final Object value = convertToType(userinput, input.getType());
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
	 * @param type The type to convert to. (Default: String)
	 * @return The converted value.
	 */
	public static Object convertToType(String string, String type) {
		if (type == null) {
			return string;
		}
		try {
			switch (type.toLowerCase()) {
			case "int":
			case "integer":
				return Integer.parseInt(string);
			case "double":
				return Double.parseDouble(string);
			case "float":
				return Float.parseFloat(string);
			case "long":
				return Long.parseLong(string);
			case "timestamp":
				return new SimpleDateFormat(DATEFORMAT).parse(string);
			case "boolean":
				return Boolean.parseBoolean(string);
			case "string":
			case "text":
				return string;
			}
		} catch (final Exception e) {
			// empty
		}
		return null;
	}
}
