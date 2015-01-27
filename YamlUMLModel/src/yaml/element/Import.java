package yaml.element;

public class Import extends YAMLElement {

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
		} else if (object instanceof Import) {
			Import importObject = (Import) object;
			boolean equals = true;
			return equals;
		}
		return false;
	}
}