package org.opentosca.yamlconverter.yamlmodel.yaml.element;

import java.util.HashMap;
import java.util.Map;

public class CapabilityType extends YAMLElement {

	private Map<String, PropertyDefinition> properties = new HashMap<String, PropertyDefinition>();

	public Map<String, PropertyDefinition> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, PropertyDefinition> properties) {
		this.properties = properties;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		CapabilityType that = (CapabilityType) o;

		if (!properties.equals(that.properties)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + properties.hashCode();
		return result;
	}
}