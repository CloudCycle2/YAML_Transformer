package org.opentosca.yamlconverter.yamlmodel.yaml.element;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ArtifactType extends YAMLElement {

	private String implementation = "";
	private String mime_type = "";
	private String[] file_ext;
	private Map<String, PropertyDefinition> properties = new HashMap<String, PropertyDefinition>();

	public String getImplementation() {
		return implementation;
	}

	public void setImplementation(String implementation) {
		if (implementation != null) {
			this.implementation = implementation;
		}
	}

	public String getMime_type() {
		return mime_type;
	}

	public void setMime_type(String mime_type) {
		if (mime_type != null) {
			this.mime_type = mime_type;
		}
	}

	public String[] getFile_ext() {
		return file_ext;
	}

	public void setFile_ext(String[] file_ext) {
		if (file_ext != null) {
			this.file_ext = file_ext;
		}
	}

	public Map<String, PropertyDefinition> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, PropertyDefinition> properties) {
		if (properties != null) {
			this.properties = properties;
		}
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		ArtifactType that = (ArtifactType) o;

		if (!Arrays.equals(file_ext, that.file_ext)) return false;
		if (!implementation.equals(that.implementation)) return false;
		if (!mime_type.equals(that.mime_type)) return false;
		if (!properties.equals(that.properties)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + implementation.hashCode();
		result = 31 * result + mime_type.hashCode();
		result = 31 * result + (file_ext != null ? Arrays.hashCode(file_ext) : 0);
		result = 31 * result + properties.hashCode();
		return result;
	}
}