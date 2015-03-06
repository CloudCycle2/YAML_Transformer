package org.opentosca.yamlconverter.switchmapper;

import org.opentosca.model.tosca.*;
import org.opentosca.yamlconverter.main.utils.AnyMap;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.NodeTemplate;

import javax.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class NodeTemplatesSubSwitch extends AbstractSubSwitch {

	private Map<TNodeTemplate, List<Map<String, Object>>> relationshipRequirements =
			new HashMap<TNodeTemplate, List<Map<String, Object>>>();

	public NodeTemplatesSubSwitch(Yaml2XmlSwitch parentSwitch) {
		super(parentSwitch);
	}

	@Override
	public void process() {
		if (getServiceTemplate().getNode_templates() != null) {
			for (final Entry<String, NodeTemplate> nt : getServiceTemplate().getNode_templates().entrySet()) {
				final TNodeTemplate xnode = createNodeTemplate(nt.getValue(), nt.getKey());
				getTopologyTemplate().getNodeTemplateOrRelationshipTemplate().add(xnode);
			}
			for (Entry<TNodeTemplate, List<Map<String, Object>>> relationshipRequirement : relationshipRequirements.entrySet()) {
				for (Map<String, Object> requirement : relationshipRequirement.getValue()) {
					processRelationshipRequirements(relationshipRequirement.getKey(), requirement);
				}
			}
		}
	}

	private TNodeTemplate createNodeTemplate(NodeTemplate nodeTemplate, String nodename) {
		final TNodeTemplate result = new TNodeTemplate();
		// first set simple attributes like id, name, etc.
		result.setId(name2id(nodename));
		result.setName(nodename);
		if (nodeTemplate.getDescription() != null && !nodeTemplate.getDescription().isEmpty()) {
			result.getDocumentation().add(toDocumentation(nodeTemplate.getDescription()));
		}
		result.setType(toTnsQName(nodeTemplate.getType()));

		// then process more difficult things
		processCapabilitiesInNodeTemplate(nodeTemplate, result);
		processPropertiesInNodeTemplate(nodeTemplate, nodename, result);
		if (nodeTemplate.getRequirements() != null && !nodeTemplate.getRequirements().isEmpty()) {
			processRequirements(nodeTemplate, result);
		}

		return result;
	}

	private void processCapabilitiesInNodeTemplate(NodeTemplate nodeTemplate, TNodeTemplate result) {
		final TNodeTemplate.Capabilities capabilities = new TNodeTemplate.Capabilities();

		for (final Entry<String, Object> nodeTemplateCapability : nodeTemplate.getCapabilities().entrySet()) {
			if (nodeTemplateCapability.getValue() instanceof HashMap) {
				final Map<?, ?> capabilityDefinition = (Map<?, ?>) nodeTemplateCapability.getValue();
				final TCapability tCapability = new TCapability();
				tCapability.setName(nodeTemplateCapability.getKey());
				String capabilityType = "CAPABILITY_TYPE";
				try {
					capabilityType = (String) capabilityDefinition.get("type");
				} catch (final Exception e) {
					System.out.println("No capability type defined or illegal value, using default.");
				}
				tCapability.setType(toTnsQName(capabilityType));
				tCapability.setId(result.getId() + "_" + nodeTemplateCapability.getKey());
				// TODO: set properties if any available
				capabilities.getCapability().add(tCapability);
			}
		}
		if (!nodeTemplate.getCapabilities().isEmpty()) {
			result.setCapabilities(capabilities);
		}
	}

	private void processPropertiesInNodeTemplate(NodeTemplate nodeTemplate, String nodename, TNodeTemplate result) {
		final TEntityTemplate.Properties prop = new TEntityTemplate.Properties();
		final JAXBElement<AnyMap> jaxbprop = getAnyMapForProperties(nodeTemplate.getProperties(), nodename);
		prop.setAny(jaxbprop);
		result.setProperties(prop);
	}

	/**
	 * Process requirements from {@code nodeTemplate} and add them to {@code result}.
	 * There are two possible notations of requirements:
	 * <br />
	 * 1)
	 * requirements:
	 *   - requirementId: someCapability
	 * => will produce:
	 *   <Requirements>
	 *     <Requirement id="requirementId" type="tns:someRequirement" />
	 *   </Requirements>
	 * NOTE: There is a convention for "someCapability": It must end with "Capability", otherwise no requirement will
	 * be created!
	 * <br />
	 * 2)
	 * requirements:
	 *   - relationshipTemplateId: targetElementId
	 *     relationship_type: someRelationshipType
	 * => will produce:
	 *   <RelationshipTemplate id="relationshipTemplateId" type="tns:relationshipType">
	 *     <SourceElement ref="{@code result}.id"></SourceElement>
	 *     <TargetElement ref="targetElementId"></TargetElement>
	 *   </RelationshipTemplate>
	 * @param nodeTemplate node template from YAML
	 * @param result corresponding node template for XML
	 */
	private void processRequirements(final NodeTemplate nodeTemplate, final TNodeTemplate result) {
		final TNodeTemplate.Requirements resultRequirements = new TNodeTemplate.Requirements();
		this.relationshipRequirements.put(result, new ArrayList<Map<String, Object>>());
		for (Map<String, Object> requirement : nodeTemplate.getRequirements()) {
			if (requirement.containsKey("relationship_type") && requirement.size() == 2) {
				// Here: produce relationship template based on requirement
				// store requirement for later processing, because we need to access the processed node templates
				// -> otherwise it's possible to request a node template which hasn't processed so far
				this.relationshipRequirements.get(result).add(requirement);
			} else if (requirement.size() == 1) {
				// Here: produce requirement by using the capability name
				String requirementName = (String) requirement.keySet().toArray()[0];
				String capability = (String) requirement.values().toArray()[0];
				if (capability.endsWith("Capability")) {
					// TODO: check if requirement type already exists and use this
					// create requirement type for requirement or use existing one
					TRequirementType requirementType = new TRequirementType();
					String requirementTypeName = capability.replace("Capability", "Requirement");
					requirementType.setName(requirementTypeName);
					requirementType.setRequiredCapabilityType(this.toTnsQName(capability));
					getDefinitions().getServiceTemplateOrNodeTypeOrNodeTypeImplementation().add(requirementType);

					// create requirement
					TRequirement tRequirement = new TRequirement();
					tRequirement.setName(requirementName);
					tRequirement.setType(this.toTnsQName(requirementTypeName));
					resultRequirements.getRequirement().add(tRequirement);
				} else {
					throw new RuntimeException("This type of requirements definition is not supported." +
							"Convention: name = [...]Capability");
				}
			} else {
				throw new RuntimeException("This type of requirements definition is not supported.");
			}
		}
		result.setRequirements(resultRequirements);
	}

	/**
	 * For information about what is processed, take a look at
	 * {@link #processRequirements(org.opentosca.yamlconverter.yamlmodel.yaml.element.NodeTemplate,
	 * org.opentosca.model.tosca.TNodeTemplate)}
	 * @param result
	 * @param requirement
	 */
	private void processRelationshipRequirements(final TNodeTemplate result, final Map<String, Object> requirement) {
		TRelationshipTemplate relationshipTemplate = new TRelationshipTemplate();
		// set properties by using values of requirement
		for (String key : requirement.keySet()) {
            if (key.equals("relationship_type")) {
                String relationshipType = (String) requirement.get(key);
                relationshipTemplate.setType(this.toTnsQName(relationshipType));
            } else {
                relationshipTemplate.setId(key);

				// set source element
                TRelationshipTemplate.SourceElement source = new TRelationshipTemplate.SourceElement();
                source.setRef(result);
                relationshipTemplate.setSourceElement(source);

				// set target element; if no reference is found, throw exception
                TRelationshipTemplate.TargetElement target = new TRelationshipTemplate.TargetElement();
                TNodeTemplate targetTemplate = getTargetNodeTemplate(result, (String) requirement.get(key));
                if (targetTemplate == null) {
                    throw new RuntimeException("Illegal reference. "+
                            (String) requirement.get(key) + " is no valid NodeTemplate id.");
                }
                target.setRef(targetTemplate);
                relationshipTemplate.setTargetElement(target);
            }
        }

		getTopologyTemplate().getNodeTemplateOrRelationshipTemplate().add(relationshipTemplate);
	}

	private TNodeTemplate getTargetNodeTemplate(final TNodeTemplate result, final String nodeTemplateId) {
		if (result.getId().equals(nodeTemplateId)) {
			return result;
		}
		for (TEntityTemplate entityTemplate : getTopologyTemplate().getNodeTemplateOrRelationshipTemplate()) {
			if (entityTemplate instanceof TNodeTemplate) {
				TNodeTemplate nodeTemplate = (TNodeTemplate) entityTemplate;
				if (nodeTemplate.getId().equals(nodeTemplateId)) {
					return nodeTemplate;
				}
			}
		}
		return null;
	}

}
