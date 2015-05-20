package org.opentosca.yamlconverter.constraints;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.opentosca.yamlconverter.main.utils.ConstraintUtils;

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

	/**
	 * Converts constraints from yamlbeans to a {@link List} of {@link ConstraintClause}.
	 *
	 * @param constraintsListMap The list/map from yamlbeans.
	 * @param dataType The expected datatype.
	 * @return A list of {@link ConstraintClause}.
	 */
	public static List<ConstraintClause<Object>> toConstraints(List<Map<String, Object>> constraintsListMap, Class<?> dataTypeCls) {
		final List<ConstraintClause<Object>> constraints = new ArrayList<ConstraintClause<Object>>();
		for (final Map<String, Object> constraint : constraintsListMap) {
			final ConstraintClause<Object> constraintClause = toConstraintClause(constraint, dataTypeCls);
			constraints.add(constraintClause);
		}
		return constraints;
	}

	/**
	 * Creates a {@link ConstraintClause} with the give dataType.
	 * 
	 * @param dataType The data type of the {@link ConstraintClause}
	 */
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
		return ConstraintUtils.convert(representedValue, this.dataType);
	}

}
