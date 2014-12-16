package org.opentosca.yamlconverter.yamlmodel.yaml.element;

public class TypedProperty extends YAMLElement {

	// TODO: check if it's really default_3 or just a typo?!
	private TypedProperty default_3;
	private PropertyType type;

	public TypedProperty() {

	}

	public TypedProperty getDefault_3() {
		return this.default_3;
	}

	public void setDefault_3(TypedProperty default_3) {
		if (default_3 != null) {
			this.default_3 = default_3;
		}
	}

	public PropertyType getType() {
		return this.type;
	}

	public void setType(PropertyType type) {
		if (type != null) {
			this.type = type;
		}
	}
}