package org.opentosca.yamlconverter.switchmapper;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlWriter;
import org.opentosca.model.tosca.*;
import org.opentosca.model.tosca.TEntityType.DerivedFrom;
import org.opentosca.model.tosca.TEntityType.PropertiesDefinition;
import org.opentosca.model.tosca.TNodeType.CapabilityDefinitions;
import org.opentosca.model.tosca.TNodeType.Interfaces;
import org.opentosca.model.tosca.TNodeType.RequirementDefinitions;
import org.opentosca.yamlconverter.main.utils.AnyMap;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.*;

import javax.xml.namespace.QName;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class can parse ServiceTemplates (YAML bean) to Definitions (XML bean).
 *
 */
public class Yaml2XmlSwitch {
	/**
	 * The XML-Namespace of XML-Schemas.
	 */
	private static final String XMLSCHEMA_NS = "http://www.w3.org/2001/XMLSchema";

	/**
	 * The XML-Namespace of the created document.
	 */
	private static final String NS = "http://www.example.org/tosca/yamlgen";

	/**
	 * The default user input.
	 */
	private static final String DEFAULT_USER_INPUT = "DEFAULTUSERINPUT";

	/**
	 * The XML-Namespace of the types.
	 */
	private static String TYPESNS = "http://www.example.org/tosca/yamlgen/types";

	/**
	 * A counter for creating unique IDs.
	 */
	private long uniqueID = 0;

	/**
	 * StringBuilder for the XSD.
	 */
	private StringBuilder xsd;

	/**
	 * The service template to parse.
	 */
	private ServiceTemplate st;

	/**
	 * InputVarName -> InputVarValue
	 */
	private Map<String, String> inputs = new HashMap<>();

	private Definitions toscaResult = new Definitions();

	/**
	 * Parses {@link ServiceTemplate} to {@link Definitions}.
	 *
	 * @param st the {@link ServiceTemplate} to parse
	 * @return the parsed {@link Definitions}
	 */
	public Definitions parse(ServiceTemplate st) {
		this.xsd = new StringBuilder(); // reset
		this.st = st;
		return processServiceTemplate(st);
	}

	/**
	 * Returns an additional XSD to support the properties of templates.
	 *
	 * @return additional XSD.
	 */
	public String getXSD() {
		final String pre = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\""
				+ "targetNamespace=\"" + TYPESNS + "\" xmlns=\"" + TYPESNS + "\">\n";
		final String post = "</xs:schema>";
		return pre + this.xsd.toString() + post;
	}

	/**
	 * Getter for InputVariables.
	 *
	 * @return InputVarName -> InputVarValue
	 */
	private Map<String, String> getInputs() {
		return this.inputs;
	}

	/**
	 * Set the map for Input-Variables.
	 *
	 * @param inputs InputVarName -> InputVarValue
	 */
	public void setInputs(Map<String, String> inputs) {
		this.inputs = inputs;
	}

	private Definitions processServiceTemplate(ServiceTemplate yamlServiceTemplate) {
		final TServiceTemplate serviceTemplate = new TServiceTemplate();
		final TTopologyTemplate topologyTemplate = new TTopologyTemplate();
		setInitialProperties(yamlServiceTemplate, serviceTemplate, topologyTemplate);

		processCapabilityTypes(yamlServiceTemplate);
		processRelationshipTypes(yamlServiceTemplate);
		processArtifactTypes(yamlServiceTemplate);
		processNodeTypes(yamlServiceTemplate);
		processImports(yamlServiceTemplate);
		processNodeTemplates(yamlServiceTemplate, topologyTemplate);

		this.toscaResult.getImport().add(createTypeImport());

		return this.toscaResult;
	}

