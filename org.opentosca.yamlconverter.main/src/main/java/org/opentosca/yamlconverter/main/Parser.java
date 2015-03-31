package org.opentosca.yamlconverter.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opentosca.model.tosca.Definitions;
import org.opentosca.yamlconverter.constraints.ConstraintClause;
import org.opentosca.yamlconverter.main.exceptions.ConverterException;
import org.opentosca.yamlconverter.main.interfaces.IToscaXml2XmlBeanConverter;
import org.opentosca.yamlconverter.main.interfaces.IToscaYaml2YamlBeanConverter;
import org.opentosca.yamlconverter.main.interfaces.IToscaYamlParser;
import org.opentosca.yamlconverter.main.utils.ConstraintUtils;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.Input;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.ServiceTemplate;

/**
 * This implementation of {@link org.opentosca.yamlconverter.main.interfaces.IToscaYamlParser} uses different converter to convert from YAML
 * string to YAML bean to XML bean to XML string. Methods to get an additional XSD schema, get and set input parameters are provided.
 */
public class Parser implements IToscaYamlParser {

	private final IToscaYaml2YamlBeanConverter yamlConverter = new YamlBeansConverter();
	// TODO: make use of the interface, i.e. IToscaYaml2XmlConverter yamlXmlConverter = ...;
	private final SwitchMapperConverter yamlXmlConverter = new SwitchMapperConverter();
	private final IToscaXml2XmlBeanConverter xmlConverter = new JAXBConverter(new NSPrefixMapper());

	private String xml = "";
	private ServiceTemplate serviceTempl = null;
	private Definitions definition = null;

	@Override
	public void parse(String yamlString) {
		if (yamlString == null || yamlString.equals("")) {
			throw new IllegalArgumentException("YAML string may not be empty!");
		}
		try {
			this.serviceTempl = this.yamlConverter.convertToYamlBean(yamlString);
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
			this.definition = this.yamlXmlConverter.convertToXmlBean(this.serviceTempl);
			this.xml = this.xmlConverter.convertToXml(this.definition);
		}
		return this.xml;
	}

	@Override
	public String getXSD() {
		if (this.serviceTempl == null) {
			throw new IllegalStateException("Call parse(..) before calling getXSD()");
		}
		if (this.xml.equals("")) {
			this.definition = this.yamlXmlConverter.convertToXmlBean(this.serviceTempl);
			this.xml = this.xmlConverter.convertToXml(this.definition);
		}
		return this.yamlXmlConverter.getXSD();
	}

	@Override
	public Map<String, String> getInputRequirementsText() {
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
	 * TODO: Clarify why this method is still used in {@link org.opentosca.yamlconverter.main.UI.ConsoleUI}
	 *
	 * @return
	 */
	public Map<String, Input> getInputRequirements() {
		if (this.serviceTempl == null) {
			throw new IllegalStateException("Call parse(..) before calling getInputRequirements()");
		}
		return this.serviceTempl.getInputs();
	}

	/**
	 * This method collects all constraints and their description, adds them to {@code descriptionForUser} and returns the string so that it
	 * can be used. If no constraints are labelled with the input, "None" will be added.
	 *
	 * @param currentInput class containing an input from a YAML string/document
	 * @param descriptionForUser current output for the input variable
	 * @return the output description with information about the constraints for the given input object
	 */
	private String addConstraintsToDescription(Input currentInput, String descriptionForUser) {
		descriptionForUser += "Constraints: ";
		if (currentInput.getConstraints().size() > 0) {
			final List<ConstraintClause<Object>> constraints = ConstraintUtils.toConstraints(currentInput);
			for (final ConstraintClause<Object> constraint : constraints) {
				if (constraint != null) {
					descriptionForUser += constraint.toString() + ", ";
				}
			}
			// remove the last ", "
			descriptionForUser = descriptionForUser.substring(0, descriptionForUser.length() - 2);
		} else {
			descriptionForUser += "None";
		}
		return descriptionForUser;
	}

	/**
	 * Set the input variables.
	 *
	 * @param input the inputVariable map
	 */
	@Override
	public void setInputValues(Map<String, String> input) {
		this.yamlXmlConverter.setInputs(input);
	}

	public ServiceTemplate getServiceTemplate() {
		return this.serviceTempl;
	}

}
