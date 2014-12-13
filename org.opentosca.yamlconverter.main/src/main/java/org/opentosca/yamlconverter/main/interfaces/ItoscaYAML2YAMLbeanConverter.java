package org.opentosca.yamlconverter.main.interfaces;

import org.opentosca.yamlconverter.yamlmodel.YamlRootElement;
import org.opentosca.yamlconverter.main.exceptions.ConverterException;

public interface ItoscaYAML2YAMLbeanConverter {
	
	/**
	 * Converts Tosca YAML to Tosca YAML beans.
	 * @param yamlstring A Tosca YAML in a String
	 * @return The Tosca YAML root bean
	 * @throws ConverterException 
	 */
	public YamlRootElement yaml2yamlbean(String yamlstring) throws ConverterException;
	
	/**
	 * Converts Tosca YAML beans to Tosca YAML.
	 * @param root The Tosca YAML root bean
	 * @return A Tosca YAML in a String
	 * @throws ConverterException 
	 */
	public String yamlbean2yaml(YamlRootElement root) throws ConverterException;
}
