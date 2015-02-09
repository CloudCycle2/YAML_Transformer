package org.opentosca.yamlconverter.yamlmodel.yaml.element;

public abstract class YAMLElement {

	private String description = "";

	public void setDescription(String description) {
		if (description != null) {
			this.description = description;
		}
	}

	public String getDescription() {
		return this.description;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		YAMLElement element = (YAMLElement) o;

		if (!description.equals(element.description)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return description.hashCode();
	}
}