package org.opentosca.yamlconverter.main.interfaces;

import org.opentosca.yamlconverter.main.exceptions.ConverterException;

public interface IToscaYaml2XmlConverter {
	/**
	 * Converts Tosca YAML to Tosca XML.
	 * @param yamlstring A Tosca YAML in a String
	 * @return A Tosca XML in a String
	 * @throws ConverterException 
	 */
	public String yaml2xml(String yamlstring) throws ConverterException;
	
	/**
	 * Converts Tosca XML to Tosca YAML.
	 * @param xmlstring A Tosca XML in a String
	 * @return A Tosca YAML in a String
	 * @throws ConverterException
	 */
	public String xml2yaml(String xmlstring) throws ConverterException;
}
