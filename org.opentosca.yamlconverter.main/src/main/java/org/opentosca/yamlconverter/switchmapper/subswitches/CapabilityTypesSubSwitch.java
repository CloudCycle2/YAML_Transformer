package org.opentosca.yamlconverter.switchmapper.subswitches;

import org.opentosca.model.tosca.TCapabilityType;
import org.opentosca.yamlconverter.switchmapper.Yaml2XmlSwitch;
import org.opentosca.yamlconverter.switchmapper.typemapper.ElementType;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.CapabilityType;

import javax.xml.namespace.QName;
import java.util.Map.Entry;

/**
 * This sub switch supports processing the capability types of a YAML service template.
 */
public class CapabilityTypesSubSwitch extends AbstractSubSwitch {

	public CapabilityTypesSubSwitch(Yaml2XmlSwitch parentSwitch) {
		super(parentSwitch);
	}

	/**
	 * For each capability type of the YAML service template, a corresponding {@link org.opentosca.model.tosca.TCapabilityType}
	 * is created and added to the {@link #getDefinitions()} object.
	 */
	@Override
	public void process() {
		if (getServiceTemplate().getCapability_types() != null) {
			for (final Entry<String, CapabilityType> capType : getServiceTemplate().getCapability_types().entrySet()) {
				final TCapabilityType capabilityType = createCapabilityType(capType);
				capabilityType.setName(capType.getKey());
				getDefinitions().getServiceTemplateOrNodeTypeOrNodeTypeImplementation().add(capabilityType);
			}
		}
	}

	/**
	 * Creates a Tosca capability type. Sets name and other properties which can be read from
	 * {@link org.opentosca.yamlconverter.yamlmodel.yaml.element.CapabilityType}.
	 *
	 * @param capType entry of a capability type in YAML service template
	 * @return Tosca capability type
	 */
	private TCapabilityType createCapabilityType(Entry<String, CapabilityType> capType) {
		final TCapabilityType result = new TCapabilityType();
		final CapabilityType capabilityType = capType.getValue();
		final QName derivedFrom = getTypeMapperUtil().getCorrectTypeReferenceAsQName(capabilityType.getDerived_from(), ElementType.CAPABILITY_TYPE);

		result.setName(capType.getKey());
		result.setDerivedFrom(parseDerivedFrom(derivedFrom));
		result.getDocumentation().add(toDocumentation(capabilityType.getDescription()));

		if (capabilityType.getProperties() != null && !capabilityType.getProperties().isEmpty()) {
			result.setPropertiesDefinition(parsePropertiesDefinition(capabilityType.getProperties(), capType.getKey()));
		}
		return result;
	}

}
