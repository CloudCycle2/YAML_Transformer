package de.opentosca.yamlconverter.main.interfaces;

public interface ItoscaYAML2YAMLbeanConverter {
	
	/**
	 * Converts Tosca YAML to Tosca YAML beans.
	 * @param yamlstring A Tosca YAML in a String
	 * @return The Tosca YAML root bean
	 */
	public YamlRootElement yaml2yamlbean(String yamlstring);
	
	/**
	 * Converts Tosca YAML beans to Tosca YAML.
	 * @param root The Tosca YAML root bean
	 * @return A Tosca YAML in a String
	 */
	public String yamlbean2yaml(YamlRootElement root);
}
