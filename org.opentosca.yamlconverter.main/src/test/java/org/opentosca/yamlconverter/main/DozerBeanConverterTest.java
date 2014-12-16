package org.opentosca.yamlconverter.main;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.opentosca.model.tosca.TDefinitions;
import org.opentosca.yamlconverter.main.interfaces.IToscaBean2BeanConverter;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.NodeTemplate;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.YAMLFileRoot;

/**
 * @author Sebi
 */
public class DozerBeanConverterTest extends BaseTest {

	private final IToscaBean2BeanConverter converter = new DozerBeanConverter();

	@Test
	@Ignore
	public void testYamlBean2XmlBean() throws Exception {
		final YAMLFileRoot yamlRoot = new YAMLFileRoot();
		yamlRoot.setTosca_definitions_version("tosca_123");

		final Map<String, NodeTemplate> nodeTemplateMap = new HashMap<>();
		final NodeTemplate nodeTemplate = new NodeTemplate();
		final String nodeTemplateType = "tosca.nodes.Compute";
		nodeTemplate.setType(nodeTemplateType);
		nodeTemplateMap.put("OpenStack", nodeTemplate);

		yamlRoot.setNode_templates(nodeTemplateMap);

		final TDefinitions result = this.converter.yamlb2xmlb(yamlRoot);
		Assert.assertNotNull(result);
	}

}