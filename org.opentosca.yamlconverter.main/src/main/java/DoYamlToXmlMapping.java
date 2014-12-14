import org.opentosca.yamlconverter.main.DozerBeanConverter;
import org.opentosca.yamlconverter.main.ToscaYAML2XMLConverter;
import org.opentosca.yamlconverter.main.exceptions.ConverterException;
import org.opentosca.yamlconverter.main.interfaces.ItoscaBean2BeanConverter;
import org.opentosca.yamlconverter.main.interfaces.ItoscaYAML2XMLConverter;


public class DoYamlToXmlMapping {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String result = "";
		
		//
		try {
		
		ItoscaYAML2XMLConverter yaml2xml = new ToscaYAML2XMLConverter();
		
		result = yaml2xml.yaml2xml("");
		} catch (ConverterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(result);
	}

}
