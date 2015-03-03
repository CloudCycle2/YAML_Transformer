package org.opentosca.yamlconverter.switchmapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.opentosca.model.tosca.TCapability;
import org.opentosca.model.tosca.TEntityTemplate;
import org.opentosca.model.tosca.TNodeTemplate;
import org.opentosca.yamlconverter.main.utils.AnyMap;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.NodeTemplate;

public class NodeTemplatesSubSwitch extends AbstractSubSwitch {

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
		result.setType(new QName(nodeTemplate.getType()));

		// then process more difficult things
		processCapabilitiesInNodeTemplate(nodeTemplate, result);
		processPropertiesInNodeTemplate(nodeTemplate, nodename, result);

		return result;
	}

	private void processPropertiesInNodeTemplate(NodeTemplate nodeTemplate, String nodename, TNodeTemplate result) {
		final TEntityTemplate.Properties prop = new TEntityTemplate.Properties();
		final JAXBElement<AnyMap> jaxbprop = getAnyMapForProperties(nodeTemplate.getProperties(), nodename);
		prop.setAny(jaxbprop);
		result.setProperties(prop);
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
				tCapability.setType(new QName(capabilityType));
				tCapability.setId(result.getId() + "_" + nodeTemplateCapability.getKey());
				// TODO: set properties if any available
				capabilities.getCapability().add(tCapability);
			}
		}

		result.setCapabilities(capabilities);
	}

}
