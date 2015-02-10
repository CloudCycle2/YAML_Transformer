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

	/**
	 * Parses {@link ServiceTemplate} to {@link Definitions}.
	 *
	 * @param st the {@link ServiceTemplate} to parse
	 * @return the parsed {@link Definitions}
	 */
	public Definitions parse(ServiceTemplate st) {
		this.xsd = new StringBuilder(); // reset
		this.st = st;
		return case_ServiceTemplate(st);
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

	private Definitions case_ServiceTemplate(ServiceTemplate elem) {
		final Definitions result = new Definitions();
		final TServiceTemplate serviceTemplate = new TServiceTemplate();
		final TTopologyTemplate topologyTemplate = new TTopologyTemplate();
		result.setId(unique("root"));
		result.setName(unique("Root"));
		if (elem.getTosca_default_namespace() != null && !elem.getTosca_default_namespace().isEmpty()) {
			result.setTargetNamespace(elem.getTosca_default_namespace());
		} else {
			result.setTargetNamespace(NS);
		}
		result.getServiceTemplateOrNodeTypeOrNodeTypeImplementation().add(serviceTemplate);
		// result.setExtensions();
		// result.setTypes();
		result.getDocumentation().add(toDocumentation(elem.getDescription()));
		result.getOtherAttributes().put(new QName("xmlns:types"), TYPESNS);
		if (elem.getCapability_types() != null) {
			for (final Entry<String, CapabilityType> capType : elem.getCapability_types().entrySet()) {
				final TCapabilityType ct = case_CapabilityType(capType);
				ct.setName(capType.getKey());
				result.getServiceTemplateOrNodeTypeOrNodeTypeImplementation().add(ct);
			}
		}
		if (elem.getNode_types() != null) {
			for (final Entry<String, NodeType> nt : elem.getNode_types().entrySet()) {
				final TNodeType xnode = case_NodeType(nt.getValue(), nt.getKey());
				result.getServiceTemplateOrNodeTypeOrNodeTypeImplementation().add(xnode);
			}
		}
		if (elem.getRelationship_types() != null) {
			for (final Entry<String, RelationshipType> relType : elem.getRelationship_types().entrySet()) {
				final TRelationshipType rt = case_RelationshipType(relType);
				rt.setName(relType.getKey());
				result.getServiceTemplateOrNodeTypeOrNodeTypeImplementation().add(rt);
			}
		}
		// for (final ArtifactType artType : elem.getArtifactType()) {
		// result.getServiceTemplateOrNodeTypeOrNodeTypeImplementation().add(case_ArtifactType(artType));
		// }
		if (elem.getImports() != null) {
			// for (final Entry<String, Import> importelem : elem.getImports().entrySet()) {
			// TODO: How do we handle imports?
			// result.getImport().add(case_Import(importelem));
			// }
		}
		result.getImport().add(createTypeImport());
		// serviceTemplate.setBoundaryDefinitions(value);
		serviceTemplate.setId(unique("serviceTemplate"));
		serviceTemplate.setName("ServiceTemplate");
		// serviceTemplate.setPlans(value);
		// serviceTemplate.setSubstitutableNodeType(value);
		// serviceTemplate.setTags(value);
		// serviceTemplate.setTargetNamespace(value);
		serviceTemplate.setTopologyTemplate(topologyTemplate);
		// serviceTemplate.getDocumentation().add(docu);
		// serviceTemplate.getOtherAttributes().put(key, value);
		// topologyTemplate.getAny().add(o);
		// topologyTemplate.getDocumentation().add(docu);
		if (elem.getNode_templates() != null) {
			for (final Map.Entry<String, NodeTemplate> nt : elem.getNode_templates().entrySet()) {
				final TNodeTemplate xnode = case_NodeTemplate(nt.getValue(), nt.getKey());
				topologyTemplate.getNodeTemplateOrRelationshipTemplate().add(xnode);
			}
		}
		// topologyTemplate.getOtherAttributes().put(key, value);
		return result;
	}

	private TImport createTypeImport() {
		final TImport result = new TImport();
		result.setImportType(XMLSCHEMA_NS);
		result.setLocation("types.xsd");
		result.setNamespace(TYPESNS);
		return result;
	}

	private TRelationshipType case_RelationshipType(Entry<String, RelationshipType> relType) {
		final TRelationshipType result = new TRelationshipType();
		for (final Entry<String, Map<String, Map<String, OperationDefinition>>> iface : relType.getValue().getInterfaces().entrySet()) {
			// TODO
		}
		for (final Entry<String, PropertyDefinition> prop : relType.getValue().getProperties().entrySet()) {
			// TODO
		}
		for (final String target : relType.getValue().getValid_targets()) {
			// TODO
		}
		// result.setAbstract(value);
		// result.setDerivedFrom(value);
		// result.setFinal(value);
		// result.setInstanceStates(value);
		result.setName(relType.getKey());
		// result.setPropertiesDefinition(value);
		// result.setSourceInterfaces(value);
		// result.setTags(value);
		// result.setTargetInterfaces(value);
		// result.setTargetNamespace(value);
		// result.setValidSource(value);
		// result.setValidTarget(value);
		// result.getAny().add(e);
		result.getDocumentation().add(toDocumentation(relType.getValue().getDescription()));
		// result.getOtherAttributes().put(key, value);
		return result;
	}

	private TCapabilityType case_CapabilityType(Entry<String, CapabilityType> capType) {
		final TCapabilityType result = new TCapabilityType();
		for (final Entry<String, PropertyDefinition> prop : capType.getValue().getProperties().entrySet()) {
			// TODO
		}
		// PropertiesDefinition propDef = new PropertiesDefinition();
		// propDef.setElement(value);
		// propDef.setType(value);
		// result.setPropertiesDefinition(propDef );
		// TODO: YAMLmodel CapabilityType
		// result.setAbstract(value);
		// result.setDerivedFrom(value);
		// result.setFinal(value);
		result.setName(capType.getKey());
		// result.setTags(value);
		// result.setTargetNamespace(value);
		// result.getAny().add();
		result.getDocumentation().add(toDocumentation(capType.getValue().getDescription()));
		// result.getOtherAttributes().put(key, value);
		return result;
	}

	private TNodeType case_NodeType(NodeType value, String name) {
		final TNodeType result = new TNodeType();
		// TODO: value.getArtifacts() ?
		// result.setAbstract(value);
		if (value.getCapabilities() != null) {
			result.setCapabilityDefinitions(parseNodeTypeCapabilities(value.getCapabilities()));
		}
		if (value.getDerived_from() != null) {
			result.setDerivedFrom(parseNodeTypeDerivedFrom(value.getDerived_from()));
		}
		// result.setFinal(value);
		// result.setInstanceStates(value);
		if (value.getInterfaces() != null) {
			result.setInterfaces(parseNodeTypeInterfaces(value.getInterfaces()));
		}
		result.setName(name);
		if (value.getProperties() != null) {
			result.setPropertiesDefinition(parseNodeTypePropertiesDefinition(value.getProperties(), name));
		}
		if (value.getRequirements() != null) {
			result.setRequirementDefinitions(parseNodeTypeRequirementDefinitions(value.getRequirements()));
		}
		// result.setTags(value);
		// result.setTargetNamespace(value);
		if (value.getDescription() != null) {
			result.getDocumentation().add(toDocumentation(value.getDescription()));
		}
		// result.getAny().add(e);
		// result.getOtherAttributes().put(key, value);
		return result;
	}

	private RequirementDefinitions parseNodeTypeRequirementDefinitions(Map<String, Object> requirements) {
		final RequirementDefinitions result = new RequirementDefinitions();
		for (final Entry<String, Object> req : requirements.entrySet()) {
			final TRequirementDefinition rd = new TRequirementDefinition();
			// rd.setConstraints(value);
			// rd.setLowerBound(value);
			rd.setName(req.getKey());
			// TODO: we must check here if value is only a string or a map of values!
			rd.setRequirementType(new QName(req.getValue().toString()));
			// rd.setUpperBound(value);
			result.getRequirementDefinition().add(rd);
		}
		return result;
	}

	private PropertiesDefinition parseNodeTypePropertiesDefinition(Map<String, PropertyDefinition> properties, String typename) {
		final PropertiesDefinition result = new PropertiesDefinition();
		// TODO: setElement()?!
		// result.setElement(value);
		result.setType(new QName(TYPESNS, typename + "Properties", "types"));
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

	private Interfaces parseNodeTypeInterfaces(Map<String, Map<String, Map<String, OperationDefinition>>> interfaces) {
		final Interfaces result = new Interfaces();
		for (final Entry<String, Map<String, Map<String, OperationDefinition>>> entry : interfaces.entrySet()) {
			final TInterface inf = new TInterface();
			inf.setName(entry.getKey());
			// TODO: YAMLmodel Interface Operations
			// inf.getOperation().add(e)
			result.getInterface().add(inf);
		}
		return result;
	}

	private DerivedFrom parseNodeTypeDerivedFrom(String derived_from) {
		final DerivedFrom result = new DerivedFrom();
		result.setTypeRef(new QName(derived_from));
		return result;
	}

	private CapabilityDefinitions parseNodeTypeCapabilities(Map<String, Object> capabilities) {
		final CapabilityDefinitions result = new CapabilityDefinitions();
		for (final Entry<String, Object> entr : capabilities.entrySet()) {
			final TCapabilityDefinition capdef = new TCapabilityDefinition();
			// TODO YAMLmodel NodeType Capabilities
			// capdef.setCapabilityType(value);
			// capdef.setConstraints(value);
			// capdef.setLowerBound(value);
			capdef.setName(entr.getKey());
			// capdef.setUpperBound(value);
			// capdef.getAny().add(e);
			// capdef.getDocumentation().add(e);
			// capdef.getOtherAttributes().put(key, value);
			result.getCapabilityDefinition().add(capdef);
		}
		return result;
	}

	private TDocumentation toDocumentation(String desc) {
		final TDocumentation docu = new TDocumentation();
		docu.getContent().add(desc);
		return docu;
	}

	private TNodeTemplate case_NodeTemplate(NodeTemplate elem, String nodename) {
		final TNodeTemplate result = new TNodeTemplate();
		// result.setCapabilities(cap);
		// result.setDeploymentArtifacts(depa);
		result.setId(name2id(nodename));
		// result.setMaxInstances(maxinst);
		// result.setMinInstances(mininst);
		result.setName(nodename);
		// result.setPolicies(poli);
		final TEntityTemplate.Properties prop = new TEntityTemplate.Properties();
		final QName type = new QName(elem.getType());
		final AnyMap properties = new AnyMap(parseProperties(elem.getProperties(), nodename));
		prop.setAny(properties);
		result.setProperties(prop);
		// result.setPropertyConstraints(propconstr);
		// result.setRequirements(req);
		result.setType(type);
		// result.getAny().add(any);
		result.getDocumentation().add(toDocumentation(elem.getDescription()));
		// result.getOtherAttributes().put(name, attr)
		return result;
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
	 * Checks wether the Object is a Getter or just a normal property.
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
