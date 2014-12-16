package org.opentosca.yamlconverter.main;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;
import org.opentosca.yamlconverter.main.exceptions.ConverterException;

public class Yaml2XmlQuickConverterTest extends BaseTest {
	private final ToscaYaml2XmlConverter c = new ToscaYaml2XmlConverter();

	@Test
	public void testYaml2xml() throws URISyntaxException, IOException, ConverterException {
		final String yaml = this.testUtils.readYamlTestResource("/yaml/helloworld.yaml");
		final String result = this.c.yaml2xml(yaml);
		Assert.assertNotEquals(result, "");
	}
}
