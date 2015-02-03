package org.opentosca.yamlconverter.yamlmodel.yaml.element;

import java.util.HashMap;
import java.util.Map;

public class ArtifactType extends YAMLElement {

	private String mime_type;
	private String[] file_ext;
	private Map<String, String> properties = new HashMap<String, String>();

	public String getMime_type() {
		return mime_type;
	}

	public void setMime_type(String mime_type) {
		this.mime_type = mime_type;
	}

	public String[] getFile_ext() {
		return file_ext;
	}

	public void setFile_ext(String[] file_ext) {
		this.file_ext = file_ext;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
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
		} else if (object instanceof ArtifactType) {
			ArtifactType artifactTypeObject = (ArtifactType) object;
			boolean equals = true;
			return equals;
		}
		return false;
	}
}