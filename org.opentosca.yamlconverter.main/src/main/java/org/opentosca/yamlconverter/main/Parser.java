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

	@Override
	public void parse(String yamlString) {
		if (yamlString == null || yamlString.equals("")) {
			throw new IllegalArgumentException("YAML string may not be empty!");
		}
		try {
			this.serviceTempl = this.y2yb.yaml2yamlbean(yamlString);
		} catch (final ConverterException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getXML() {
		if (this.serviceTempl == null) {
			throw new IllegalStateException("Call parse(..) before calling getXML()");
		}
		if (this.xml.equals("")) {
			this.definition = this.b2b.yamlb2xmlb(this.serviceTempl);
			this.xml = this.x2xb.xmlbean2xml(this.definition);
		}
		return this.xml;
	}

	@Override
	public String getXSD() {
		if (this.serviceTempl == null) {
			throw new IllegalStateException("Call parse(..) before calling getXSD()");
		}
		if (this.xml.equals("")) {
			this.definition = this.b2b.yamlb2xmlb(this.serviceTempl);
			this.xml = this.x2xb.xmlbean2xml(this.definition);
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
		if (currentInput.getConstraints().size() > 0) {
			// TODO: improve the following iterations
			for (final Map<String, Object> constraints : currentInput.getConstraints()) {
				if (constraints != null) {
					for (final String key : constraints.keySet()) {
						descriptionForUser += key + ": " + constraints.get(key) + ",";
					}
				}
			}
		} else {
			descriptionForUser += "None";
		}
		return descriptionForUser;
	}

	@Override
	public void setInputValues(Map<String, String> input) {
		this.b2b.setInputs(input);
	}

}
