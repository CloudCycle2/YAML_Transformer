package org.opentosca.yamlconverter.switchmapper.subswitches;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBElement;

import org.opentosca.model.tosca.TCapability;
import org.opentosca.model.tosca.TEntityTemplate;
import org.opentosca.model.tosca.TNodeTemplate;
import org.opentosca.model.tosca.TRelationshipTemplate;
import org.opentosca.model.tosca.TRequirement;
import org.opentosca.yamlconverter.main.utils.AnyMap;
import org.opentosca.yamlconverter.switchmapper.Yaml2XmlSwitch;
import org.opentosca.yamlconverter.switchmapper.typemapper.ElementType;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.NodeTemplate;

/**
 * This class supports processing of node templates of a YAML service template.
 */
public class NodeTemplatesSubSwitch extends AbstractSubSwitch {

	private final Map<TNodeTemplate, List<Map<String, Object>>> relationshipRequirements = new HashMap<TNodeTemplate, List<Map<String, Object>>>();

	public NodeTemplatesSubSwitch(Yaml2XmlSwitch parentSwitch) {
		super(parentSwitch);
	}

	/**
	 * Processes all YAML node templates by creating a {@link org.opentosca.model.tosca.TNodeTemplate} and adding it to
	 * {@link #getTopologyTemplate()}. In the next step, the relationship requirements are processed. This depends on the creation of the
	 * node templates before, because relationships between node templates can only be established when all node templates are processed
	 * completely, because the node template object is needed.
	 */
	@Override
	public void process() {
		if (getServiceTemplate().getNode_templates() != null) {
			for (final Entry<String, NodeTemplate> yamlNodeTemplate : getServiceTemplate().getNode_templates().entrySet()) {
				final TNodeTemplate nodeTemplate = createNodeTemplate(yamlNodeTemplate.getValue(), yamlNodeTemplate.getKey());
				getTopologyTemplate().getNodeTemplateOrRelationshipTemplate().add(nodeTemplate);
			}
			for (final Entry<TNodeTemplate, List<Map<String, Object>>> relationshipRequirement : this.relationshipRequirements.entrySet()) {
				for (final Map<String, Object> requirement : relationshipRequirement.getValue()) {
					processRelationshipRequirements(relationshipRequirement.getKey(), requirement);
				}
			}
		}
	}

	/**
	 * Creates a node template object and processes the inner attributes like capabilities, properties and requirements.
	 *
	 * @param nodeTemplate YAML node template
	 * @param nodeTemplateName name of the node template
	 * @return node template object containing values from {@code nodeTemplate}
	 */
	private TNodeTemplate createNodeTemplate(NodeTemplate nodeTemplate, String nodeTemplateName) {
		final TNodeTemplate result = new TNodeTemplate();
		// first set simple attributes like id, name, etc.
		result.setId(name2id(nodeTemplateName));
		result.setName(nodeTemplateName);
		if (nodeTemplate.getDescription() != null && !nodeTemplate.getDescription().isEmpty()) {
			result.getDocumentation().add(toDocumentation(nodeTemplate.getDescription()));
		}
		result.setType(getTypeMapperUtil().getCorrectTypeReferenceAsQName(nodeTemplate.getType(), ElementType.NODE_TYPE));

		// then process more difficult things
		processCapabilitiesInNodeTemplate(nodeTemplate, result);
		processPropertiesInNodeTemplate(nodeTemplate, nodeTemplateName, result);
		if (nodeTemplate.getRequirements() != null && !nodeTemplate.getRequirements().isEmpty()) {
			processRequirements(nodeTemplate, result);
		}

		return result;
	}

