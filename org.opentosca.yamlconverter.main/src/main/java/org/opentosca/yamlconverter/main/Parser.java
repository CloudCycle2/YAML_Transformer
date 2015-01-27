package org.opentosca.yamlconverter.main;

import java.util.HashMap;
import java.util.Map;

import org.opentosca.model.tosca.Definitions;
import org.opentosca.yamlconverter.main.exceptions.ConverterException;
import org.opentosca.yamlconverter.main.interfaces.IToscaXml2XmlBeanConverter;
import org.opentosca.yamlconverter.main.interfaces.IToscaYaml2YamlBeanConverter;
import org.opentosca.yamlconverter.main.interfaces.IToscaYamlParser;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.Input;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.ServiceTemplate;

public class Parser implements IToscaYamlParser {

	private final IToscaYaml2YamlBeanConverter y2yb = new YamlBeansConverter();
	private final SwitchMapperConverter b2b = new SwitchMapperConverter();
	private final IToscaXml2XmlBeanConverter x2xb = new JAXBConverter();

	private String xml = "";
	private ServiceTemplate serviceTempl = null;
	private Definitions definition = null;

	// TODO: Is this map still needed?! Yes, in fillGetter()
	private Map<String, String> inputs = new HashMap<>();

	@Override
	public void parse(String yamlString) {
		if (yamlString == null || yamlString.equals("")) {
			throw new IllegalArgumentException("Path to yaml file may not be empty!");
		}
		try {
			// TODO: how to handle the inputs? Suggestion: Give a default value to
			// yaml2yamlbean as additional parameter and add interceptor like logic (see alberts approach)
			this.serviceTempl = this.y2yb.yaml2yamlbean(yamlString);
		} catch (final ConverterException e) {
			throw new RuntimeException(e);
		}
		this.definition = this.b2b.yamlb2xmlb(this.serviceTempl);
		this.xml = this.x2xb.xmlbean2xml(this.definition);
	}

	@Override
	public String getXML() {
		if (this.xml.equals("")) {
			throw new IllegalStateException("Call parse(..) before calling getXML()");
		}
		return fillGetter();
	}

	private String fillGetter() {
		// TODO: implement me :)
		return this.xml;
	}

	@Override
	public String getXSD() {
		if (this.xml.equals("")) {
			throw new IllegalStateException("Call parse(..) before calling getXSD()");
		}
		return this.b2b.getXSD();
	}

	@Override
	public Map<String, String> getInputRequirements() {
		if (this.serviceTempl == null) {
			throw new IllegalStateException("Call parse(..) before calling getInputRequirements()");
		}
		final Map<String, String> result = new HashMap<String, String>();
		final Map<String, Input> serviceTemplateInputs = this.serviceTempl.getInputs();
		for (final String inputKey : serviceTemplateInputs.keySet()) {
			// TODO: we need a better data structure for inputs (it's just an empty class without any attributes),
			// respectively for ServiceTemplate
			// TODO: adjust code to improved data structure
			result.put(inputKey, serviceTemplateInputs.get(inputKey).toString());
		}
		return result;
	}

	@Override
	public void setInputValues(Map<String, String> input) {
		this.inputs = input;
	}

}
