package org.opentosca.yamlconverter.switchmapper;

import java.util.Map;

import javax.xml.namespace.QName;

import org.opentosca.model.tosca.Definitions;
import org.opentosca.model.tosca.TNodeTemplate;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.NodeTemplate;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.PropertyType;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.YAMLFileRoot;

public class Yaml2XmlSwitch {
	private long uniqueID = 0;

	public Object doswitch(Object elem) {
		if (elem instanceof YAMLFileRoot) {
			return case_YAMLFileRoot((YAMLFileRoot) elem);
		}
		if (elem instanceof NodeTemplate) {
			return case_NodeTemplate((NodeTemplate) elem);
		}
		throw new UnsupportedOperationException("Object not yet supported");
	}

	private Definitions case_YAMLFileRoot(YAMLFileRoot elem) {
		final Definitions result = new Definitions();
		result.setId(unique("root"));
		result.setName(unique("Root"));
		// result.setTargetNamespace();
		// result.setExtensions();
		// result.setTypes();
		// result.getDocumentation().add(docu);
		// result.getOtherAttributes().put(name, attribute);
		for (final Map.Entry<String, NodeTemplate> nt : elem.getNode_templates().entrySet()) {
			final TNodeTemplate xnode = case_NodeTemplate(nt.getValue());
			// override name and id of the nodetemplate
			xnode.setName(nt.getKey());
			xnode.setId(name2id(nt.getKey()));
			result.getServiceTemplateOrNodeTypeOrNodeTypeImplementation().add(xnode);
		}
		// The import is the last one, because we may need to add some resulting from other fields.
		// result.getImport().add(import);
		return result;
	}

	@SuppressWarnings("unused")
	private Object case_Propertytype(PropertyType elem) {
		return null;
	}

	private TNodeTemplate case_NodeTemplate(NodeTemplate elem) {
		// TODO: elem.getProperties()
		final TNodeTemplate result = new TNodeTemplate();
		// result.setCapabilities(cap);
		// result.setDeploymentArtifacts(depa);
		result.setId(unique("nodetemplate"));
		// result.setMaxInstances(maxinst);
		// result.setMinInstances(mininst);
		result.setName(unique("Nodetemplate"));
		// result.setPolicies(poli);
		// result.setProperties(prop);
		// result.setPropertyConstraints(propconstr);
		// result.setRequirements(req);
		result.setType(new QName(elem.getType()));
		// result.getAny().add(any);
		// result.getDocumentation().add(docu);
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
