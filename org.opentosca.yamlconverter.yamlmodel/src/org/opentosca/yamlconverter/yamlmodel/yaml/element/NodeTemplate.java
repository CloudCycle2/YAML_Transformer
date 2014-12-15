package org.opentosca.yamlconverter.yamlmodel.yaml.element;

import java.util.HashMap;
import java.util.Map;

public class NodeTemplate extends YAMLElement {

	private String type;
	private Map<String, Object> properties = new HashMap<>();

	public NodeTemplate() {
		this.type = "";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
}