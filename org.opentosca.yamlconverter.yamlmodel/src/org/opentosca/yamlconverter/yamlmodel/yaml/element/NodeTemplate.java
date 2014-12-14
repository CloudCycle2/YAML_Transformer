package org.opentosca.yamlconverter.yamlmodel.yaml.element;

import org.opentosca.yamlconverter.yamlmodel.tosca.nodes.Root;

import java.util.ArrayList;

public class NodeTemplate extends YAMLElement {

	private String name;
	private ArrayList<Root> root = new ArrayList<Root>();

	public NodeTemplate() {
		this.name = "";
		this.root = new ArrayList<>();
	}

	public ArrayList<Root> getRoot() {
		return root;
	}

	public void setRoot(ArrayList<Root> root) {
		if (root != null) {
			this.root = root;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name != null) {
			this.name = name;
		}
	}
}