	/**
	 * Processes capabilities from YAML node template.
	 *
	 * @param nodeTemplate YAML node template containing capabilities
	 * @param result Tosca node template
	 */
	private void processCapabilitiesInNodeTemplate(NodeTemplate nodeTemplate, TNodeTemplate result) {
		final TNodeTemplate.Capabilities capabilities = new TNodeTemplate.Capabilities();

		for (final Entry<String, Object> nodeTemplateCapability : nodeTemplate.getCapabilities().entrySet()) {
			if (nodeTemplateCapability.getValue() instanceof HashMap) {
				final Map<?, ?> capabilityDefinition = (Map<?, ?>) nodeTemplateCapability.getValue();
				final TCapability tCapability = new TCapability();
				tCapability.setName(nodeTemplateCapability.getKey());
				String capabilityType = null;
				try {
					capabilityType = (String) capabilityDefinition.get("type");
				} catch (final Exception e) {
					capabilityType = "CAPABILITY_TYPE";
				}
				tCapability.setType(getTypeMapperUtil().getCorrectTypeReferenceAsQName(capabilityType, ElementType.CAPABILITY_TYPE));
				tCapability.setId(result.getId() + "_" + nodeTemplateCapability.getKey());
				// TODO: set properties if any available
				capabilities.getCapability().add(tCapability);
			}
		}
		if (!nodeTemplate.getCapabilities().isEmpty()) {
			result.setCapabilities(capabilities);
		}
	}

	/**
	 * Processes the node template properties by using {@link #getAnyMapForProperties(java.util.Map, String)}.
	 *
	 * @param nodeTemplate YAML node template containing properties
	 * @param nodeTemplateName name of node template
	 * @param result Tosca node template where the properties have to be stored in
	 */
	private void processPropertiesInNodeTemplate(NodeTemplate nodeTemplate, String nodeTemplateName, TNodeTemplate result) {
		final TEntityTemplate.Properties prop = new TEntityTemplate.Properties();
		final JAXBElement<AnyMap> jaxbprop = getAnyMapForProperties(nodeTemplate.getProperties(), getTypeMapperUtil()
				.getCorrectTypeReferenceAsQNameForProperties(nodeTemplate.getType(), ElementType.NODE_TYPE));
		prop.setAny(jaxbprop);
		result.setProperties(prop);
	}

	/**
	 * Process requirements from {@code nodeTemplate} and add them to {@code result}. There are two possible notations of requirements: <br />
	 * 1) requirements: - requirementId: someCapability => will produce: <br>
	 *
	 * <pre>
	 * &lt;Requirements>
	 *    &lt;Requirement id="requirementId" type="tns:someRequirement" />
	 * &lt;/Requirements>
	 * </pre>
	 *
	 * NOTE: There is a convention for "someCapability": It must end with "Capability", otherwise no requirement will be created! <br />
	 * 2) requirements: - relationshipTemplateId: targetElementId relationship_type: someRelationshipType => will produce:<br>
	 *
	 * <pre>
	 * &lt;RelationshipTemplate id="relationshipTemplateId" type="tns:relationshipType">
	 *    &lt;SourceElement ref="{@code result} .id">
	 *    &lt;/SourceElement>
	 *    &lt;TargetElement ref="targetElementId">
	 *    &lt;/TargetElement>
	 * &lt;/RelationshipTemplate>
	 * </pre>
	 *
	 * @param nodeTemplate node template from YAML
	 * @param result corresponding node template for XML
	 * @throws RuntimeException if the requirement does not match "^(\w+)Capability$"
	 */
	private void processRequirements(final NodeTemplate nodeTemplate, final TNodeTemplate result) {
		final TNodeTemplate.Requirements resultRequirements = new TNodeTemplate.Requirements();
		this.relationshipRequirements.put(result, new ArrayList<Map<String, Object>>());

		// process all requirements
		for (final Map<String, Object> requirement : nodeTemplate.getRequirements()) {
			processSingleRequirement(result, resultRequirements, requirement);
		}
		result.setRequirements(resultRequirements);
	}

	/**
	 * For information about what is processed, take a look at
	 * {@link #processRequirements(org.opentosca.yamlconverter.yamlmodel.yaml.element.NodeTemplate, org.opentosca.model.tosca.TNodeTemplate)}
	 *
	 * @param result node template object for XML
	 * @param resultRequirements object containing requirements for {@code result}
	 * @param requirement
	 */
	private void processSingleRequirement(final TNodeTemplate result, final TNodeTemplate.Requirements resultRequirements,
			final Map<String, Object> requirement) {
		if (requirement.containsKey("relationship_type") && requirement.size() == 2) {
			// Here: produce relationship template based on requirement
			// store requirement for later processing, because we need to access the processed node templates
			// -> otherwise it's possible to request a node template which hasn't processed so far
			this.relationshipRequirements.get(result).add(requirement);
		} else if (requirement.size() == 1) {
			// Here: produce requirement by using the capability name
			createRequirement(resultRequirements, requirement);
		} else {
			throw new RuntimeException("This type of requirements definition is not supported.");
		}
	}

