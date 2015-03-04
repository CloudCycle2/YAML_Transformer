package org.opentosca.yamlconverter.switchmapper;

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
import org.opentosca.yamlconverter.yamlmodel.yaml.element.NodeType;

public class NodeTypesSubSwitch extends AbstractSubSwitch {

	public NodeTypesSubSwitch(Yaml2XmlSwitch parentSwitch) {
		super(parentSwitch);
	}

	@Override
	public void process() {
		if (getServiceTemplate().getNode_types() != null) {
			for (final Entry<String, NodeType> nt : getServiceTemplate().getNode_types().entrySet()) {
				final TNodeType xnode = createNodeType(nt.getValue(), nt.getKey());
				getDefinitions().getServiceTemplateOrNodeTypeOrNodeTypeImplementation().add(xnode);
			}
		}
	}

	private TNodeType createNodeType(NodeType value, String name) {
		final TNodeType result = new TNodeType();
		result.setName(name);

		final TNodeTypeImplementation nodeTypeImplementation = new TNodeTypeImplementation();
		final List<TArtifactTemplate> artifactTemplates = new ArrayList<TArtifactTemplate>();
		final TImplementationArtifacts implementationArtifacts = new TImplementationArtifacts();
		nodeTypeImplementation.setImplementationArtifacts(implementationArtifacts);
		nodeTypeImplementation.setName(name + "Implementation");

		if (value.getArtifacts() != null && !value.getArtifacts().isEmpty()) {
			// here are only artifact definitions!!
			parseNodeTypeArtifacts(value.getArtifacts(), artifactTemplates, implementationArtifacts);
		}
		if (value.getCapabilities() != null && !value.getCapabilities().isEmpty()) {
			result.setCapabilityDefinitions(parseNodeTypeCapabilities(value.getCapabilities()));
		}
		if (value.getDerived_from() != null && !value.getDerived_from().isEmpty()) {
			result.setDerivedFrom(parseDerivedFrom(value.getDerived_from()));
		}
		if (value.getInterfaces() != null && !value.getInterfaces().isEmpty()) {
			final Interfaces nodeTypeInterfaces = parseNodeTypeInterfaces(value.getInterfaces());
			addInterfaceDefinitionsToImplementationArtifacts(implementationArtifacts, nodeTypeInterfaces);
			result.setInterfaces(nodeTypeInterfaces);
		}
		if (value.getProperties() != null && !value.getProperties().isEmpty()) {
			result.setPropertiesDefinition(parsePropertiesDefinition(value.getProperties(), name));
		}
		// TODO: This does not work and crashes Winery!
		// if (value.getRequirements() != null && !value.getRequirements().isEmpty()) {
		// result.setRequirementDefinitions(parseNodeTypeRequirementDefinitions(value.getRequirements()));
		// }
		if (value.getDescription() != null && !value.getDescription().isEmpty()) {
			result.getDocumentation().add(toDocumentation(value.getDescription()));
		}

		getDefinitions().getServiceTemplateOrNodeTypeOrNodeTypeImplementation().addAll(artifactTemplates);
		getDefinitions().getServiceTemplateOrNodeTypeOrNodeTypeImplementation().add(nodeTypeImplementation);

		return result;
	}

	private RequirementDefinitions parseNodeTypeRequirementDefinitions(List<Map<String, Object>> requirements) {
		final RequirementDefinitions result = new RequirementDefinitions();
		for (final Map<String, Object> req : requirements) {
			final TRequirementDefinition rd = new TRequirementDefinition();
			if (req.size() == 2) {
				for (final String key : req.keySet()) {
					if (key.equals("relationship_type")) {
						rd.setRequirementType(toTnsQName((String) req.get(key)));
					} else {
						rd.setName(key);
					}
				}
			} else if (req.size() == 1) {
				rd.setName((String) req.keySet().toArray()[0]);
			}
			result.getRequirementDefinition().add(rd);
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

	private CapabilityDefinitions parseNodeTypeCapabilities(Map<String, Object> capabilities) {
		final CapabilityDefinitions result = new CapabilityDefinitions();
		for (final Entry<String, Object> capabilityEntry : capabilities.entrySet()) {
			final TCapabilityDefinition capabilityDefinition = new TCapabilityDefinition();
			capabilityDefinition.setName(capabilityEntry.getKey());
			if (capabilityEntry.getValue() instanceof HashMap) {
				final Map<?, ?> capability = (Map<?, ?>) capabilityEntry.getValue();
				String capabilityType = "CAPABILITY_TYPE";
				try {
					capabilityType = (String) capability.get("type");
				} catch (final Exception e) {
					System.out.println("No capability type defined or illegal value, using default.");
				}
				capabilityDefinition.setCapabilityType(toTnsQName(capabilityType));
			}
			result.getCapabilityDefinition().add(capabilityDefinition);
		}
		return result;
	}

	private void parseNodeTypeArtifacts(List<Map<String, Object>> artifacts, List<TArtifactTemplate> artifactTemplates,
			TImplementationArtifacts implementationArtifacts) {
		for (final Map<String, Object> artifact : artifacts) {
			String artifactName = "";
			String artifactFileUri = "";
			String artifactType = "";
			String artifactDescription = "";
			String artifactMimeType = "";
			Map<String, Object> additionalProperties = null;

			for (final Entry<String, Object> artifactEntry : artifact.entrySet()) {
				switch (artifactEntry.getKey()) {
				case "type":
					artifactType = (String) artifactEntry.getValue();
					break;
				case "description":
					artifactDescription = (String) artifactEntry.getValue();
					break;
				case "mime_type":
					artifactMimeType = (String) artifactEntry.getValue();
					break;
				case "properties":
					if (artifactEntry.getValue() instanceof Map<?, ?>) {
						additionalProperties = (Map) artifactEntry.getValue();
					}
					break;
				default:
					artifactName = artifactEntry.getKey();
					if (artifactEntry.getValue() instanceof String) {
						artifactFileUri = (String) artifactEntry.getValue();
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
		implementationArtifact.setArtifactRef(toTnsQName(artifactName));
		implementationArtifact.setArtifactType(toTnsQName(artifactType));
		implementationArtifacts.getImplementationArtifact().add(implementationArtifact);
	}

	private TArtifactTemplate addArtifactTemplate(List<TArtifactTemplate> artifactTemplates, String artifactName, String artifactFileUri,
			String artifactType, Map<String, Object> additionalProperties) {
		final TArtifactTemplate artifactTemplate = new TArtifactTemplate();
		artifactTemplate.setName(artifactName);
		artifactTemplate.setId(artifactName);
		artifactTemplate.setType(toTnsQName(artifactType));

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

		final TEntityTemplate.Properties properties = new TEntityTemplate.Properties();
		properties.setAny(getAnyMapForProperties(additionalProperties, artifactType));
		artifactTemplate.setProperties(properties);

		artifactTemplates.add(artifactTemplate);

		return artifactTemplate;
	}

}
