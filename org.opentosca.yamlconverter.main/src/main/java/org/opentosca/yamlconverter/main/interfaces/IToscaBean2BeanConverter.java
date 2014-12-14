package org.opentosca.yamlconverter.main.interfaces;

import org.opentosca.model.tosca.TDefinitions;
import org.opentosca.yamlconverter.yamlmodel.YamlRootElement;
import org.opentosca.model.tosca.TestRoot;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.YAMLElement;

public interface IToscaBean2BeanConverter {

	/**
	 * Converts a Tosca XML bean to a Tosca YAML bean.
	 * @param xmlroot The XML root bean
	 * @return The YAML root bean
	 */
	public YAMLElement xmlb2yamlb(TDefinitions xmlroot);

	/**
	 * Converts a Tosca YAML bean to a Tosca XML bean.
	 * @param yamlroot The YAML root bean
	 * @return The XML root bean
	 */
	public TestRoot yamlb2xmlb(YamlRootElement yamlroot);

	public TDefinitions yamlb2xmlb(YAMLElement yamlBean);
}