	private void setInitialProperties(ServiceTemplate yamlServiceTemplate, TServiceTemplate serviceTemplate,
			TTopologyTemplate topologyTemplate) {
		this.toscaResult.setId(unique("root"));
		this.toscaResult.setName(unique("Root"));
		if (yamlServiceTemplate.getTosca_default_namespace() != null && !yamlServiceTemplate.getTosca_default_namespace().isEmpty()) {
			this.toscaResult.setTargetNamespace(yamlServiceTemplate.getTosca_default_namespace());
		} else {
			this.toscaResult.setTargetNamespace(NS);
		}
		this.toscaResult.getServiceTemplateOrNodeTypeOrNodeTypeImplementation().add(serviceTemplate);
		this.toscaResult.getDocumentation().add(toDocumentation(yamlServiceTemplate.getDescription()));
		if (yamlServiceTemplate.getTemplate_author() != null && !yamlServiceTemplate.getTemplate_author().isEmpty()) {
			this.toscaResult.getDocumentation().add(toDocumentation("Template Author: " + yamlServiceTemplate.getTemplate_author()));
		}
		if (yamlServiceTemplate.getTemplate_version() != null && !yamlServiceTemplate.getTemplate_version().isEmpty()) {
			this.toscaResult.getDocumentation().add(toDocumentation("Template Version: " + yamlServiceTemplate.getTemplate_version()));
		}
		this.toscaResult.getOtherAttributes().put(new QName("xmlns:types"), TYPESNS);
		if (yamlServiceTemplate.getTemplate_name() != null && !yamlServiceTemplate.getTemplate_name().isEmpty()) {
			serviceTemplate.setId(unique(yamlServiceTemplate.getTemplate_name()));
			serviceTemplate.setName(yamlServiceTemplate.getTemplate_name());
		} else {
			serviceTemplate.setId(unique("servicetemplate"));
			serviceTemplate.setName("ServiceTemplate");
		}
		serviceTemplate.setTopologyTemplate(topologyTemplate);
	}

	private void processCapabilityTypes(ServiceTemplate yamlServiceTemplate) {
		if (yamlServiceTemplate.getCapability_types() != null) {
			for (final Entry<String, CapabilityType> capType : yamlServiceTemplate.getCapability_types().entrySet()) {
				final TCapabilityType ct = createCapabilityType(capType);
				ct.setName(capType.getKey());
				this.toscaResult.getServiceTemplateOrNodeTypeOrNodeTypeImplementation().add(ct);
			}
		}
	}

	private void processNodeTypes(ServiceTemplate yamlServiceTemplate) {
		if (yamlServiceTemplate.getNode_types() != null) {
			for (final Entry<String, NodeType> nt : yamlServiceTemplate.getNode_types().entrySet()) {
				final TNodeType xnode = createNodeType(nt.getValue(), nt.getKey());
				this.toscaResult.getServiceTemplateOrNodeTypeOrNodeTypeImplementation().add(xnode);
			}
		}
	}

	private void processRelationshipTypes(ServiceTemplate yamlServiceTemplate) {
		if (yamlServiceTemplate.getRelationship_types() != null) {
			for (final Entry<String, RelationshipType> relType : yamlServiceTemplate.getRelationship_types().entrySet()) {
				final TRelationshipType rt = createRelationshipType(relType);
				rt.setName(relType.getKey());
				this.toscaResult.getServiceTemplateOrNodeTypeOrNodeTypeImplementation().add(rt);
			}
		}
	}

	private void processArtifactTypes(ServiceTemplate yamlServiceTemplate) {
		if (yamlServiceTemplate.getArtifact_types() != null) {
			this.toscaResult.getServiceTemplateOrNodeTypeOrNodeTypeImplementation().addAll(
					getArtifactTypesFromYaml(yamlServiceTemplate.getArtifact_types()));
		}
	}

	private void processImports(ServiceTemplate yamlServiceTemplate) {
		if (yamlServiceTemplate.getImports() != null) {
			// for (final Entry<String, Import> importelem : elem.getImports().entrySet()) {
			// TODO: How do we handle imports?
			// result.getImport().add(case_Import(importelem));
			// }
		}
	}

	private void processNodeTemplates(ServiceTemplate yamlServiceTemplate, TTopologyTemplate topologyTemplate) {
		if (yamlServiceTemplate.getNode_templates() != null) {
			for (final Entry<String, NodeTemplate> nt : yamlServiceTemplate.getNode_templates().entrySet()) {
				final TNodeTemplate xnode = createNodeTemplate(nt.getValue(), nt.getKey());
				topologyTemplate.getNodeTemplateOrRelationshipTemplate().add(xnode);
			}
		}
	}

	/**
	 * Read artifact types from yaml. Example notation: artifacts: artifact_name: derived_from: tosca.artifact.Root mime_type:
	 * application/java-archive
	 *
	 * @param objArtifactTypes
	 * @return
	 */
	private List<TArtifactType> getArtifactTypesFromYaml(Map<String, ArtifactType> objArtifactTypes) {
		final List<TArtifactType> artifactTypes = new ArrayList<TArtifactType>();

		for (final Entry<String, ArtifactType> entry : objArtifactTypes.entrySet()) {
			final TArtifactType artifactType = new TArtifactType();
			artifactType.setName(entry.getKey());

			final ArtifactType value = entry.getValue();
			artifactType.setDerivedFrom(parseDerivedFrom(value.getDerived_from()));
			artifactType.setPropertiesDefinition(parsePropertiesDefinition(value.getProperties(), entry.getKey()));

			artifactTypes.add(artifactType);
		}

		return artifactTypes;
	}

