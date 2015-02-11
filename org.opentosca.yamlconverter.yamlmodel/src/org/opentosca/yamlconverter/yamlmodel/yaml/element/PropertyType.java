package org.opentosca.yamlconverter.yamlmodel.yaml.element;

import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Maps yaml primitive types to their respective java classes.
 *
 * @usage {@link PropertyType#get(String)}
 * @example <code>PropertyType.get("integer")</code>
 */
public enum PropertyType {
	stringType("string", String.class),
	integerType("integer", Integer.class),
	floatType("float", Float.class),
	booleanType("boolean", Boolean.class),
	timestampType("timestamp", Date.class);

	private static final Map<String, Class<?>> aliases = new HashMap<String, Class<?>>();
	private final String alias;
	private final Class<?> dataType;

	static {
		for (final PropertyType type : EnumSet.allOf(PropertyType.class)) {
			aliases.put(type.alias, type.dataType);
		}
	}

	PropertyType(String alias, Class<?> dataType) {
		this.alias = alias;
		this.dataType = dataType;
	}

	public static Class<?> get(String alias) {
		return aliases.get(alias);
	}

}