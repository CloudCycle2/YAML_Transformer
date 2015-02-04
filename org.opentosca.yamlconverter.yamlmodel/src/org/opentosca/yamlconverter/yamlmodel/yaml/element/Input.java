package org.opentosca.yamlconverter.yamlmodel.yaml.element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Input extends YAMLElement {

	private String type = "";
	private String defaultValue = "";
	private List<Map<String, String>> constraints = new ArrayList<Map<String, String>>();

	public String getType() {
		return type;
	}

	public void setType(String type) {
		if (type != null) {
			this.type = type;
		}
	}

	public String getDefault() {
		return defaultValue;
	}

	public void setDefault(String defaultValue) {
		if (defaultValue != null) {
			this.defaultValue = defaultValue;
		}
	}

	public List<Map<String, String>> getConstraints() {
		return constraints;
	}

	public void setConstraints(List<Map<String, String>> constraints) {
		if (constraints != null) {
			this.constraints = constraints;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		Input input = (Input) o;

		if (!constraints.equals(input.constraints)) return false;
		if (!defaultValue.equals(input.defaultValue)) return false;
		if (!type.equals(input.type)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + type.hashCode();
		result = 31 * result + defaultValue.hashCode();
		result = 31 * result + constraints.hashCode();
		return result;
	}
}