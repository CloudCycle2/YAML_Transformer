package org.opentosca.yamlconverter.main.interfaces;

import org.opentosca.yamlconverter.main.exceptions.ConverterException;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.ServiceTemplate;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.YAMLElement;

/**
 * This interface provides methods to convert a YAML string to a YAML bean or vice versa.
 */
public interface IToscaYaml2YamlBeanConverter {

	/**
	 * Converts Tosca YAML to Tosca YAML beans.
	 *
	 * @param yamlstring A Tosca YAML in a String
	 * @return The Tosca YAML root bean
	 * @throws ConverterException
	 */
	public ServiceTemplate convertToYamlBean(String yamlstring) throws ConverterException;

	/**
	 * Converts Tosca YAML beans to Tosca YAML.
	 *
	 * @param root The Tosca YAML root bean
	 * @return A Tosca YAML in a String
	 * @throws ConverterException
	 */
	public String convertToYaml(YAMLElement root) throws ConverterException;
}
