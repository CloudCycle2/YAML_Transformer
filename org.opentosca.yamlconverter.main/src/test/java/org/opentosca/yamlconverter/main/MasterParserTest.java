package org.opentosca.yamlconverter.main;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.opentosca.yamlconverter.main.exceptions.ConverterException;
import org.opentosca.yamlconverter.main.interfaces.IToscaYamlParser;

public class MasterParserTest extends BaseTest {
	private final IToscaYamlParser parser = new Parser();

	@Test
	public void testYaml2xml() throws URISyntaxException, IOException, ConverterException {
		final String yaml = this.testUtils.readYamlTestResource("/yaml/helloworld.yaml");
		this.parser.parse(yaml);
		// TODO: test
		// parser.getInputRequirements();
		// parser.setInputValues(input);
		final String xml = this.parser.getXML();
		System.out.println(xml);
	}

	@Test
	public void testGetInputRequirements() throws Exception {
		final String yamlInput = this.testUtils.readYamlTestResource("/yaml/inputs.yaml");
		this.parser.parse(yamlInput);
		final Map<String, String> inputs = this.parser.getInputRequirementsText();
		Assert.assertNotNull(inputs);
		Assert.assertEquals(1, inputs.size());
		Assert.assertTrue("must contain an input with key 'foo'", inputs.containsKey("foo"));
	}

	@Test
	public void testGetInputRequirements_WithoutConstraints() throws Exception {
		final String yamlInput = this.testUtils.readYamlTestResource("/yaml/inputs_emptyConstraints.yaml");
		this.parser.parse(yamlInput);
		final Map<String, String> inputs = this.parser.getInputRequirementsText();
		Assert.assertNotNull(inputs);
		Assert.assertEquals(1, inputs.size());
		Assert.assertTrue("must contain an input with key 'foo'", inputs.containsKey("foo"));
		Assert.assertTrue("constraints are not in description", inputs.get("foo").toLowerCase().contains("constraints: none"));
	}
}
