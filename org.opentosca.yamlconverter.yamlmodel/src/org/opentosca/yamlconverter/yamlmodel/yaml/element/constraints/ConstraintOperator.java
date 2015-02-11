package org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints;

@SuppressWarnings("rawtypes")
public enum ConstraintOperator {
	equal(Equal.class),
	greater_than(GreaterThan.class),
	greater_or_equal(GreaterOrEqual.class),
	less_than(LessThan.class),
	less_or_equal(LessOrEqual.class),
	in_range(InRange.class),
	valid_values(ValidValues.class),
	length(Length.class),
	min_length(MinLength.class),
	max_length(MaxLength.class),
	pattern(Pattern.class);

	private final Class<? extends ConstraintClause> type;

	ConstraintOperator(Class<? extends ConstraintClause> type) {
		this.type = type;
	}

	public Class<? extends ConstraintClause> getType() {
		return this.type;
	}

	public static ConstraintOperator toOperator(String operator) {
		return Enum.valueOf(ConstraintOperator.class, operator);
	}
}
