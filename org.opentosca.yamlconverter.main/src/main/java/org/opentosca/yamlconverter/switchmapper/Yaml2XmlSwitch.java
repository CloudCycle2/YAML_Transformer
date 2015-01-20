package org.opentosca.yamlconverter.switchmapper;

import javax.xml.namespace.QName;

import org.opentosca.model.tosca.Definitions;
import org.opentosca.model.tosca.TDocumentation;
import org.opentosca.model.tosca.TEntityTemplate;
import org.opentosca.model.tosca.TNodeTemplate;
import org.opentosca.model.tosca.TServiceTemplate;
import org.opentosca.model.tosca.TTopologyTemplate;
import org.opentosca.yamlconverter.main.AnyMap;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.NodeTemplate;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.PropertyType;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.ServiceTemplate;

public class Yaml2XmlSwitch {
	private static final String TOSCABASETYPES_NS = null;
	private long uniqueID = 0;

	public Object doswitch(Object elem) {
		if (elem instanceof ServiceTemplate) {
			return case_ServiceTemplate((ServiceTemplate) elem);
		}
		if (elem instanceof NodeTemplate) {
			return case_NodeTemplate((NodeTemplate) elem);
		}
		throw new UnsupportedOperationException("Object not yet supported");
	}

	private Definitions case_ServiceTemplate(ServiceTemplate elem) {
		final Definitions result = new Definitions();
		final TServiceTemplate serviceTemplate = new TServiceTemplate();
		final TTopologyTemplate topologyTemplate = new TTopologyTemplate();
		result.setId(unique("root"));
		result.setName(unique("Root"));
		// result.setTargetNamespace();
		// result.setExtensions();
		// result.setTypes();
		result.getDocumentation().add(toDocumentation(elem.getDescription()));
		// result.getOtherAttributes().put(name, attribute);
		/*
		 * for(CapabilityType capType : elem.getCapabilityTypes()){
		 * result.getServiceTemplateOrNodeTypeOrNodeTypeImplementation().add(case_CapabilityType(capType)); }
		 */
		/*
		 * for(NodeType nodeType : elem.getNodeTypes()){
		 * result.getServiceTemplateOrNodeTypeOrNodeTypeImplementation().add(case_NodeType(nodeType)); }
		 */
		/*
		 * for(RelationshipType relType : elem.getRelationshipTypes()){
		 * result.getServiceTemplateOrNodeTypeOrNodeTypeImplementation().add(case_RelationshipType(relType)); }
		 */
		/*
		 * for(ArtifactType artType : elem.getArtifactType()){
		 * result.getServiceTemplateOrNodeTypeOrNodeTypeImplementation().add(case_ArtifactType(artType)); }
		 */
		/*
		 * for (final Import importelem : elem.getImports()) { result.getImport().add(case_Import(importelem)); }
		 */
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
		for (final NodeTemplate nt : elem.getNodeTemplate()) {
			final TNodeTemplate xnode = case_NodeTemplate(nt);
			// TODO: is the name now parsed directly to the nodetemplate?
			// override name and id of the nodetemplate
			// xnode.setName(nt.getKey());
			// xnode.setId(name2id(nt.getKey()));
			topologyTemplate.getNodeTemplateOrRelationshipTemplate().add(xnode);
		}
		// topologyTemplate.getOtherAttributes().put(key, value);
		return result;
	}

	private TDocumentation toDocumentation(String desc) {
		final TDocumentation docu = new TDocumentation();
		docu.getContent().add(desc);
		// TODO set lang and source
		return docu;
	}

	@SuppressWarnings("unused")
	private Object case_Propertytype(PropertyType elem) {
		// TODO
		return null;
	}

	private TNodeTemplate case_NodeTemplate(NodeTemplate elem) {
		final TNodeTemplate result = new TNodeTemplate();
		// result.setCapabilities(cap);
		// result.setDeploymentArtifacts(depa);
		result.setId(unique("nodetemplate"));
		// result.setMaxInstances(maxinst);
		// result.setMinInstances(mininst);
		result.setName(unique("Nodetemplate"));
		// result.setPolicies(poli);
		final TEntityTemplate.Properties prop = new TEntityTemplate.Properties();
		final QName type = new QName(elem.getType());
		// final Object properties = parseProperties(elem.getProperties(), type);
		final AnyMap properties = new AnyMap(elem.getProperties());
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
}
