package yaml.element;

public class NodeTemplate extends YAMLElement {
	private String type;
	private Map<String, String> properties;
	private Map<String, String> requirements;
	private Map<String, String> capabilities;
	private Map<String, String> interfaces;
	private Map<String, String> artifacts;

	public int hashCode() {
		int hashCode = 0;
		if ( this.type != null ) {
			hashCode += this.type.hashCode();
		}
		if ( hashCode == 0 ) {
			hashCode = super.hashCode();
		}
		return hashCode;
	}

	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (object instanceof NodeTemplate) {
			NodeTemplate nodeTemplateObject = (NodeTemplate) object;
			boolean equals = true;
			equals &= ((this.type == nodeTemplateObject.type)
				|| (this.type != null && this.type.equals(nodeTemplateObject.type)));
			equals &= this.properties == nodeTemplateObject.properties;
			equals &= this.requirements == nodeTemplateObject.requirements;
			equals &= this.capabilities == nodeTemplateObject.capabilities;
			equals &= this.interfaces == nodeTemplateObject.interfaces;
			equals &= this.artifacts == nodeTemplateObject.artifacts;
			return equals;
		}
		return false;
	}
}