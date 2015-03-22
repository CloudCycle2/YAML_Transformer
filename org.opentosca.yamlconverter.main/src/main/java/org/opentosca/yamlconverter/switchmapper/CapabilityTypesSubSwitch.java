package org.opentosca.yamlconverter.switchmapper;

import org.opentosca.model.tosca.TCapabilityType;
import org.opentosca.yamlconverter.main.exceptions.NoBaseTypeMappingException;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.CapabilityType;

import java.util.Map.Entry;

public class CapabilityTypesSubSwitch extends AbstractSubSwitch {

	public CapabilityTypesSubSwitch(Yaml2XmlSwitch parentSwitch) {
		super(parentSwitch);
	}

	@Override
	public void process() {
		if (getServiceTemplate().getCapability_types() != null) {
			for (final Entry<String, CapabilityType> capType : getServiceTemplate().getCapability_types().entrySet()) {
				final TCapabilityType ct = createCapabilityType(capType);
				ct.setName(capType.getKey());
				getDefinitions().getServiceTemplateOrNodeTypeOrNodeTypeImplementation().add(ct);
			}
		}
	}

	private TCapabilityType createCapabilityType(Entry<String, CapabilityType> capType) {
		final TCapabilityType result = new TCapabilityType();
		final CapabilityType capabilityType = capType.getValue();
		if (capabilityType.getProperties() != null && !capabilityType.getProperties().isEmpty()) {
			result.setPropertiesDefinition(parsePropertiesDefinition(capabilityType.getProperties(), capType.getKey()));
		}
		result.setName(capType.getKey());
		try {
			result.setDerivedFrom(parseDerivedFrom(BaseTypeMapper.getXmlCapabilityType(capabilityType.getDerived_from())));
		} catch (NoBaseTypeMappingException e) {
			result.setDerivedFrom(parseDerivedFrom(capabilityType.getDerived_from()));
		}
		result.getDocumentation().add(toDocumentation(capabilityType.getDescription()));
		return result;
	}

}