	/**
	 * For information about what is processed, take a look at
	 * {@link #processRequirements(org.opentosca.yamlconverter.yamlmodel.yaml.element.NodeTemplate, org.opentosca.model.tosca.TNodeTemplate)}
	 *
	 * @param resultRequirements object storing requirements for a corresponding XML node template
	 * @param requirement map containing requirement attributes; mapping size must be 1
	 */
	private void createRequirement(final TNodeTemplate.Requirements resultRequirements, final Map<String, Object> requirement) {
		final String capability = (String) requirement.values().toArray()[0];
		if (capability.endsWith("Capability")) {
			final String requirementName = (String) requirement.keySet().toArray()[0];
			final String requirementTypeName = capability.replace("Capability", "Requirement");
			createAndAddRequirementType(capability, requirementTypeName);

			// create requirement
			final TRequirement tRequirement = new TRequirement();
			tRequirement.setId(requirementName);
			tRequirement.setName(requirementName);
			tRequirement.setType(getNamespaceUtil().toTnsQName(requirementTypeName));
			resultRequirements.getRequirement().add(tRequirement);
		} else {
			throw new RuntimeException("This type of requirements definition is not supported." + "Convention: name = [...]Capability");
		}
	}

	/**
	 * For information about what is processed, take a look at
	 * {@link #processRequirements(org.opentosca.yamlconverter.yamlmodel.yaml.element.NodeTemplate, org.opentosca.model.tosca.TNodeTemplate)}
	 *
	 * @param result
	 * @param requirement
	 */
	private void processRelationshipRequirements(final TNodeTemplate result, final Map<String, Object> requirement) {
		final TRelationshipTemplate relationshipTemplate = new TRelationshipTemplate();
		// set properties by using values of requirement
		for (final String key : requirement.keySet()) {
			if (key.equals("relationship_type")) {
				final String relationshipType = (String) requirement.get(key);
				relationshipTemplate.setType(getTypeMapperUtil().getCorrectTypeReferenceAsQName(relationshipType,
						ElementType.RELATIONSHIP_TYPE));
			} else {
				relationshipTemplate.setId(key);

				// set source element
				final TRelationshipTemplate.SourceElement source = new TRelationshipTemplate.SourceElement();
				source.setRef(result);
				relationshipTemplate.setSourceElement(source);

				// set target element; if no reference is found, throw exception
				final TRelationshipTemplate.TargetElement target = new TRelationshipTemplate.TargetElement();
				final TNodeTemplate targetTemplate = getTargetNodeTemplate((String) requirement.get(key));
				if (targetTemplate == null) {
					throw new RuntimeException("Illegal reference. " + (String) requirement.get(key) + " is no valid NodeTemplate id.");
				}
				target.setRef(targetTemplate);
				relationshipTemplate.setTargetElement(target);
			}
		}

		getTopologyTemplate().getNodeTemplateOrRelationshipTemplate().add(relationshipTemplate);
	}

	/**
	 * Finds the {@link org.opentosca.model.tosca.TNodeTemplate} with id {@code nodeTemplateId} by searching all available entity templates
	 * from {@link #getTopologyTemplate()} and compare the id's.
	 *
	 * @param nodeTemplateId id of the node template to search for
	 * @return the node template object or null if node template couldn't be found
	 */
	private TNodeTemplate getTargetNodeTemplate(final String nodeTemplateId) {
		for (final TEntityTemplate entityTemplate : getTopologyTemplate().getNodeTemplateOrRelationshipTemplate()) {
			if (entityTemplate instanceof TNodeTemplate) {
				final TNodeTemplate nodeTemplate = (TNodeTemplate) entityTemplate;
				if (nodeTemplate.getId().equals(nodeTemplateId)) {
					return nodeTemplate;
				}
			}
		}
		return null;
	}

}
