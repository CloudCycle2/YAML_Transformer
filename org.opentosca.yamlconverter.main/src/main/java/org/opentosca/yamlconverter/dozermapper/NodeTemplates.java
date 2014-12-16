package org.opentosca.yamlconverter.dozermapper;


import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.QName;

import org.dozer.DozerConverter;
import org.opentosca.model.tosca.TEntityTemplate;
import org.opentosca.model.tosca.TEntityTemplate.Properties;
import org.opentosca.model.tosca.TNodeTemplate;
import org.opentosca.model.tosca.TServiceTemplate;
import org.opentosca.model.tosca.TTopologyTemplate;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.NodeTemplate;

public class NodeTemplates extends DozerConverter<Map<String, NodeTemplate>, TServiceTemplate> {

	public NodeTemplates(Class<Map<String, NodeTemplate>> prototypeA, Class<TServiceTemplate> prototypeB) {
		super(prototypeA, prototypeB);
	}

	@Override
	public Map<String, NodeTemplate> convertFrom(TServiceTemplate arg0, Map<String, NodeTemplate> arg1) {
		throw new UnsupportedOperationException("Not yet implemented bidirectional!");
	}

	@Override
	public TServiceTemplate convertTo(Map<String, NodeTemplate> yamlmap, TServiceTemplate arg1) {
		final TServiceTemplate serviceTemp = new TServiceTemplate();
		final TTopologyTemplate topoTemp = new TTopologyTemplate();
		final List<TEntityTemplate> nodelist = topoTemp.getNodeTemplateOrRelationshipTemplate();
		for (final Map.Entry<String, NodeTemplate> entry : yamlmap.entrySet()) {
			nodelist.add(toXMLNodeTempl(entry));
		}
		serviceTemp.setTopologyTemplate(topoTemp);
		return serviceTemp;
	}

	private TNodeTemplate toXMLNodeTempl(Entry<String, NodeTemplate> entry) {
		final TNodeTemplate xNodeTemp = new TNodeTemplate();
		final Properties prop = new Properties();
		prop.setAny(entry.getValue().getProperties());
		xNodeTemp.setProperties(prop);
		xNodeTemp.setName(entry.getKey());
		xNodeTemp.setType(new QName(entry.getValue().getType()));
		return xNodeTemp;
	}

}
