package org.opentosca.yamlconverter.yamlmodel.yaml.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeTemplate extends YAMLElement {

	private String type = "";
	private Map<String, Object> properties = new HashMap<String, Object>();
	private List<Map<String, Object>> requirements = new ArrayList<Map<String, Object>>();
	private Map<String, Object> capabilities = new HashMap<String, Object>();
	private Map<String, String> interfaces = new HashMap<String, String>();
	private List<Map<String, Object>> artifacts = new ArrayList<Map<String, Object>>();

	public void setType(String type) {
		if (type != null) {
			this.type = type;
		}
	}

	public String getType() {
		return this.type;
	}

	public void setProperties(Map<String, Object> properties) {
		if (properties != null) {
			this.properties = properties;
		}
	}

	public Map<String, Object> getProperties() {
		return this.properties;
	}

	public void setRequirements(List<Map<String, Object>> requirements) {
		if (requirements != null) {
			this.requirements = requirements;
		}
	}

	public List<Map<String, Object>> getRequirements() {
		return this.requirements;
	}

	public void setCapabilities(Map<String, Object> capabilities) {
		if (capabilities != null) {
			this.capabilities = capabilities;
		}
	}

	public Map<String, Object> getCapabilities() {
		return this.capabilities;
	}

	public Map<String, String> getInterfaces() {
		return this.interfaces;
	}

	public void setInterfaces(Map<String, String> interfaces) {
		if (interfaces != null) {
			this.interfaces = interfaces;
		}
	}

	public List<Map<String, Object>> getArtifacts() {
		return this.artifacts;
	}

	public void setArtifacts(List<Map<String, Object>> artifacts) {
		if (artifacts != null) {
			this.artifacts = artifacts;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		NodeTemplate that = (NodeTemplate) o;

		if (!artifacts.equals(that.artifacts)) return false;
		if (!capabilities.equals(that.capabilities)) return false;
		if (!interfaces.equals(that.interfaces)) return false;
		if (!properties.equals(that.properties)) return false;
		if (!requirements.equals(that.requirements)) return false;
		if (!type.equals(that.type)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + type.hashCode();
		result = 31 * result + properties.hashCode();
		result = 31 * result + requirements.hashCode();
		result = 31 * result + capabilities.hashCode();
		result = 31 * result + interfaces.hashCode();
		result = 31 * result + artifacts.hashCode();
		return result;
	}
}