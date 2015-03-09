package org.opentosca.yamlconverter.main.interfaces;

import org.opentosca.model.tosca.Definitions;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.ServiceTemplate;

/**
 * This interface defines methods to convert from YAML beans to XML beans or vice versa.
 */
public interface IToscaBean2BeanConverter {

	/**
	 * Converts a Tosca XML bean to a Tosca YAML bean.
	 *
	 * @param xmlRoot The XML root bean
	 * @return The YAML root bean
	 */
	public ServiceTemplate convertToYamlBean(Definitions xmlRoot);

	/**
	 * Converts a Tosca YAML bean to a Tosca XML bean.
	 *
	 * @param yamlRoot The YAML root bean
	 * @return The XML root bean
	 */
	public Definitions convertToXmlBean(ServiceTemplate yamlRoot);
}