	private TImport createTypeImport() {
		final TImport result = new TImport();
		result.setImportType(XMLSCHEMA_NS);
		result.setLocation("types.xsd");
		result.setNamespace(TYPESNS);
		return result;
	}

	private TRelationshipType createRelationshipType(Entry<String, RelationshipType> relType) {
		final TRelationshipType result = new TRelationshipType();
		final RelationshipType relationshipType = relType.getValue();
		result.setName(relType.getKey());
		result.setDerivedFrom(parseDerivedFrom(relationshipType.getDerived_from()));

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
			validTarget.setTypeRef(new QName(relationshipType.getValid_targets()[0]));
			result.setValidTarget(validTarget);
		}
		result.getDocumentation().add(toDocumentation(relationshipType.getDescription()));
		return result;
	}

	private TCapabilityType createCapabilityType(Entry<String, CapabilityType> capType) {
		final TCapabilityType result = new TCapabilityType();
		final CapabilityType capabilityType = capType.getValue();
		if (capabilityType.getProperties() != null && !capabilityType.getProperties().isEmpty()) {
			result.setPropertiesDefinition(parsePropertiesDefinition(capabilityType.getProperties(), capType.getKey()));
		}
		result.setName(capType.getKey());
		result.setDerivedFrom(parseDerivedFrom(capabilityType.getDerived_from()));
		result.getDocumentation().add(toDocumentation(capabilityType.getDescription()));
		return result;
	}

	private TNodeType createNodeType(NodeType value, String name) {
		final TNodeType result = new TNodeType();
		result.setName(name);

		final TNodeTypeImplementation nodeTypeImplementation = new TNodeTypeImplementation();
		final List<TArtifactTemplate> artifactTemplates = new ArrayList<TArtifactTemplate>();
		final TImplementationArtifacts implementationArtifacts = new TImplementationArtifacts();
		nodeTypeImplementation.setImplementationArtifacts(implementationArtifacts);
		nodeTypeImplementation.setName(name+"Implementation");

		if (value.getArtifacts() != null) {
			// here are only artifact definitions!!
			parseNodeTypeArtifacts(value.getArtifacts(), artifactTemplates, implementationArtifacts);
		}
		if (value.getCapabilities() != null) {
			result.setCapabilityDefinitions(parseNodeTypeCapabilities(value.getCapabilities()));
		}
		if (value.getDerived_from() != null) {
			result.setDerivedFrom(parseDerivedFrom(value.getDerived_from()));
		}
		if (value.getInterfaces() != null) {
			final Interfaces nodeTypeInterfaces = parseNodeTypeInterfaces(value.getInterfaces());
			addInterfaceDefinitionsToImplementationArtifacts(implementationArtifacts, nodeTypeInterfaces);
			result.setInterfaces(nodeTypeInterfaces);
		}
		if (value.getProperties() != null) {
			result.setPropertiesDefinition(parsePropertiesDefinition(value.getProperties(), name));
		}
		if (value.getRequirements() != null) {
			result.setRequirementDefinitions(parseNodeTypeRequirementDefinitions(value.getRequirements()));
		}
		if (value.getDescription() != null) {
			result.getDocumentation().add(toDocumentation(value.getDescription()));
		}

		this.toscaResult.getServiceTemplateOrNodeTypeOrNodeTypeImplementation().addAll(artifactTemplates);
		this.toscaResult.getServiceTemplateOrNodeTypeOrNodeTypeImplementation().add(nodeTypeImplementation);

		return result;
	}

	private void addInterfaceDefinitionsToImplementationArtifacts(final TImplementationArtifacts implementationArtifacts,
																  final Interfaces nodeTypeInterfaces) {
		// TODO: find a better solution to map interfaces to implementation artifacts
		for (TInterface tInterface : nodeTypeInterfaces.getInterface()) {
			for (TOperation tOperation : tInterface.getOperation()) {
				for (TImplementationArtifacts.ImplementationArtifact implArtifact : implementationArtifacts.getImplementationArtifact()) {
					if (implArtifact.getArtifactRef().getLocalPart().contains(tOperation.getName())) {
						implArtifact.setInterfaceName(tInterface.getName());
						implArtifact.setOperationName(tOperation.getName());
					}
				}
			}
		}
	}

	private void parseNodeTypeArtifacts(List<Map<String, Object>> artifacts, List<TArtifactTemplate> artifactTemplates,
										TImplementationArtifacts implementationArtifacts) {

		for (Map<String, Object> artifact : artifacts) {
			String artifactName = "";
			String artifactFileUri = "";
			String artifactType = "";
			String artifactDescription = "";
			String artifactMimeType = "";
			Map additionalProperties = null;

			for (Entry<String, Object> artifactEntry : artifact.entrySet()) {
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

	private TArtifactTemplate addArtifactTemplate(final List<TArtifactTemplate> artifactTemplates, final String artifactName,
									 final String artifactFileUri, final String artifactType, final Map additionalProperties) {
		TArtifactTemplate artifactTemplate = new TArtifactTemplate();
		artifactTemplate.setName(artifactName);
		artifactTemplate.setId(artifactName);
		artifactTemplate.setType(new QName(TYPESNS, artifactType));

		TArtifactTemplate.ArtifactReferences artifactReferences = new TArtifactTemplate.ArtifactReferences();
		TArtifactReference artifactReference = new TArtifactReference();
		TArtifactReference.Include include = new TArtifactReference.Include();
		int lastIndexOfSlash = artifactFileUri.lastIndexOf("/");
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

		TEntityTemplate.Properties properties = new TEntityTemplate.Properties();
		properties.setAny(new AnyMap(parseProperties(additionalProperties, artifactType)));
		artifactTemplate.setProperties(properties);

		artifactTemplates.add(artifactTemplate);

		return artifactTemplate;
	}

	private void addImplementationArtifact(final TImplementationArtifacts implementationArtifacts,
										   final String artifactName, final String artifactType) {
		TImplementationArtifacts.ImplementationArtifact implementationArtifact =
				new TImplementationArtifacts.ImplementationArtifact();
		implementationArtifact.setArtifactRef(new QName(artifactName));
		implementationArtifact.setArtifactType(new QName(artifactType));
		implementationArtifacts.getImplementationArtifact().add(implementationArtifact);
	}

	private RequirementDefinitions parseNodeTypeRequirementDefinitions(List<Map<String, Object>> requirements) {
		final RequirementDefinitions result = new RequirementDefinitions();
		for (final Map<String, Object> req : requirements) {
			final TRequirementDefinition rd = new TRequirementDefinition();
			if (req.size() == 2) {
				for (final String key : req.keySet()) {
					if (key.equals("relationship_type")) {
						rd.setRequirementType(new QName((String) req.get(key)));
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

	private PropertiesDefinition parsePropertiesDefinition(Map<String, PropertyDefinition> properties, String typename) {
		final PropertiesDefinition result = new PropertiesDefinition();
		// TODO: setType()?!
		result.setElement(new QName(TYPESNS, typename + "Properties", "types"));
		generateTypeXSD(properties, typename + "Properties");
		return result;
	}

	private void generateTypeXSD(Map<String, PropertyDefinition> properties, String name) {
		this.xsd.append("<xs:complexType name=\"" + name + "\">\n");
		this.xsd.append("<xs:sequence>\n");
		for (final Entry<String, PropertyDefinition> entry : properties.entrySet()) {
			this.xsd.append("<xs:element name=\"" + entry.getKey() + "\" type=\"xs:" + entry.getValue().getType() + "\" />\n");
		}
		this.xsd.append("</xs:sequence>\n");
		this.xsd.append("</xs:complexType>\n");
	}

	private Interfaces parseNodeTypeInterfaces(Map<String, Map<String, Map<String, String>>> interfaces) {
		final Interfaces result = new Interfaces();
		for (final Entry<String, Map<String, Map<String, String>>> entry : interfaces.entrySet()) {
			final TInterface inf = getInterfaceWithOperations(entry);
			result.getInterface().add(inf);
		}
		return result;
	}

	private TInterface getInterfaceWithOperations(Entry<String, Map<String, Map<String, String>>> entry) {
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

	private DerivedFrom parseDerivedFrom(String derived_from) {
		final DerivedFrom result = new DerivedFrom();
		result.setTypeRef(new QName(derived_from));
		return result;
	}

	private CapabilityDefinitions parseNodeTypeCapabilities(Map<String, Object> capabilities) {
		final CapabilityDefinitions result = new CapabilityDefinitions();
		for (final Entry<String, Object> capabilityEntry : capabilities.entrySet()) {
			final TCapabilityDefinition capabilityDefinition = new TCapabilityDefinition();
			capabilityDefinition.setName(capabilityEntry.getKey());
			if (capabilityEntry.getValue() instanceof HashMap) {
				final Map capability = (HashMap) capabilityEntry.getValue();
				String capabilityType = "CAPABILITY_TYPE";
				try {
					capabilityType = (String) capability.get("type");
				} catch (final Exception e) {
					System.out.println("No capability type defined or illegal value, using default.");
				}
				capabilityDefinition.setCapabilityType(new QName(capabilityType));
			}
			result.getCapabilityDefinition().add(capabilityDefinition);
		}
		return result;
	}

	private TDocumentation toDocumentation(String desc) {
		final TDocumentation docu = new TDocumentation();
		docu.getContent().add(desc);
		return docu;
	}

	private TNodeTemplate createNodeTemplate(NodeTemplate nodeTemplate, String nodename) {
		final TNodeTemplate result = new TNodeTemplate();
		// first set simple attributes like id, name, etc.
		result.setId(name2id(nodename));
		result.setName(nodename);
		result.getDocumentation().add(toDocumentation(nodeTemplate.getDescription()));
		result.setType(new QName(nodeTemplate.getType()));

		// then process more difficult things
		processCapabilitiesInNodeTemplate(nodeTemplate, result);
		processPropertiesInNodeTemplate(nodeTemplate, nodename, result);

		return result;
	}

	private void processCapabilitiesInNodeTemplate(NodeTemplate nodeTemplate, TNodeTemplate result) {
		final TNodeTemplate.Capabilities capabilities = new TNodeTemplate.Capabilities();

		for (final Entry<String, Object> nodeTemplateCapability : nodeTemplate.getCapabilities().entrySet()) {
			if (nodeTemplateCapability.getValue() instanceof HashMap) {
				final Map capabilityDefinition = (HashMap) nodeTemplateCapability.getValue();
				final TCapability tCapability = new TCapability();
				tCapability.setName(nodeTemplateCapability.getKey());
				String capabilityType = "CAPABILITY_TYPE";
				try {
					capabilityType = (String) capabilityDefinition.get("type");
				} catch (final Exception e) {
					System.out.println("No capability type defined or illegal value, using default.");
				}
				tCapability.setType(new QName(capabilityType));
				tCapability.setId(result.getId() + "_" + nodeTemplateCapability.getKey());
				// TODO: set properties if any available
				capabilities.getCapability().add(tCapability);
			}
		}

		result.setCapabilities(capabilities);
	}

	private void processPropertiesInNodeTemplate(NodeTemplate nodeTemplate, String nodename, TNodeTemplate result) {
		final TEntityTemplate.Properties prop = new TEntityTemplate.Properties();
		final AnyMap properties = new AnyMap(parseProperties(nodeTemplate.getProperties(), nodename));
		prop.setAny(properties);
		result.setProperties(prop);
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> parseProperties(Map<String, Object> properties, String nodename) {
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

	private String parseGetter(Map<String, Object> getterMap) {
		for (final Entry<String, Object> getter : getterMap.entrySet()) {
			switch (getter.getKey()) {
			case "get_input":
				final String inputvar = (String) getter.getValue();
				if (getInputs().containsKey(inputvar)) {
					return getInputs().get(inputvar);
				}
				if (this.st.getInputs().containsKey(inputvar)) {
					if (this.st.getInputs().get(inputvar).getDefault() != null && !this.st.getInputs().get(inputvar).getDefault().isEmpty()) {
						return this.st.getInputs().get(inputvar).getDefault();
					}
				}
				// TODO: *Type-defaults
				return DEFAULT_USER_INPUT;
			case "get_property":
				@SuppressWarnings("unchecked")
				final List<String> list = (List<String>) getter.getValue();
				final String template = list.get(0);
				final String property = list.get(1);
				if (this.st.getNode_templates().containsKey(template)) {
					if (this.st.getNode_templates().get(template).getProperties().containsKey(property)) {
						return (String) this.st.getNode_templates().get(template).getProperties().get(property);
					}
				}
			case "get_ref_property":
				return DEFAULT_USER_INPUT;
			default:
				final String result = serializeYAML(getterMap);
				if (result != null) {
					return result;
				} else {
					return DEFAULT_USER_INPUT;
				}
			}
		}
		return "";
	}

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
	 * Adds a unique number to the prefix.
	 *
	 * @param prefix The prefix
	 * @return A unique String.
	 */
	private String unique(String prefix) {
		long id = this.uniqueID++;
		if (id < 0) {
			// Negative IDs are not pretty.
			this.uniqueID = 0;
			id = this.uniqueID;
		}
		return prefix + id;
	}

	/**
	 * If name contains multiple aliases the result is the first.
	 *
	 * @param name The name field of an XML element.
	 * @return A valid ID.
	 */
	private String name2id(String name) {
		return name.split(",")[0];
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
}
