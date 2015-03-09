package org.opentosca.yamlconverter.main;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;
import org.opentosca.model.tosca.TDefinitions;
import org.opentosca.yamlconverter.main.interfaces.IToscaXml2XmlBeanConverter;

public class DoXMLConvTest extends BaseTest {

	@Test
	public void testXMLconv() {
		final IToscaXml2XmlBeanConverter x2xb = new JAXBConverter();
		String xmlstr = "";
		try {
			xmlstr = readFile("Moodle-Definitions.xml", Charset.defaultCharset());
		} catch (final IOException e) {
			System.out.println("IOexception on fileread");
		}
		final TDefinitions root = x2xb.convertToXmlBean(xmlstr);
		System.out.println("result: " + root);
	}

	private static String readFile(String path, Charset encoding) throws IOException {
		final byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}