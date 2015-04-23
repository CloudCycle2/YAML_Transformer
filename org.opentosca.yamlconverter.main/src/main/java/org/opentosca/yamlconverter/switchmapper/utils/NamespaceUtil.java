package org.opentosca.yamlconverter.switchmapper.utils;

import javax.xml.namespace.QName;

import org.opentosca.yamlconverter.switchmapper.Yaml2XmlSwitch;

/**
 * @author Sebi
 */
public class NamespaceUtil {

	private final String targetNamespace;

	public NamespaceUtil(String targetNamespace) {
		if (targetNamespace == null || targetNamespace.isEmpty()) {
			throw new IllegalArgumentException("target namespace may not be null or empty.");
		}
		this.targetNamespace = targetNamespace;
	}

	/**
	 * Creates a QName element for the given local name with the pre-defined target namespace
	 *
	 * @param localName of the element
	 * @return the QName
	 */
	public QName toTnsQName(String localName) {
		return new QName(this.targetNamespace, localName, Yaml2XmlSwitch.TARGET_NS_PREFIX);
	}

	/**
	 * Creates a QName element for the given local name with target namespace for base types
	 * {@link org.opentosca.yamlconverter.switchmapper.Yaml2XmlSwitch#BASE_TYPES_NS}
	 * 
	 * @param localName local name of the element to reference
	 * @return QName object
	 */
	public QName toBaseTypesNsQName(String localName) {
		return new QName(Yaml2XmlSwitch.BASE_TYPES_NS, localName, Yaml2XmlSwitch.BASE_TYPES_PREFIX);
	}

	/**
	 * Creates a QName element for the given local name with target namespace for specific types
	 * {@link org.opentosca.yamlconverter.switchmapper.Yaml2XmlSwitch#SPECIFIC_TYPES_NS}
	 * 
	 * @param localName local name of the element to reference
	 * @return QName object
	 */
	public QName toSpecificTypesNsQName(String localName) {
		return new QName(Yaml2XmlSwitch.SPECIFIC_TYPES_NS, localName, Yaml2XmlSwitch.SPECIFIC_TYPES_PREFIX);
	}
}
