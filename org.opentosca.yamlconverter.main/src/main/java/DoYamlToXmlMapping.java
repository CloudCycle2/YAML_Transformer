import org.opentosca.yamlconverter.main.ToscaYaml2XmlConverter;
import org.opentosca.yamlconverter.main.exceptions.ConverterException;
import org.opentosca.yamlconverter.main.interfaces.IToscaYaml2XmlConverter;


public class DoYamlToXmlMapping {

	public static void main(String[] args) {
		String result = "";
		
		//
		try {
		
		IToscaYaml2XmlConverter yaml2xml = new ToscaYaml2XmlConverter();
		
		result = yaml2xml.yaml2xml("");
		} catch (ConverterException e) {
			e.printStackTrace();
		}
		
		System.out.println(result);
	}

}
