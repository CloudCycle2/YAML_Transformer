package org.opentosca.yamlconverter.main;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;
import org.opentosca.yamlconverter.main.exceptions.ConverterException;
import org.opentosca.yamlconverter.main.interfaces.IToscaYaml2YamlBeanConverter;
import org.opentosca.yamlconverter.switchmapper.Yaml2XmlTextSwitch;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.YAMLFileRoot;

public class Yaml2XmlDirectConverterTest extends BaseTest {
	private final IToscaYaml2YamlBeanConverter c = new YamlBeansConverter();

	@Test
	public void testYaml2xml() throws URISyntaxException, IOException, ConverterException {
		final String yaml = this.testUtils.readYamlTestResource("/yaml/helloworld.yaml");
		final YAMLFileRoot root = this.c.yaml2yamlbean(yaml);
		final Yaml2XmlTextSwitch mapper = new Yaml2XmlTextSwitch();
		mapper.parse(root);
		final String result = mapper.getXML();
		Assert.assertNotEquals(result, "");
		System.out.println(result);
	}
}
