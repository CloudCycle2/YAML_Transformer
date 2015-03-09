package org.opentosca.yamlconverter.main.interfaces;

import org.opentosca.model.tosca.Definitions;

/**
 * This interface defines methods to convert XML beans to XML or vice versa.
 */
public interface IToscaXml2XmlBeanConverter {

	/**
	 * Converts Tosca XML beans to Tosca XML
	 *
	 * @param root The Tosca XML root bean
	 * @return A Tosca XML-containing String
	 */
	public String convertToXml(Definitions root);

	/**
	 * Converts Tosca XML to Tosca XML beans.
	 * This requires a schema (XSD file) usually.
	 *
	 * @param xmlstring A Tosca XML-containing String
	 * @return The Tosca XML root bean
	 */
	public Definitions convertToXmlBean(String xmlstring);
}
