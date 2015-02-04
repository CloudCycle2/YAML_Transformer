package org.opentosca.yamlconverter.yamlmodel.yaml.element;

import java.util.HashMap;
import java.util.Map;

public class NodeTemplate extends YAMLElement {

	private String type = "";
	private Map<String, Object> properties = new HashMap<String, Object>();
	private Map<String, String> requirements = new HashMap<String, String>();
	private Map<String, String> capabilities = new HashMap<String, String>();
	private Map<String, String> interfaces = new HashMap<String, String>();
	private Map<String, String> artifacts = new HashMap<String, String>();


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

	public void setRequirements(Map<String, String> requirements) {
		if (requirements != null) {
			this.requirements = requirements;
		}
	}

	public Map<String, String> getRequirements() {
		return this.requirements;
	}

	public void setCapabilities(Map<String, String> capabilities) {
		if (capabilities != null) {
			this.capabilities = capabilities;
		}
	}

	public Map<String, String> getCapabilities() {
		return this.capabilities;
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