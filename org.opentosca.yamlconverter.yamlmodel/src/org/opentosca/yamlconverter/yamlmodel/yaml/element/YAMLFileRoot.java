package org.opentosca.yamlconverter.yamlmodel.yaml.element;

import java.util.ArrayList;

public class YAMLFileRoot extends YAMLElement {

	private String tosca_definitions_version;
	private ArrayList<NodeTemplate> node_templates = new ArrayList<NodeTemplate>();

	public YAMLFileRoot() {
		this.tosca_definitions_version = "";
		this.node_templates = new ArrayList<>();
	}

	public String getTosca_definitions_version() {
		return tosca_definitions_version;
	}

	public void setTosca_definitions_version(String tosca_definitions_version) {
		if (tosca_definitions_version != null) {
			this.tosca_definitions_version = tosca_definitions_version;
		}
	}

	public ArrayList<NodeTemplate> getNode_templates() {
		return node_templates;
	}

	public void setNode_templates(ArrayList<NodeTemplate> node_templates) {
		if (node_templates != null) {
			this.node_templates = node_templates;
		}
	}
}