//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB)
// Reference Implementation, v2.2.4-2
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source
// schema.
// Generated on: 2013.07.10 at 12:45:26 PM CEST
//
// TOSCA version: TOSCA-v1.0-cs02.xsd
//

package org.opentosca.model.tosca;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for tImport complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="tImport">
 *   &lt;complexContent>
 *     &lt;extension base="{http://docs.oasis-open.org/tosca/ns/2011/12}tExtensibleElements">
 *       &lt;attribute name="namespace" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="location" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="importType" use="required" type="{http://docs.oasis-open.org/tosca/ns/2011/12}importedURI" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tImport")
public class TImport extends TExtensibleElements {

	@XmlAttribute(name = "namespace")
	@XmlSchemaType(name = "anyURI")
	protected String namespace;
	@XmlAttribute(name = "location")
	@XmlSchemaType(name = "anyURI")
	protected String location;
	@XmlAttribute(name = "importType", required = true)
	protected String importType;

	/**
	 * Gets the value of the namespace property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getNamespace() {
		return this.namespace;
	}

	/**
	 * Sets the value of the namespace property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setNamespace(String value) {
		this.namespace = value;
	}

	/**
	 * Gets the value of the location property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getLocation() {
		return this.location;
	}

	/**
	 * Sets the value of the location property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setLocation(String value) {
		this.location = value;
	}

	/**
	 * Gets the value of the importType property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getImportType() {
		return this.importType;
	}

	/**
	 * Sets the value of the importType property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setImportType(String value) {
		this.importType = value;
	}

}
