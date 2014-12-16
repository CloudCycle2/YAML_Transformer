package org.opentosca.yamlconverter.main;

import org.opentosca.model.tosca.TDefinitions;
import org.opentosca.yamlconverter.main.exceptions.ConverterException;
import org.opentosca.yamlconverter.main.interfaces.IToscaBean2BeanConverter;
import org.opentosca.yamlconverter.main.interfaces.IToscaXml2XmlBeanConverter;
import org.opentosca.yamlconverter.main.interfaces.IToscaYaml2XmlConverter;
import org.opentosca.yamlconverter.main.interfaces.IToscaYaml2YamlBeanConverter;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.YAMLElement;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.YAMLFileRoot;

/**
 * This Converter can convert Tosca YAML to Tosca XML (bi-directional) by the use of other converters.
 *
 * @author Jonas Heinisch
 *
 */
public class ToscaYaml2XmlConverter implements IToscaYaml2XmlConverter {
	private final IToscaYaml2YamlBeanConverter y2yb = new YamlBeansConverter();
	private final IToscaBean2BeanConverter b2b = new QuickBeansConverter();
	private final IToscaXml2XmlBeanConverter x2xb = new JAXBConverter();

	@Override
	public String yaml2xml(String yamlstring) throws ConverterException {
		final YAMLFileRoot yroot = this.y2yb.yaml2yamlbean(yamlstring);
		final TDefinitions xroot = this.b2b.yamlb2xmlb(yroot);
		return this.x2xb.xmlbean2xml(xroot);

		// final YamlRootElement yamlroot = new YamlRootElement();
		// yamlroot.description = "TestDescription";

		// final TestRoot testRoot = this.b2b.yamlb2xmlb(yamlroot);
		// return testRoot.description;
	}

	@Override
	public String xml2yaml(String xmlstring) throws ConverterException {
		final TDefinitions xroot = this.x2xb.xml2xmlbean(xmlstring);
		final YAMLElement yroot = this.b2b.xmlb2yamlb(xroot);
		return this.y2yb.yamlbean2yaml(yroot);
	}

}
