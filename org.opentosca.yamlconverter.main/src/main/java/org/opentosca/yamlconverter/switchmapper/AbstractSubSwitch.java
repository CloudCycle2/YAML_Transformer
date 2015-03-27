package org.opentosca.yamlconverter.switchmapper;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlWriter;
import org.opentosca.model.tosca.*;
import org.opentosca.model.tosca.TEntityType.DerivedFrom;
import org.opentosca.model.tosca.TEntityType.PropertiesDefinition;
import org.opentosca.yamlconverter.main.exceptions.NoBaseTypeMappingException;
import org.opentosca.yamlconverter.main.utils.AnyMap;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.PropertyDefinition;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.ServiceTemplate;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class AbstractSubSwitch implements ISubSwitch {

	private Map<String, TRequirementType> addedRequirementTypes = new HashMap<String, TRequirementType>();
	private final Yaml2XmlSwitch parent;
	private TTopologyTemplate topologyCache;

	public AbstractSubSwitch(Yaml2XmlSwitch parentSwitch) {
		this.parent = parentSwitch;
	}

	protected Definitions getDefinitions() {
		return this.parent.getToscaResult();
	}

	protected ServiceTemplate getServiceTemplate() {
		return this.parent.getServiceTemplate();
	}

	protected String getTargetNamespace() {
		return this.parent.getUsedNamespace();
	}

	/**
	 * Returns the current topology template
	 *
	 * @return the template, or null if neither a cached or parsed template exists
	 */
	protected TTopologyTemplate getTopologyTemplate() {
		if (this.topologyCache == null) {
			for (final TExtensibleElements elem : getDefinitions().getServiceTemplateOrNodeTypeOrNodeTypeImplementation()) {
				if (elem instanceof TServiceTemplate) {
					this.topologyCache = ((TServiceTemplate) elem).getTopologyTemplate();
					break;
				}
			}
		}
		return this.topologyCache;
	}

	/**
	 * Creates a TDocumentation with the given String and adds it to this element
	 *
	 * @param desc the description
	 * @return the TDocumentation reference
	 */
	protected TDocumentation toDocumentation(String desc) {
		final TDocumentation docu = new TDocumentation();
		docu.getContent().add(desc);
		return docu;
	}

	/**
	 * If name contains multiple aliases the result is the first.
	 *
	 * @param name The name field of an XML element.
	 * @return A valid ID.
	 */
	protected String name2id(String name) {
		return name.split(",")[0];
	}

	/**
	 * Creates and sets the derived_from information on this element
	 *
	 * @param derived_from name of the super element
	 * @return the DerivedFrom reference
	 */
	protected DerivedFrom parseDerivedFrom(String derived_from) {
		final DerivedFrom result = new DerivedFrom();
		result.setTypeRef(toTnsQName(derived_from));
		return result;
	}

	/**
	 * Creates a QName element for the given local name with the target namespace of our {@link #getDefinitions()}
	 *
	 * @param localName of the element
	 * @return the QName
	 */
	protected QName toTnsQName(String localName) {
		return new QName(getDefinitions().getTargetNamespace(), localName, Yaml2XmlSwitch.TOSCA_NS_PREFIX);
	}

	protected QName toBaseTypesNsQName(String name) {
		return new QName(Yaml2XmlSwitch.BASE_TYPES_NS, name, Yaml2XmlSwitch.BASE_TYPES_PREFIX);
	}

	protected QName toSpecificTypesNsQName(String name) {
		return new QName(Yaml2XmlSwitch.SPECIFIC_TYPES_NS, name, Yaml2XmlSwitch.SPECIFIC_TYPES_PREFIX);
	}

	/**
	 * Creates and sets the PropertiesDefinition for this element and adds the element to the xsd definitions.
	 *
	 * @param properties of this element
	 * @param typename of this element
	 * @return a reference to the PropertiesDefinition
	 */
	protected PropertiesDefinition parsePropertiesDefinition(Map<String, PropertyDefinition> properties, String typename) {
		final PropertiesDefinition result = new PropertiesDefinition();
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
		this.parent.getXSDStringBuilder()
				.append("<xs:complexType name=\"")
				.append(tName)
				.append("\">\n");
		this.parent.getXSDStringBuilder().append("<xs:sequence>\n");

		for (final Entry<String, PropertyDefinition> entry : properties.entrySet()) {
			this.parent.getXSDStringBuilder()
					.append("<xs:element name=\"")
					.append(entry.getKey())
					.append("\" type=\"xs:")
					.append(entry.getValue().getType())
					.append("\" />\n");
		}

		this.parent.getXSDStringBuilder()
				.append("</xs:sequence>\n")
				.append("</xs:complexType>\n");
		this.parent.getXSDStringBuilder()
				.append("<xs:element name=\"")
				.append(name)
				.append("\" type=\"")
				.append(tName)
				.append("\" />\n");
	}

	/**
	 * Converts the given map to a jaxb-parseable {@link AnyMap}
	 *
	 * @param customMap given properties
	 * @param nodename name of the element
	 * @return the {@link JAXBElement} with the AnyMap
	 */
	protected JAXBElement<AnyMap> getAnyMapForProperties(final Map<String, Object> customMap, final String nodename) {
		final AnyMap properties = new AnyMap(parseProperties(customMap));
		properties.getOtherAttributes().put(new QName("xmlns"), Yaml2XmlSwitch.TYPES_NS);
		return new JAXBElement<AnyMap>(new QName(Yaml2XmlSwitch.TYPES_NS, nodename + "Properties", "types"), AnyMap.class, properties);
	}

	/**
	 *
	 * @param properties
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> parseProperties(Map<String, Object> properties) {
		final Map<String, String> result = new HashMap<String, String>();
		for (final Entry<String, Object> entry : properties.entrySet()) {
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
	 * @param value
	 * @return true if getter, false if property
	 */
	private boolean isGetter(Object value) {
		if (value instanceof Map<?, ?>) {
			return true;
		}
		return false;
	}

	protected TInterface getInterfaceWithOperations(Entry<String, Map<String, Map<String, String>>> entry) {
		final TInterface inf = new TInterface();
		inf.setName(entry.getKey());
		// TODO: is this right?!
		for (final Entry<String, Map<String, String>> op : entry.getValue().entrySet()) {
			final TOperation top = new TOperation();
			top.setName(op.getKey());
			// value contains keys "implementation" and "description" eventually
			// TODO: how to use implementation name??
			inf.getOperation().add(top);
		}
		return inf;
	}

	/**
	 * processes the get* keywords, e.g. get_property or get_input
	 *
	 * @param getterMap combined map of get_inputs and get_properties
	 * @return the String denoting the userinput, or {@link Yaml2XmlSwitch#DEFAULT_USER_INPUT} if none exists
	 */
	private String parseGetter(Map<String, Object> getterMap) {
		for (final Entry<String, Object> getter : getterMap.entrySet()) {
			switch (getter.getKey()) {
			case "get_input":
				final String inputvar = (String) getter.getValue();
				if (this.parent.getInputs().containsKey(inputvar)) {
					return this.parent.getInputs().get(inputvar);
				}
				if (getServiceTemplate().getInputs().containsKey(inputvar)) {
					if (getServiceTemplate().getInputs().get(inputvar).getDefault() != null
							&& !getServiceTemplate().getInputs().get(inputvar).getDefault().isEmpty()) {
						return getServiceTemplate().getInputs().get(inputvar).getDefault();
					}
				}
				return Yaml2XmlSwitch.DEFAULT_USER_INPUT;
			case "get_property":
				@SuppressWarnings("unchecked")
				final List<String> list = (List<String>) getter.getValue();
				final String template = list.get(0);
				final String property = list.get(1);
				if (getServiceTemplate().getNode_templates().containsKey(template)) {
					if (getServiceTemplate().getNode_templates().get(template).getProperties().containsKey(property)) {
						return (String) getServiceTemplate().getNode_templates().get(template).getProperties().get(property);
					}
				}
			case "get_ref_property":
				return Yaml2XmlSwitch.DEFAULT_USER_INPUT;
			default:
				final String result = serializeYAML(getterMap);
				if (result != null) {
					return result;
				} else {
					return Yaml2XmlSwitch.DEFAULT_USER_INPUT;
				}
			}
		}
		return "";
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

	/**
	 * Creates a {@link TRequirementType} and adds it to the service template
	 * 
	 * @param capability name of the capability
	 * @param requirementTypeName name of the requirement
	 */
	protected void createAndAddRequirementType(final String capability, final String requirementTypeName) {
		// create requirement type for requirement if no has been created before
		if (!this.addedRequirementTypes.containsKey(requirementTypeName)) {
			final TRequirementType requirementType = new TRequirementType();
			requirementType.setName(requirementTypeName);
			try {
				requirementType.setRequiredCapabilityType(toTnsQName(BaseTypeMapper.getXmlCapabilityType(capability)));
			} catch (NoBaseTypeMappingException e) {
				requirementType.setRequiredCapabilityType(toTnsQName(capability));
			}
			this.addedRequirementTypes.put(requirementTypeName, requirementType);
			getDefinitions().getServiceTemplateOrNodeTypeOrNodeTypeImplementation().add(requirementType);
		}
	}

	@Override
	public abstract void process();
}
