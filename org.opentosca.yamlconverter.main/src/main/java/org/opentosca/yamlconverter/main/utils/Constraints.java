package org.opentosca.yamlconverter.main.utils;

import java.util.Map;

public class Constraints {

	enum ConstraintOperator {
		equal(ConstraintOperatorType.scalar),
		greater_than(ConstraintOperatorType.scalar),
		greater_or_equal(ConstraintOperatorType.scalar),
		less_than(ConstraintOperatorType.scalar),
		less_or_equal(ConstraintOperatorType.scalar),
		in_range(ConstraintOperatorType.dualScalar),
		valid_values(ConstraintOperatorType.list),
		length(ConstraintOperatorType.scalar),
		min_length(ConstraintOperatorType.scalar),
		max_length(ConstraintOperatorType.scalar),
		pattern(ConstraintOperatorType.regex);

		private final ConstraintOperatorType type;

		ConstraintOperator(ConstraintOperatorType type) {
			this.type = type;
		}

		public ConstraintOperatorType getType() {
			return this.type;
		}

		public static ConstraintOperator toOperator(String operator) {
			return Enum.valueOf(ConstraintOperator.class, operator);
		}

		public static boolean isValid(Map<String, String> constraints) {
			return false;
		}
	}

	enum ConstraintOperatorType {
		scalar,
		dualScalar,
		list,
		regex;
	}

}
