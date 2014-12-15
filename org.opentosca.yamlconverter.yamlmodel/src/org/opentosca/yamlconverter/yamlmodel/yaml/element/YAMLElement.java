package org.opentosca.yamlconverter.yamlmodel.yaml.element;

public abstract class YAMLElement {

	private String description;

	public YAMLElement() {
		this.description = "";
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if (description != null) {
			this.description = description;
		}
	}
}