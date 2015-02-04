package org.opentosca.yamlconverter.yamlmodel.yaml.element;

import java.util.HashMap;
import java.util.Map;

public class NodeType extends YAMLElement {

	private String derived_from = "";
	private Map<String, Map<String, String>> properties = new HashMap<String, Map<String, String>>();
	private Map<String, String> requirements = new HashMap<String, String>();
	private Map<String, String> capabilities = new HashMap<String, String>();
	private Map<String, String> interfaces = new HashMap<String, String>();
	private Map<String, String> artifacts = new HashMap<String, String>();


	public void setArtifacts(Map<String, String> artifacts) {
		if (artifacts != null) {
			this.artifacts = artifacts;
		}
	}

	public Map<String, String> getArtifacts() {
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

	public Map<String, Map<String, String>> getProperties() {
		return this.properties;
	}

	public void setProperties(Map<String, Map<String, String>> properties) {
		if (properties != null) {
			this.properties = properties;
		}
	}

	public Map<String, String> getRequirements() {
		return this.requirements;
	}

	public void setRequirements(Map<String, String> requirements) {
		if (requirements != null) {
			this.requirements = requirements;
		}
	}

	public Map<String, String> getCapabilities() {
		return this.capabilities;
	}

	public void setCapabilities(Map<String, String> capabilities) {
		if (capabilities != null) {
			this.capabilities = capabilities;
		}
	}

	public Map<String, String> getInterfaces() {
		return this.interfaces;
	}

	public void setInterfaces(Map<String, String> interfaces) {
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