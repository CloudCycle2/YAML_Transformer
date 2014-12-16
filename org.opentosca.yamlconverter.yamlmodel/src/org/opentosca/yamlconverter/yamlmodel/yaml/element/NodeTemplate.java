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
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, Object> getProperties() {
		return this.properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof NodeTemplate)) {
			return false;
		}
		final NodeTemplate other = (NodeTemplate) obj;
		if (this.properties == null) {
			if (other.properties != null) {
				return false;
			}
		} else {
			if (!this.properties.keySet().equals(other.properties.keySet())) {
				return false;
			}
			for (final String key : this.properties.keySet()) {
				if (!this.properties.get(key).toString().equals(other.properties.get(key).toString())) {
					return false;
				}
			}
		}
		if (this.type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!this.type.equals(other.type)) {
			return false;
		}
		return true;
	}
}