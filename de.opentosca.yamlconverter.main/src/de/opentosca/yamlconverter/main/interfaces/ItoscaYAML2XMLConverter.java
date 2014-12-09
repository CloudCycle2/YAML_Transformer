package de.opentosca.yamlconverter.main.interfaces;

public interface ItoscaYAML2XMLConverter {
	/**
	 * Converts Tosca YAML to Tosca XML.
	 * @param yamlstring A Tosca YAML in a String
	 * @return A Tosca XML in a String
	 */
	public String yaml2xml(String yamlstring);
	
	/**
	 * Converts Tosca XML to Tosca YAML.
	 * @param xmlstring A Tosca XML in a String
	 * @return A Tosca YAML in a String
	 */
	public String xml2yaml(String xmlstring);
}
