package org.opentosca.yamlconverter.yamlmodel.yaml.element;

public class Output extends YAMLElement {

	private Object value;

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
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
		} else if (object instanceof Output) {
			Output outputObject = (Output) object;
			boolean equals = true;
			return equals;
		}
		return false;
	}
}