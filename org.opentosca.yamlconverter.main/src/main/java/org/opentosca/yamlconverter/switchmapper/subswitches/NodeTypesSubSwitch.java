package org.opentosca.yamlconverter.switchmapper.subswitches;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.opentosca.model.tosca.TArtifactReference;
import org.opentosca.model.tosca.TArtifactTemplate;
import org.opentosca.model.tosca.TCapabilityDefinition;
import org.opentosca.model.tosca.TEntityTemplate;
import org.opentosca.model.tosca.TImplementationArtifacts;
import org.opentosca.model.tosca.TInterface;
import org.opentosca.model.tosca.TNodeType;
import org.opentosca.model.tosca.TNodeType.CapabilityDefinitions;
import org.opentosca.model.tosca.TNodeType.Interfaces;
import org.opentosca.model.tosca.TNodeType.RequirementDefinitions;
import org.opentosca.model.tosca.TNodeTypeImplementation;
import org.opentosca.model.tosca.TOperation;
import org.opentosca.model.tosca.TRequirementDefinition;
import org.opentosca.yamlconverter.switchmapper.Yaml2XmlSwitch;
import org.opentosca.yamlconverter.switchmapper.typemapper.ElementType;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.NodeType;

/**
 * This class supports processing of node types from a YAML service template.
 */
public class NodeTypesSubSwitch extends AbstractSubSwitch {

	public NodeTypesSubSwitch(Yaml2XmlSwitch parentSwitch) {
		super(parentSwitch);
	}

	/**
	 * Processes every YAML node type and creates a corresponding {@link org.opentosca.model.tosca.TNodeType}. Each node type is added to
	 * {@link #getDefinitions()} object as well as {@link org.opentosca.model.tosca.TArtifactTemplate} and
	 * {@link org.opentosca.model.tosca.TNodeTypeImplementation} which will be created in the process, too.
	 */
	@Override
	public void process() {
		if (getServiceTemplate().getNode_types() != null) {
			for (final Entry<String, NodeType> yamlNodeType : getServiceTemplate().getNode_types().entrySet()) {
				final TNodeType nodeType = createNodeType(yamlNodeType.getValue(), yamlNodeType.getKey());
				getDefinitions().getServiceTemplateOrNodeTypeOrNodeTypeImplementation().add(nodeType);
			}
		}
	}

	/**
	 * Creates a node type, node type implementation and a list of artifact templates. Sets some basic attributes and calls
	 * {@link #parseNodeTypeAttributes(org.opentosca.yamlconverter.yamlmodel.yaml.element.NodeType, String, org.opentosca.model.tosca.TNodeType, java.util.List, org.opentosca.model.tosca.TImplementationArtifacts)}
	 * to process each node type attribute.
	 *
	 * @param value YAML node type
	 * @param name name of YAML node type
	 * @return Tosca node type
	 */
	private TNodeType createNodeType(NodeType value, String name) {
		final TNodeType result = new TNodeType();
		result.setName(name);
		result.setTargetNamespace(getTargetNamespace());

		final TNodeTypeImplementation nodeTypeImplementation = new TNodeTypeImplementation();
		final List<TArtifactTemplate> artifactTemplates = new ArrayList<TArtifactTemplate>();
		final TImplementationArtifacts implementationArtifacts = new TImplementationArtifacts();
		nodeTypeImplementation.setImplementationArtifacts(implementationArtifacts);
		nodeTypeImplementation.setName(name + "Implementation");
		nodeTypeImplementation.setNodeType(getTypeMapperUtil().getCorrectTypeReferenceAsQName(result.getName(), ElementType.NODE_TYPE));

		parseNodeTypeAttributes(value, name, result, artifactTemplates, implementationArtifacts);

		getDefinitions().getServiceTemplateOrNodeTypeOrNodeTypeImplementation().addAll(artifactTemplates);
		getDefinitions().getServiceTemplateOrNodeTypeOrNodeTypeImplementation().add(nodeTypeImplementation);

		return result;
	}

