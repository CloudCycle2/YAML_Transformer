package yaml.element;

public class NodeType extends YAMLElement {
	private String derived_from;
	private Map<String, String> properties;
	private Map<String, String> requirements;
	private Map<String, String> capabilities;
	private Map<String, String> interfaces;
	private Map<String, String> artifacts;

	public int hashCode() {
		int hashCode = 0;
		if ( this.derived_from != null ) {
			hashCode += this.derived_from.hashCode();
		}
		if ( hashCode == 0 ) {
			hashCode = super.hashCode();
		}
		return hashCode;
	}

	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (object instanceof NodeType) {
			NodeType nodeTypeObject = (NodeType) object;
			boolean equals = true;
			equals &= ((this.derived_from == nodeTypeObject.derived_from)
				|| (this.derived_from != null && this.derived_from.equals(nodeTypeObject.derived_from)));
			equals &= this.properties == nodeTypeObject.properties;
			equals &= this.requirements == nodeTypeObject.requirements;
			equals &= this.capabilities == nodeTypeObject.capabilities;
			equals &= this.interfaces == nodeTypeObject.interfaces;
			equals &= this.artifacts == nodeTypeObject.artifacts;
			return equals;
		}
		return false;
	}
}