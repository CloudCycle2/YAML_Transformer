package org.opentosca.yamlconverter.main;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.opentosca.yamlconverter.main.interfaces.IToscaYamlParser;

public class MasterParserTest extends BaseTest {
	private final IToscaYamlParser parser = new Parser();

	@Test
	public void testGetInputRequirements() throws Exception {
		final String yamlInput = this.testUtils.readYamlTestResource("/yaml/inputs.yml");
		this.parser.parse(yamlInput);
		final Map<String, String> inputs = this.parser.getInputRequirementsText();
		Assert.assertNotNull(inputs);
		Assert.assertEquals(3, inputs.size());
		Assert.assertTrue("must contain an input with key 'foo'", inputs.containsKey("foo"));
	}

	@Test
	public void testGetInputRequirements_WithoutConstraints() throws Exception {
		final String yamlInput = this.testUtils.readYamlTestResource("/yaml/inputs_emptyConstraints.yml");
		this.parser.parse(yamlInput);
		final Map<String, String> inputs = this.parser.getInputRequirementsText();
		Assert.assertNotNull(inputs);
		Assert.assertEquals(1, inputs.size());
		Assert.assertTrue("must contain an input with key 'foo'", inputs.containsKey("foo"));
		Assert.assertTrue("constraints are not in description", inputs.get("foo").toLowerCase().contains("constraints: none"));
	}
}
