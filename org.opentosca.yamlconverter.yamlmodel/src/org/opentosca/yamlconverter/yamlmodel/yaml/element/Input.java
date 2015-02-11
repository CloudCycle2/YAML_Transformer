package org.opentosca.yamlconverter.yamlmodel.yaml.element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Input extends YAMLElement {

	private String type = "";
	private String defaultValue = "";
	private List<Map<String, Object>> constraints = new ArrayList<Map<String, Object>>();

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		if (type != null) {
			this.type = type;
		}
	}

	public String getDefault() {
		return this.defaultValue;
	}

	public void setDefault(String defaultValue) {
		if (defaultValue != null) {
			this.defaultValue = defaultValue;
		}
	}

	public List<Map<String, Object>> getConstraints() {
		return this.constraints;
	}

	public void setConstraints(List<Map<String, Object>> constraints) {
		if (constraints != null) {
			this.constraints = constraints;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}

		final Input input = (Input) o;

		if (!this.constraints.equals(input.constraints)) {
			return false;
		}
		if (!this.defaultValue.equals(input.defaultValue)) {
			return false;
		}
		if (!this.type.equals(input.type)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + this.type.hashCode();
		result = 31 * result + this.defaultValue.hashCode();
		result = 31 * result + this.constraints.hashCode();
		return result;
	}
}