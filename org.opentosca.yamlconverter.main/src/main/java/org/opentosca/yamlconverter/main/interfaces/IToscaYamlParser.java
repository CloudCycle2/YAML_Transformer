package org.opentosca.yamlconverter.main.interfaces;

import java.util.Map;

public interface IToscaYamlParser {
	// TODO: YAMLFileRoot -> ServiceTemplate

	/**
	 * Tell the parser to parse the yamlString to XML.
	 *
	 * @param yamlString YAML ServiceTemplate as a String
	 */
	public void parse(String yamlString);

	/**
	 * Get the parsed XML as a String. Requires previous call of parse(..) Optional Requirement: call setInputValues(..) to replace
	 * input-getter.
	 *
	 * @return parsed XML
	 */
	public String getXML();

	/**
	 * Get the parsed XSD as a String. Requires previous call of parse(..)
	 *
	 * @return parsed XSD
	 */
	public String getXSD();

	/**
	 * Get a Map containing required input variables and a description about the input requirements. Map: inputvarname ->
	 * description/requirements Requires previous call of parse(..)
	 *
	 * @return input requirements map
	 */
	public Map<String, String> getInputRequirements();

	/**
	 * Set the values for the input Variables. Map: inputvarname -> inputvarvalue
	 *
	 * @param input the inputvar map
	 */
	public void setInputValues(Map<String, String> input);
}
