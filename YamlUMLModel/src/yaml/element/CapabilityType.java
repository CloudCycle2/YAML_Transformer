package yaml.element;

public class CapabilityType extends YAMLElement {

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
		} else if (object instanceof CapabilityType) {
			CapabilityType capabilityTypeObject = (CapabilityType) object;
			boolean equals = true;
			return equals;
		}
		return false;
	}
}