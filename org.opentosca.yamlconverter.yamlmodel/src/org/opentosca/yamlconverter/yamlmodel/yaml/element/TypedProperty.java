package org.opentosca.yamlconverter.yamlmodel.yaml.element;

/**
 * A.3.9
 */
@Deprecated
public class TypedProperty {
	private TypedProperty default_1;
	private PropertyType type;

	public int hashCode() {
		int hashCode = 0;
		if ( this.default_1 != null ) {
			hashCode += this.default_1.hashCode();
		}
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
		} else if (object instanceof TypedProperty) {
			TypedProperty typedPropertyObject = (TypedProperty) object;
			boolean equals = true;
			equals &= ((this.default_1 == typedPropertyObject.default_1)
				|| (this.default_1 != null && this.default_1.equals(typedPropertyObject.default_1)));
			equals &= ((this.type == typedPropertyObject.type)
				|| (this.type != null && this.type.equals(typedPropertyObject.type)));
			return equals;
		}
		return false;
	}
}