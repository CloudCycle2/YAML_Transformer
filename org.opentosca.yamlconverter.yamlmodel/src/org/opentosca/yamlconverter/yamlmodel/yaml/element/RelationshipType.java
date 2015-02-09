package org.opentosca.yamlconverter.yamlmodel.yaml.element;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RelationshipType extends YAMLElement {

	private Map<String, String> properties = new HashMap<String, String>();
	private Map<String, String> interfaces = new HashMap<String, String>();
	private String[] targets;

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		if (properties != null) {
			this.properties = properties;
		}
	}

	public Map<String, String> getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(Map<String, String> interfaces) {
		if (interfaces != null) {
			this.interfaces = interfaces;
		}
	}

	public String[] getTargets() {
		return targets;
	}

	public void setTargets(String[] targets) {
		if (targets != null) {
			this.targets = targets;
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
		if (!Arrays.equals(targets, that.targets)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + properties.hashCode();
		result = 31 * result + interfaces.hashCode();
		result = 31 * result + (targets != null ? Arrays.hashCode(targets) : 0);
		return result;
	}
}