package org.opentosca.yamlconverter.main;

import org.opentosca.model.tosca.Definitions;
import org.opentosca.yamlconverter.main.exceptions.ConverterException;
import org.opentosca.yamlconverter.main.interfaces.IToscaBean2BeanConverter;
import org.opentosca.yamlconverter.main.interfaces.IToscaXml2XmlBeanConverter;
import org.opentosca.yamlconverter.main.interfaces.IToscaYaml2XmlConverter;
import org.opentosca.yamlconverter.main.interfaces.IToscaYaml2YamlBeanConverter;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.ServiceTemplate;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.YAMLElement;

/**
 * This Converter can convert Tosca YAML to Tosca XML (bi-directional) by the use of other converters.
 *
 * @author Jonas Heinisch
 *
 */
public class ToscaYaml2XmlConverter implements IToscaYaml2XmlConverter {

	private final IToscaYaml2YamlBeanConverter yamlConverter = new YamlBeansConverter();
	private final IToscaBean2BeanConverter yamlXmlConverter = new SwitchMapperConverter();
	private final IToscaXml2XmlBeanConverter xmlConverter = new JAXBConverter();

	@Override
	public String convertToXml(String yamlString) throws ConverterException {
		final ServiceTemplate yamlRoot = this.yamlConverter.convertToYamlBean(yamlString);
		final Definitions xmlRoot = this.yamlXmlConverter.convertToXmlBean(yamlRoot);
		return this.xmlConverter.convertToXml(xmlRoot);
	}

	@Override
	public String convertToYaml(String xmlString) throws ConverterException {
		final Definitions xroot = this.xmlConverter.convertToXmlBean(xmlString);
		final YAMLElement yroot = this.yamlXmlConverter.convertToYamlBean(xroot);
		return this.yamlConverter.convertToYaml(yroot);
	}

}
