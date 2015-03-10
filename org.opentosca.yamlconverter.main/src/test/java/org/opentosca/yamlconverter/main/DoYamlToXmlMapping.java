package org.opentosca.yamlconverter.main;

import org.junit.Test;
import org.opentosca.yamlconverter.main.exceptions.ConverterException;
import org.opentosca.yamlconverter.main.interfaces.IToscaYaml2XmlConverter;

public class DoYamlToXmlMapping extends BaseTest {

	@Test
	public void testMapping() {
		String result = "";

		//
		try {

			final IToscaYaml2XmlConverter yaml2xml = new ToscaYaml2XmlConverter();

			result = yaml2xml.convertToXml("");
		} catch (final ConverterException e) {
			e.printStackTrace();
		}

		System.out.println(result);
	}

}
