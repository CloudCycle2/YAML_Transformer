package org.opentosca.yamlconverter.yamlmodel.yaml.element;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RelationshipType extends YAMLElement {

	private Map<String, PropertyDefinition> properties = new HashMap<String, PropertyDefinition>();
	private Map<String, Map<String, InterfaceDefinition>> interfaces = new HashMap<String, Map<String, InterfaceDefinition>>();
	private String[] valid_targets;

	public Map<String, PropertyDefinition> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, PropertyDefinition> properties) {
		if (properties != null) {
			this.properties = properties;
		}
	}

	public Map<String, Map<String, InterfaceDefinition>> getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(Map<String, Map<String, InterfaceDefinition>> interfaces) {
		if (interfaces != null) {
			this.interfaces = interfaces;
		}
	}

	public String[] getValid_targets() {
		return valid_targets;
	}

	public void setValid_targets(String[] valid_targets) {
		if (valid_targets != null) {
			this.valid_targets = valid_targets;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		RelationshipType that = (RelationshipType) o;

		if (!interfaces.equals(that.interfaces)) return false;
		if (!properties.equals(that.properties)) return false;
		if (!Arrays.equals(valid_targets, that.valid_targets)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + properties.hashCode();
		result = 31 * result + interfaces.hashCode();
		result = 31 * result + (valid_targets != null ? Arrays.hashCode(valid_targets) : 0);
		return result;
	}
}