	private void parseNodeTypeAttributes(final NodeType value, final String name, final TNodeType result,
			final List<TArtifactTemplate> artifactTemplates, final TImplementationArtifacts implementationArtifacts) {
		if (value.getArtifacts() != null && !value.getArtifacts().isEmpty()) {
			// here are only artifact definitions!!
			parseNodeTypeArtifacts(value.getArtifacts(), artifactTemplates, implementationArtifacts);
		}
		if (value.getCapabilities() != null && !value.getCapabilities().isEmpty()) {
			result.setCapabilityDefinitions(parseNodeTypeCapabilities(value.getCapabilities()));
		}
		if (value.getDerived_from() != null && !value.getDerived_from().isEmpty()) {
			result.setDerivedFrom(parseDerivedFrom(getTypeMapperUtil().getCorrectTypeReferenceAsQName(value.getDerived_from(),
					ElementType.NODE_TYPE)));
		}
		if (value.getInterfaces() != null && !value.getInterfaces().isEmpty()) {
			final Interfaces nodeTypeInterfaces = parseNodeTypeInterfaces(value.getInterfaces());
			addInterfaceDefinitionsToImplementationArtifacts(implementationArtifacts, nodeTypeInterfaces);
			result.setInterfaces(nodeTypeInterfaces);
		}
		if (value.getProperties() != null && !value.getProperties().isEmpty()) {
			result.setPropertiesDefinition(parsePropertiesDefinition(value.getProperties(), name));
		}
		if (value.getRequirements() != null && !value.getRequirements().isEmpty()) {
			result.setRequirementDefinitions(parseNodeTypeRequirementDefinitions(value.getRequirements()));
		}
		if (value.getDescription() != null && !value.getDescription().isEmpty()) {
			result.getDocumentation().add(toDocumentation(value.getDescription()));
		}
	}

	private RequirementDefinitions parseNodeTypeRequirementDefinitions(List<Map<String, String>> requirements) {
		final RequirementDefinitions result = new RequirementDefinitions();
		for (final Map<String, String> requirement : requirements) {
			final TRequirementDefinition requirementDefinition = new TRequirementDefinition();
			if (requirement.size() == 1) {
				final String capability = (String) requirement.values().toArray()[0];
				final String requirementName = (String) requirement.keySet().toArray()[0];
				final String requirementTypeName = capability.replace("Capability", "Requirement");
				createAndAddRequirementType(capability, requirementTypeName);
				requirementDefinition.setRequirementType(getNamespaceUtil().toTnsQName(requirementTypeName));
				requirementDefinition.setName(requirementName);
			}
			result.getRequirementDefinition().add(requirementDefinition);
		}
		return result;
	}

	private void addInterfaceDefinitionsToImplementationArtifacts(TImplementationArtifacts implementationArtifacts,
			Interfaces nodeTypeInterfaces) {
		// TODO: find a better solution to map interfaces to implementation artifacts
		for (final TInterface tInterface : nodeTypeInterfaces.getInterface()) {
			for (final TOperation tOperation : tInterface.getOperation()) {
				for (final TImplementationArtifacts.ImplementationArtifact implArtifact : implementationArtifacts
						.getImplementationArtifact()) {
					if (implArtifact.getArtifactRef().getLocalPart().contains(tOperation.getName())) {
						implArtifact.setInterfaceName(tInterface.getName());
						implArtifact.setOperationName(tOperation.getName());
					}
				}
			}
		}
	}

	private Interfaces parseNodeTypeInterfaces(Map<String, Map<String, Map<String, String>>> interfaces) {
		final Interfaces result = new Interfaces();
		for (final Entry<String, Map<String, Map<String, String>>> entry : interfaces.entrySet()) {
			final TInterface inf = getInterfaceWithOperations(entry);
			result.getInterface().add(inf);
		}
		return result;
	}

	/**
	 * Creates a {@link org.opentosca.model.tosca.TNodeType.CapabilityDefinitions} object. Eventually a default for the capability type is
	 * used.
	 *
	 * @param capabilities map containing capabilities with some definitions
	 * @return an object containing all capability definitions
	 */
	private CapabilityDefinitions parseNodeTypeCapabilities(Map<String, Object> capabilities) {
		final CapabilityDefinitions result = new CapabilityDefinitions();
		for (final Entry<String, Object> capabilityEntry : capabilities.entrySet()) {
			final TCapabilityDefinition capabilityDefinition = new TCapabilityDefinition();
			capabilityDefinition.setName(capabilityEntry.getKey());
			if (capabilityEntry.getValue() instanceof HashMap) {
				final Map<?, ?> capability = (Map<?, ?>) capabilityEntry.getValue();
				String capabilityType = null;
				try {
					capabilityType = (String) capability.get("type");
				} catch (final Exception e) {
					capabilityType = "CAPABILITY_TYPE";
				}
				capabilityDefinition.setCapabilityType(getTypeMapperUtil().getCorrectTypeReferenceAsQName(capabilityType,
						ElementType.CAPABILITY_TYPE));
			}
			result.getCapabilityDefinition().add(capabilityDefinition);
		}
		return result;
	}

