import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.opentosca.model.tosca.TDefinitions;
import org.opentosca.yamlconverter.main.JAXBConverter;
import org.opentosca.yamlconverter.main.interfaces.IToscaXml2XmlBeanConverter;

public class DoXMLConv {

	public static void main(String[] args) {
		final IToscaXml2XmlBeanConverter x2xb = new JAXBConverter();
		String xmlstr = "";
		try {
			xmlstr = readFile("Moodle-Definitions.xml", Charset.defaultCharset());
		} catch (final IOException e) {
			System.out.println("IOexception on fileread");
		}
		final TDefinitions root = x2xb.xml2xmlbean(xmlstr);
		System.out.println("result: " + root);
	}

	private static String readFile(String path, Charset encoding) throws IOException {
		final byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}