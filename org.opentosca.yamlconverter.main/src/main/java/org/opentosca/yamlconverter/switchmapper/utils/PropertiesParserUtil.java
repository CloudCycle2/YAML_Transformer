package org.opentosca.yamlconverter.switchmapper.utils;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.opentosca.model.tosca.TEntityType;
import org.opentosca.yamlconverter.main.utils.AnyMap;
import org.opentosca.yamlconverter.switchmapper.Yaml2XmlSwitch;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.Input;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.NodeTemplate;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.PropertyDefinition;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.ServiceTemplate;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlWriter;

/**
 * @author Sebi
 */
public class PropertiesParserUtil {

	private final Yaml2XmlSwitch parent;
	private final ServiceTemplate serviceTemplate;

	public PropertiesParserUtil(Yaml2XmlSwitch parentSwitch, ServiceTemplate serviceTemplate) {
		if (parentSwitch == null || serviceTemplate == null) {
			throw new IllegalArgumentException("Params may not be null.");
		}
		this.parent = parentSwitch;
		this.serviceTemplate = serviceTemplate;
	}

	/**
	 * Creates and sets the PropertiesDefinition for this element and adds the element to the xsd definitions.
	 *
	 * @param properties of this element
	 * @param typename of this element
	 * @return a reference to the PropertiesDefinition
	 */
	public TEntityType.PropertiesDefinition parsePropertiesDefinition(Map<String, PropertyDefinition> properties, String typename) {
		final TEntityType.PropertiesDefinition result = new TEntityType.PropertiesDefinition();
		// setType() works, setElement will throw an error while importing the XML to Winery
		result.setType(new QName(Yaml2XmlSwitch.TYPES_NS, typename + "Properties", "types"));
		generateTypeXSD(properties, typename + "Properties");
		return result;
	}

	/**
	 * Adds the xsd information for the element to the xsd builder.
	 *
	 * @param properties of the element
	 * @param name of the element
	 */
	private void generateTypeXSD(Map<String, PropertyDefinition> properties, String name) {
		final String tName = "t" + name;
		this.parent.getXSDStringBuilder().append("<xs:complexType name=\"").append(tName).append("\">\n");
		this.parent.getXSDStringBuilder().append("<xs:sequence>\n");

		for (final Map.Entry<String, PropertyDefinition> entry : properties.entrySet()) {
			this.parent.getXSDStringBuilder().append("<xs:element name=\"").append(entry.getKey()).append("\" type=\"xs:")
			.append(entry.getValue().getType()).append("\" />\n");
		}

		this.parent.getXSDStringBuilder().append("</xs:sequence>\n").append("</xs:complexType>\n");
		this.parent.getXSDStringBuilder().append("<xs:element name=\"").append(name).append("\" type=\"").append(tName).append("\" />\n");
	}

	/**
	 * Converts the given map to a jaxb-parseable {@link org.opentosca.yamlconverter.main.utils.AnyMap}
	 *
	 * @param customMap given properties
	 * @param nodeName name of the element
	 * @return the {@link javax.xml.bind.JAXBElement} with the AnyMap
	 */
	public JAXBElement<AnyMap> getAnyMapForProperties(final Map<String, Object> customMap, final QName nodeName) {
		final AnyMap properties = new AnyMap(parseProperties(customMap));
		properties.getOtherAttributes().put(new QName("xmlns"), nodeName.getNamespaceURI());
		final QName elementQName = new QName(nodeName.getNamespaceURI(), nodeName.getLocalPart() + "Properties", nodeName.getPrefix());
		return new JAXBElement<AnyMap>(elementQName, AnyMap.class, properties);
	}

	/**
	 * Parses a map of properties. Each entry is checked if it contains a getter-method, like get_input or get_property. If yes,
	 * {@link #parseGetter(java.util.Map)} is called. If no, the value is set directly.
	 *
	 * @param properties map of properties
	 * @return a map with replaced get* keywords eventually
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> parseProperties(Map<String, Object> properties) {
		final Map<String, String> result = new HashMap<String, String>();
		for (final Map.Entry<String, Object> entry : properties.entrySet()) {
			String value = "";
			if (isGetter(entry.getValue())) {
				value = parseGetter((Map<String, Object>) entry.getValue());
			} else {
				value = (String) entry.getValue();
			}
			result.put(entry.getKey(), value);
		}
		return result;
	}

	/**
	 * Checks whether the Object is a Getter or just a normal property.
	 *
	 * @param value the object to check for a getter
	 * @return true if getter, false if property
	 */
	private boolean isGetter(Object value) {
		return value instanceof Map<?, ?>;
	}

