package org.opentosca.yamlconverter.switchmapper;

import org.opentosca.model.tosca.TInterface;
import org.opentosca.model.tosca.TRelationshipType;
import org.opentosca.yamlconverter.switchmapper.typemapper.ElementType;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.RelationshipType;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class RelationshipTypesSubSwitch extends AbstractSubSwitch {

	public RelationshipTypesSubSwitch(Yaml2XmlSwitch parentSwitch) {
		super(parentSwitch);
	}

	@Override
	public void process() {
		if (getServiceTemplate().getRelationship_types() != null) {
			for (final Entry<String, RelationshipType> relType : getServiceTemplate().getRelationship_types().entrySet()) {
				final TRelationshipType rt = createRelationshipType(relType);
				rt.setName(relType.getKey());
				getDefinitions().getServiceTemplateOrNodeTypeOrNodeTypeImplementation().add(rt);
			}
		}
	}

	private TRelationshipType createRelationshipType(Entry<String, RelationshipType> relType) {
		final TRelationshipType result = new TRelationshipType();
		final RelationshipType relationshipType = relType.getValue();
		result.setName(relType.getKey());
		result.setDerivedFrom(parseDerivedFrom(getCorrectTypeReferenceAsQName(relationshipType.getDerived_from(), ElementType.RELATIONSHIP_TYPE)));

		// set interfaces
		final TRelationshipType.TargetInterfaces targetInterfaces = new TRelationshipType.TargetInterfaces();
		for (final Entry<String, Map<String, Map<String, String>>> ifaceEntry : relationshipType.getInterfaces().entrySet()) {
			if (ifaceEntry.getValue() instanceof HashMap) {
				final TInterface tInterface = getInterfaceWithOperations(ifaceEntry);
				targetInterfaces.getInterface().add(tInterface);
			}
		}
		result.setTargetInterfaces(targetInterfaces);

		// set properties
		if (relationshipType.getProperties() != null && !relationshipType.getProperties().isEmpty()) {
			result.setPropertiesDefinition(parsePropertiesDefinition(relationshipType.getProperties(), relType.getKey()));
		}

		// set valid target (only one possible, thus choose first one)
		if (relationshipType.getValid_targets().length > 0 && relationshipType.getValid_targets()[0] != null) {
			final TRelationshipType.ValidTarget validTarget = new TRelationshipType.ValidTarget();
			validTarget.setTypeRef(toTnsQName(relationshipType.getValid_targets()[0]));
			result.setValidTarget(validTarget);
		}
		result.getDocumentation().add(toDocumentation(relationshipType.getDescription()));
		return result;
	}

}
