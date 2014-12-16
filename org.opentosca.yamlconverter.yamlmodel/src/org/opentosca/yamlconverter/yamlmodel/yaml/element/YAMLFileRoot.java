package org.opentosca.yamlconverter.yamlmodel.yaml.element;

import java.util.HashMap;
import java.util.Map;

public class YAMLFileRoot extends YAMLElement {

	private String tosca_definitions_version;
	private Map<String, NodeTemplate> node_templates = new HashMap<>();

	public YAMLFileRoot() {
		this.tosca_definitions_version = "";
	}

	public String getTosca_definitions_version() {
		return this.tosca_definitions_version;
	}

	public void setTosca_definitions_version(String tosca_definitions_version) {
		if (tosca_definitions_version != null) {
			this.tosca_definitions_version = tosca_definitions_version;
		}
	}

	public Map<String, NodeTemplate> getNode_templates() {
		return this.node_templates;
	}

	public void setNode_templates(Map<String, NodeTemplate> node_templates) {
		if (node_templates != null) {
			this.node_templates = node_templates;
		}
	}

	@Override
	public String toString() {
		return "YAMLFileRoot{" + "tosca_definitions_version='" + this.tosca_definitions_version + '\'' + ", node_templates="
				+ this.node_templates + '}';
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			if (obj instanceof YAMLFileRoot) {
				final YAMLFileRoot other = (YAMLFileRoot) obj;
				if (this.tosca_definitions_version != null) {
					if (!this.tosca_definitions_version.equals(other.tosca_definitions_version)) {
						return false;
					}
				} else if (other.tosca_definitions_version != null) {
					return false;
				}
				if (this.node_templates != null) {
					if (!this.node_templates.keySet().equals(other.node_templates.keySet())) {
						return false;
					}
					for (final String key : this.node_templates.keySet()) {
						if (!this.node_templates.get(key).equals(other.node_templates.get(key))) {
							return false;
						}
					}
					return true;
				} else if (other.node_templates != null) {
					return false;
				}
			}
		}
		return false;
	}
}