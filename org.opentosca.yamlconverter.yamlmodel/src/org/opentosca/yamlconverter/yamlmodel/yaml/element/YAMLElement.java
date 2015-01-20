package org.opentosca.yamlconverter.yamlmodel.yaml.element;

public abstract class YAMLElement {
	private String description;

	@Override
	public int hashCode() {
		int hashCode = 0;
		if (this.description != null) {
			hashCode += this.description.hashCode();
		}
		if (hashCode == 0) {
			hashCode = super.hashCode();
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (object instanceof YAMLElement) {
			final YAMLElement YAMLElementObject = (YAMLElement) object;
			boolean equals = true;
			equals &= this.description == YAMLElementObject.description || this.description != null
					&& this.description.equals(YAMLElementObject.description);
			return equals;
		}
		return false;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}