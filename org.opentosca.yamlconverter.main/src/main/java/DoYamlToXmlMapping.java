import org.opentosca.yamlconverter.main.ToscaYaml2XmlConverterNew;
import org.opentosca.yamlconverter.main.exceptions.ConverterException;
import org.opentosca.yamlconverter.main.interfaces.IToscaYaml2XmlConverterNew;


public class DoYamlToXmlMapping {

	public static void main(String[] args) {
		String result = "";
		
		//
		try {
		
		IToscaYaml2XmlConverterNew yaml2xml = new ToscaYaml2XmlConverterNew();
		
		result = yaml2xml.yaml2xml("");
		} catch (ConverterException e) {
			e.printStackTrace();
		}
		
		System.out.println(result);
	}

}
