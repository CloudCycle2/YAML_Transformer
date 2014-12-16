package org.opentosca.yamlconverter.yamlmodel.yaml.element;

public abstract class YAMLElement {

	private String description;

	public YAMLElement() {
		this.description = "";
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		if (description != null) {
			this.description = description;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (obj instanceof YAMLElement) {
			final YAMLElement other = (YAMLElement) obj;
			if (this.description == null) {
				return other.description == null;
			} else {
				return this.description.equals(other.description);
			}
		}
		return false;
	}
}