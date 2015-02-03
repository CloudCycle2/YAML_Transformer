package org.opentosca.yamlconverter.yamlmodel.yaml.element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Input extends YAMLElement {

	private String type = "";
	private String defaultValue = "%%USER_INPUT_REQUIRED%%";
	private List<Map<String, String>> constraints = new ArrayList<Map<String, String>>();

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDefault() {
		return defaultValue;
	}

	public void setDefault(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public List<Map<String, String>> getConstraints() {
		return constraints;
	}

	public void setConstraints(List<Map<String, String>> constraints) {
		this.constraints = constraints;
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
		} else if (object instanceof Input) {
			Input inputObject = (Input) object;
			boolean equals = true;
			return equals;
		}
		return false;
	}
}