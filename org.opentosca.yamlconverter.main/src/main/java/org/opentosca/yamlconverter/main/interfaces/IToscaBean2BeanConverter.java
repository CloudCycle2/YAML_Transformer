package org.opentosca.yamlconverter.main.interfaces;

import org.opentosca.model.tosca.Definitions;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.ServiceTemplate;

/**
 * TODO: rename methods, e.g. convertToYamlBean
 */
public interface IToscaBean2BeanConverter {

	/**
	 * Converts a Tosca XML bean to a Tosca YAML bean.
	 *
	 * @param xmlroot The XML root bean
	 * @return The YAML root bean
	 */
	public ServiceTemplate xmlb2yamlb(Definitions xmlroot);

	/**
	 * Converts a Tosca YAML bean to a Tosca XML bean.
	 *
	 * @param yamlroot The YAML root bean
	 * @return The XML root bean
	 */
	// public TestRoot yamlb2xmlb(YamlRootElement yamlroot);

	public Definitions yamlb2xmlb(ServiceTemplate yamlBean);
}
