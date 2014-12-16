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
}