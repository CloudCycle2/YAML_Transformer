package org.opentosca.yamlconverter.yamlmodel.yaml.element;

import java.util.Map;

public class NodeTemplate extends YAMLElement {
	private String type;
	private Map<String, String> properties;
	private Map<String, String> requirements;
	private Map<String, String> capabilities;
	private Map<String, String> interfaces;
	private Map<String, String> artifacts;

	@Override
	public int hashCode() {
		int hashCode = 0;
		if (this.type != null) {
			hashCode += this.type.hashCode();
		}
		if (hashCode == 0) {
			hashCode = super.hashCode();
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (object instanceof NodeTemplate) {
			final NodeTemplate nodeTemplateObject = (NodeTemplate) object;
			boolean equals = true;
			equals &= this.type == nodeTemplateObject.type || this.type != null && this.type.equals(nodeTemplateObject.type);
			equals &= this.properties == nodeTemplateObject.properties;
			equals &= this.requirements == nodeTemplateObject.requirements;
			equals &= this.capabilities == nodeTemplateObject.capabilities;
			equals &= this.interfaces == nodeTemplateObject.interfaces;
			equals &= this.artifacts == nodeTemplateObject.artifacts;
			return equals;
		}
		return false;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, String> getProperties() {
		return this.properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public Map<String, String> getRequirements() {
		return this.requirements;
	}

	public void setRequirements(Map<String, String> requirements) {
		this.requirements = requirements;
	}

	public Map<String, String> getCapabilities() {
		return this.capabilities;
	}

	public void setCapabilities(Map<String, String> capabilities) {
		this.capabilities = capabilities;
	}

	public Map<String, String> getInterfaces() {
		return this.interfaces;
	}

	public void setInterfaces(Map<String, String> interfaces) {
		this.interfaces = interfaces;
	}

	public Map<String, String> getArtifacts() {
		return this.artifacts;
	}

	public void setArtifacts(Map<String, String> artifacts) {
		this.artifacts = artifacts;
	}
}