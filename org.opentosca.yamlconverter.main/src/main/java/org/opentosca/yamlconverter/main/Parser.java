package org.opentosca.yamlconverter.main;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
			throw new IllegalArgumentException("YAML string may not be empty!");
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
		String result = this.xml;
		// TODO: this only replaces known inputs and properties --> switch to usage of regex
		for (final Entry<String, String> repdata : this.inputs.entrySet()) {
			result = result.replace("get_input(#" + repdata.getKey() + ")", repdata.getValue());
		}
		for (final Entry<String, String> repdata : this.b2b.getPropertyValues().entrySet()) {
			result = result.replace("get_property(#" + repdata.getKey() + ")", repdata.getValue());
		}
		// TODO: get_ref_property
		return result;
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
		if (serviceTemplateInputs != null) {
			for (final String inputKey : serviceTemplateInputs.keySet()) {
				final Input currentInput = serviceTemplateInputs.get(inputKey);
				String descriptionForUser = "Description: ";
				descriptionForUser += currentInput.getDescription() + "; ";
				descriptionForUser += "Type: " + currentInput.getType() + "; ";
				descriptionForUser = addConstraintsToDescription(currentInput, descriptionForUser);
				result.put(inputKey, descriptionForUser);
			}
		}
		return result;
	}

	/**
	 * TODO: add description
	 *
	 * @param currentInput
	 * @param descriptionForUser
	 * @return
	 */
	private String addConstraintsToDescription(Input currentInput, String descriptionForUser) {
		descriptionForUser += "Constraints: ";
		// TODO: improve the following iterations
		for (final Map<String, String> constraints : currentInput.getConstraints()) {
			if (constraints != null) {
				for (final String key : constraints.keySet()) {
					descriptionForUser += key + ": " + constraints.get(key) + ",";
				}
			}
		}
		return descriptionForUser;
	}

	@Override
	public void setInputValues(Map<String, String> input) {
		this.inputs = input;
	}

}
