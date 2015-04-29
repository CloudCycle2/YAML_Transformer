package org.opentosca.yamlconverter.switchmapper.subswitches;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.opentosca.model.tosca.Definitions;
import org.opentosca.model.tosca.TDocumentation;
import org.opentosca.model.tosca.TEntityType.DerivedFrom;
import org.opentosca.model.tosca.TEntityType.PropertiesDefinition;
import org.opentosca.model.tosca.TExtensibleElements;
import org.opentosca.model.tosca.TInterface;
import org.opentosca.model.tosca.TOperation;
import org.opentosca.model.tosca.TRequirementType;
import org.opentosca.model.tosca.TServiceTemplate;
import org.opentosca.model.tosca.TTopologyTemplate;
import org.opentosca.yamlconverter.main.utils.AnyMap;
import org.opentosca.yamlconverter.switchmapper.ISubSwitch;
import org.opentosca.yamlconverter.switchmapper.Yaml2XmlSwitch;
import org.opentosca.yamlconverter.switchmapper.typemapper.ElementType;
import org.opentosca.yamlconverter.switchmapper.utils.NamespaceUtil;
import org.opentosca.yamlconverter.switchmapper.utils.PropertiesParserUtil;
import org.opentosca.yamlconverter.switchmapper.utils.TypeMapperUtil;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.PropertyDefinition;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.ServiceTemplate;

/**
 * This class provides some general attributes and methods for its subclasses. The methods support the processing of a YAML service
 * template.
 */
public abstract class AbstractSubSwitch implements ISubSwitch {

	private final Map<String, TRequirementType> addedRequirementTypes = new HashMap<String, TRequirementType>();
	private final Yaml2XmlSwitch parent;
	private TTopologyTemplate topologyCache;

	private final NamespaceUtil namespaceUtil;
	private final TypeMapperUtil typeMapperUtil;
	private final PropertiesParserUtil propertiesParserUtil;

	public AbstractSubSwitch(Yaml2XmlSwitch parentSwitch) {
		this.parent = parentSwitch;
		this.namespaceUtil = new NamespaceUtil(getDefinitions().getTargetNamespace());
		this.typeMapperUtil = new TypeMapperUtil(this.namespaceUtil);
		this.propertiesParserUtil = new PropertiesParserUtil(this.parent, getServiceTemplate());
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

	protected NamespaceUtil getNamespaceUtil() {
		return this.namespaceUtil;
	}

	protected TypeMapperUtil getTypeMapperUtil() {
		return this.typeMapperUtil;
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
		result.setTypeRef(getNamespaceUtil().toTnsQName(derived_from));
		return result;
	}

	/**
	 * Creates a {@link org.opentosca.model.tosca.TEntityType.DerivedFrom} object and sets a type reference to {@code referenceDerivedFrom}.
	 *
	 * @param referenceDerivedFrom a QName object representing a xml reference
	 * @return a DerivedFrom object containing a reference of {@code referenceDerivedFrom}
	 */
	protected DerivedFrom parseDerivedFrom(QName referenceDerivedFrom) {
		final DerivedFrom result = new DerivedFrom();
		result.setTypeRef(referenceDerivedFrom);
		return result;
	}

	/**
	 * Creates and sets the PropertiesDefinition for this element and adds the element to the xsd definitions. Uses
	 * {@link #propertiesParserUtil} to do this.
	 *
	 * @param properties of this element
	 * @param typename of this element
	 * @return a reference to the PropertiesDefinition
	 */
	protected PropertiesDefinition parsePropertiesDefinition(Map<String, PropertyDefinition> properties, String typename) {
		return this.propertiesParserUtil.parsePropertiesDefinition(properties, typename);
	}

	/**
	 * Converts the given map to a jaxb-parseable {@link AnyMap} by using {@link #propertiesParserUtil}.
	 *
	 * @param customMap given properties
	 * @param nodeName name of the element
	 * @return the {@link JAXBElement} with the AnyMap
	 */
	protected JAXBElement<AnyMap> getAnyMapForProperties(final Map<String, Object> customMap, final QName nodeName) {
		return this.propertiesParserUtil.getAnyMapForProperties(customMap, nodeName);
	}

	/**
	 * Converts {@code entry} to a {@link org.opentosca.model.tosca.TInterface} object. {@code entry} is a map of operation definitions
	 * containing an operation name and maybe some other attributes like description. {@link org.opentosca.model.tosca.TInterface} only
	 * supports the interface and operation name, so that other attributes like description are not used and "thrown away".
	 *
	 * @param entry containing one interface name as key and operation definitions as object
	 * @return interface object with operations
	 */
	protected TInterface getInterfaceWithOperations(Entry<String, Map<String, Map<String, String>>> entry) {
		final TInterface tInterface = new TInterface();
		tInterface.setName(entry.getKey());
		for (final Entry<String, Map<String, String>> operation : entry.getValue().entrySet()) {
			final TOperation tOperation = new TOperation();
			tOperation.setName(operation.getKey());
			tInterface.getOperation().add(tOperation);
		}
		return tInterface;
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
			requirementType.setRequiredCapabilityType(getTypeMapperUtil().getCorrectTypeReferenceAsQName(capability,
					ElementType.CAPABILITY_TYPE));
			this.addedRequirementTypes.put(requirementTypeName, requirementType);
			getDefinitions().getServiceTemplateOrNodeTypeOrNodeTypeImplementation().add(requirementType);
		}
	}

	@Override
	public abstract void process();
}
