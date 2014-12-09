package org.opentosca.yamlconverter.main.interfaces;

import org.opentosca.model.tosca.TDefinitions;

public interface ItoscaXML2XMLbeanConverter {
	/**
	 * Converts Tosca XML beans to Tosca XML
	 * @param root The Tosca XML root bean
	 * @return A Tosca XML-containing String
	 */
	public String xmlbean2xml(TDefinitions root);
	
	/**
	 * Converts Tosca XML to Tosca XML beans.
	 * @param xmlstring A Tosca XML-containing String
	 * @return The Tosca XML root bean
	 */
	public TDefinitions xml2xmlbean(String xmlstring);
}