	/**
	 * Processes the get* keywords, e.g. get_property or get_input.
	 *
	 * @param getterMap combined map of get_inputs and get_properties
	 * @return the String denoting the user input, or {@link Yaml2XmlSwitch#DEFAULT_USER_INPUT} if none exists
	 */
	private String parseGetter(Map<String, Object> getterMap) {
		// TODO: are multiple get* keywords allowed? if yes, this method would always return a value in the first iteration;
		// TODO: if no, the for-loop is unnecessary.
		for (final Map.Entry<String, Object> getter : getterMap.entrySet()) {
			switch (getter.getKey()) {
			case "get_input":
				return getInputValue(getter);
			case "get_property":
				@SuppressWarnings("unchecked")
				final List<String> list = (List<String>) getter.getValue();
				final String template = list.get(0);
				final String property = list.get(1);
				final Map<String, NodeTemplate> nodeTemplates = this.serviceTemplate.getNode_templates();
				if (nodeTemplates.containsKey(template)) {
					if (nodeTemplates.get(template).getProperties().containsKey(property)) {
						return (String) nodeTemplates.get(template).getProperties().get(property);
					}
				}
			case "get_ref_property":
				return Yaml2XmlSwitch.DEFAULT_USER_INPUT;
			default:
				return getGetterDefault(getterMap);
			}
		}
		return "";
	}

	/**
	 * Get value for get_input keyword. First try to read the user input for the value (if any exists). Otherwise try to read the default
	 * value from YAML service template (if any exists). Otherwise use default
	 * {@link org.opentosca.yamlconverter.switchmapper.Yaml2XmlSwitch#DEFAULT_USER_INPUT}.
	 *
	 * @param getter entry containing get_input and a value which is the name of the input
	 * @return an input value or a default
	 */
	private String getInputValue(final Map.Entry<String, Object> getter) {
		final String getterValue = (String) getter.getValue();
		// 1.) try to use the custom user inputs
		final Map<String, String> userInputs = this.parent.getInputs();
		if (userInputs.containsKey(getterValue)) {
			return userInputs.get(getterValue);
		}
		// 2.) try to use the input default value defined in service template
		final Map<String, Input> serviceTemplateInputs = this.serviceTemplate.getInputs();
		if (serviceTemplateInputs.containsKey(getterValue)) {
			final String inputDefault = serviceTemplateInputs.get(getterValue).getDefault();
			if (inputDefault != null && !inputDefault.isEmpty()) {
				return inputDefault;
			}
		}
		return Yaml2XmlSwitch.DEFAULT_USER_INPUT;
	}

	/**
	 * Gets a default value. Tries to serialize {@code getterMap} by using {@link com.esotericsoftware.yamlbeans.YamlWriter}. If this is not
	 * possible, a default is used: {@link org.opentosca.yamlconverter.switchmapper.Yaml2XmlSwitch#DEFAULT_USER_INPUT}.
	 *
	 * @param getterMap map containing no correct get* keyword
	 * @return map as YAML string or default
	 */
	private String getGetterDefault(final Map<String, Object> getterMap) {
		final String result = serializeYAML(getterMap);
		if (result != null) {
			return result;
		} else {
			return Yaml2XmlSwitch.DEFAULT_USER_INPUT;
		}
	}

	/**
	 * Serializes the given map to a yaml string
	 *
	 * @param getterMap to serialize
	 * @return the yaml string, or <code>null</code> if an exception occurred.
	 */
	private String serializeYAML(Map<String, Object> getterMap) {
		final Writer output = new StringWriter();
		final YamlWriter writer = new YamlWriter(output);
		try {
			writer.write(getterMap);
			writer.close();
		} catch (final YamlException e) {
			return null;
		}
		return output.toString();
	}
}
