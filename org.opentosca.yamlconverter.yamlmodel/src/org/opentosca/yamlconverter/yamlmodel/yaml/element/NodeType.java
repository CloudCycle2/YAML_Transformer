package org.opentosca.yamlconverter.yamlmodel.yaml.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeType extends YAMLElement {

	private String derived_from = "";
	private Map<String, PropertyDefinition> properties = new HashMap<String, PropertyDefinition>();
	private List<Map<String, Object>> requirements = new ArrayList<Map<String, Object>>();
	private Map<String, Object> capabilities = new HashMap<String, Object>();
	private Map<String, Map<String, Map<String, String>>> interfaces =
			new HashMap<String, Map<String, Map<String, String>>>();
	private List<Map<String, Object>> artifacts = new ArrayList<Map<String, Object>>();


	public void setArtifacts(List<Map<String, Object>> artifacts) {
		if (artifacts != null) {
			this.artifacts = artifacts;
		}
	}

	public List<Map<String, Object>> getArtifacts() {
		return this.artifacts;
	}

	public String getDerived_from() {
		return this.derived_from;
	}

	public void setDerived_from(String derived_from) {
		if (derived_from != null) {
			this.derived_from = derived_from;
		}
	}

	public Map<String, PropertyDefinition> getProperties() {
		return this.properties;
	}

	public void setProperties(Map<String, PropertyDefinition> properties) {
		if (properties != null) {
			this.properties = properties;
		}
	}

	public List<Map<String, Object>> getRequirements() {
		return this.requirements;
	}

	public void setRequirements(List<Map<String, Object>> requirements) {
		if (requirements != null) {
			this.requirements = requirements;
		}
	}

	public Map<String, Object> getCapabilities() {
		return this.capabilities;
	}

	public void setCapabilities(Map<String, Object> capabilities) {
		if (capabilities != null) {
			this.capabilities = capabilities;
		}
	}

	public Map<String, Map<String, Map<String, String>>> getInterfaces() {
		return this.interfaces;
	}

	public void setInterfaces(Map<String, Map<String, Map<String, String>>> interfaces) {
		if (interfaces != null) {
			this.interfaces = interfaces;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		NodeType nodeType = (NodeType) o;

		if (!artifacts.equals(nodeType.artifacts)) return false;
		if (!capabilities.equals(nodeType.capabilities)) return false;
		if (!derived_from.equals(nodeType.derived_from)) return false;
		if (!interfaces.equals(nodeType.interfaces)) return false;
		if (!properties.equals(nodeType.properties)) return false;
		if (!requirements.equals(nodeType.requirements)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + derived_from.hashCode();
		result = 31 * result + properties.hashCode();
		result = 31 * result + requirements.hashCode();
		result = 31 * result + capabilities.hashCode();
		result = 31 * result + interfaces.hashCode();
		result = 31 * result + artifacts.hashCode();
		return result;
	}
}