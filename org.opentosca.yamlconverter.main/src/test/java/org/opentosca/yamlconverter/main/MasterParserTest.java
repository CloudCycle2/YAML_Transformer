package org.opentosca.yamlconverter.main;

import java.io.IOException;
import java.net.URISyntaxException;

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
}