	/**
	 * Parse node type artifacts. For each artifact a name, file uri, type and properties must be set. Description and mime type are
	 * optional and not processed currently.
	 *
	 * @param artifacts
	 * @param artifactTemplates
	 * @param implementationArtifacts
	 */
	private void parseNodeTypeArtifacts(List<Map<String, Object>> artifacts, List<TArtifactTemplate> artifactTemplates,
			TImplementationArtifacts implementationArtifacts) {
		for (final Map<String, Object> artifact : artifacts) {
			String artifactName = "";
			String artifactFileUri = "";
			String artifactType = "";
			String artifactDescription = "";
			String artifactMimeType = "";
			Map<String, Object> additionalProperties = new HashMap<String, Object>();

			for (final Entry<String, Object> artifactEntry : artifact.entrySet()) {
				final Object value = artifactEntry.getValue();
				switch (artifactEntry.getKey()) {
				case "type":
					artifactType = (String) value;
					break;
				case "description":
					artifactDescription = (String) value;
					break;
				case "mime_type":
					artifactMimeType = (String) value;
					break;
				case "properties":
					if (value instanceof Map<?, ?>) {
						additionalProperties = (Map) value;
					}
					break;
				default:
					artifactName = artifactEntry.getKey();
					if (value instanceof String) {
						artifactFileUri = (String) value;
					}
					break;
				}
			}
			addArtifactTemplate(artifactTemplates, artifactName, artifactFileUri, artifactType, additionalProperties);
			addImplementationArtifact(implementationArtifacts, artifactName, artifactType);
		}
	}

	private void addImplementationArtifact(TImplementationArtifacts implementationArtifacts, String artifactName, String artifactType) {
		final TImplementationArtifacts.ImplementationArtifact implementationArtifact = new TImplementationArtifacts.ImplementationArtifact();
		implementationArtifact.setArtifactRef(getNamespaceUtil().toTnsQName(artifactName));
		implementationArtifact.setArtifactType(getTypeMapperUtil().getCorrectTypeReferenceAsQName(artifactType, ElementType.ARTIFACT_TYPE));
		implementationArtifacts.getImplementationArtifact().add(implementationArtifact);
	}

	private TArtifactTemplate addArtifactTemplate(List<TArtifactTemplate> artifactTemplates, String artifactName, String artifactFileUri,
			String artifactType, Map<String, Object> additionalProperties) {
		final TArtifactTemplate artifactTemplate = new TArtifactTemplate();
		artifactTemplate.setName(artifactName);
		artifactTemplate.setId(artifactName);
		artifactTemplate.setType(getTypeMapperUtil().getCorrectTypeReferenceAsQName(artifactType, ElementType.ARTIFACT_TYPE));

		setArtifactReferencesForArtifactTemplate(artifactFileUri, artifactTemplate);

		final TEntityTemplate.Properties properties = new TEntityTemplate.Properties();
		properties.setAny(getAnyMapForProperties(additionalProperties, artifactType));
		artifactTemplate.setProperties(properties);

		artifactTemplates.add(artifactTemplate);

		return artifactTemplate;
	}

	private void setArtifactReferencesForArtifactTemplate(final String artifactFileUri, final TArtifactTemplate artifactTemplate) {
		final TArtifactTemplate.ArtifactReferences artifactReferences = new TArtifactTemplate.ArtifactReferences();
		final TArtifactReference artifactReference = new TArtifactReference();
		final TArtifactReference.Include include = new TArtifactReference.Include();
		final int lastIndexOfSlash = artifactFileUri.lastIndexOf("/");
		if (lastIndexOfSlash > 0) {
			artifactReference.setReference(artifactFileUri.substring(0, lastIndexOfSlash));
			include.setPattern(artifactFileUri.substring(artifactFileUri.lastIndexOf("/")));
		} else {
			artifactReference.setReference("");
			include.setPattern(artifactFileUri);
		}
		artifactReference.getIncludeOrExclude().add(include);
		artifactReferences.getArtifactReference().add(artifactReference);
		artifactTemplate.setArtifactReferences(artifactReferences);
	}

}
