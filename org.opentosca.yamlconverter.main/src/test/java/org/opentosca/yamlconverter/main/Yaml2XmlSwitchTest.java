package org.opentosca.yamlconverter.main;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.opentosca.model.tosca.Definitions;
import org.opentosca.yamlconverter.main.exceptions.ConverterException;
import org.opentosca.yamlconverter.main.interfaces.IToscaXml2XmlBeanConverter;
import org.opentosca.yamlconverter.main.interfaces.IToscaYaml2YamlBeanConverter;
import org.opentosca.yamlconverter.switchmapper.Yaml2XmlSwitch;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.ServiceTemplate;

public class Yaml2XmlSwitchTest extends BaseTest {
	private final IToscaYaml2YamlBeanConverter c = new YamlBeansConverter();
	IToscaXml2XmlBeanConverter conv = new JAXBConverter();

	@Test
	public void testYaml2xml() throws URISyntaxException, IOException, ConverterException {
		final String yaml = this.testUtils.readYamlTestResource("/yaml/helloworld.yaml");
		final ServiceTemplate root = this.c.convertToYamlBean(yaml);
		final Yaml2XmlSwitch mapper = new Yaml2XmlSwitch();
		final Definitions d = mapper.parse(root);
		System.out.println(this.conv.convertToXml(d));
	}
}
