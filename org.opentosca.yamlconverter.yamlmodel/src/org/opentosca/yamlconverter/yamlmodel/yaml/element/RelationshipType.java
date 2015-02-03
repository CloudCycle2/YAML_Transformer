package org.opentosca.yamlconverter.yamlmodel.yaml.element;

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
		this.properties = properties;
	}

	public Map<String, String> getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(Map<String, String> interfaces) {
		this.interfaces = interfaces;
	}

	public String[] getTargets() {
		return targets;
	}

	public void setTargets(String[] targets) {
		this.targets = targets;
	}

	public int hashCode() {
		int hashCode = 0;
		if ( hashCode == 0 ) {
			hashCode = super.hashCode();
		}
		return hashCode;
	}

	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (object instanceof RelationshipType) {
			RelationshipType relationshipTypeObject = (RelationshipType) object;
			boolean equals = true;
			return equals;
		}
		return false;
	}
}