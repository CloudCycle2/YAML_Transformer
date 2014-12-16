package org.opentosca.yamlconverter.main;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.opentosca.yamlconverter.main.interfaces.IToscaYaml2YamlBeanConverter;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.NodeTemplate;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.YAMLFileRoot;

/**
 * @author Sebi
 */
public class YamlHelloWorldParseTest extends BaseTest {

	private final IToscaYaml2YamlBeanConverter converter = new YamlBeansConverter();

	@Test
	public void testYaml2YamlBean() throws Exception {
		final YAMLFileRoot root = (YAMLFileRoot) this.converter.yaml2yamlbean(this.testUtils.readYamlTestResource("/yaml/helloworld.yaml"));
		Assert.assertNotNull(root);
		final YAMLFileRoot rootExpected = getHelloWorldRoot();
		Assert.assertEquals(rootExpected, root);
	}

	@Test
	public void testYamlBean2Yaml() throws Exception {
		final IToscaYaml2YamlBeanConverter converter = new YamlBeansConverter();
		final String yaml = converter.yamlbean2yaml(getHelloWorldRoot());
		this.testUtils.yamlFilesEqual(yaml, "/yaml/helloworld.yaml");
	}

	private YAMLFileRoot getHelloWorldRoot() {
		final YAMLFileRoot root = new YAMLFileRoot();
		root.setTosca_definitions_version("tosca_simple_yaml_1_0");

		final Map<String, NodeTemplate> node_templates = new HashMap<String, NodeTemplate>();

		final NodeTemplate temp1 = new NodeTemplate();
		temp1.setType("tosca.nodes.Compute");
		temp1.getProperties().put("disk_size", 10);
		temp1.getProperties().put("num_cpus", 2);
		temp1.getProperties().put("mem_size", 4);
		temp1.getProperties().put("os_arch", "x86_64");
		temp1.getProperties().put("os_type", "linux");
		temp1.getProperties().put("os_distribution", "rhel");
		temp1.getProperties().put("os_version", "6.5");
		node_templates.put("my_server", temp1);

		final NodeTemplate temp2 = new NodeTemplate();
		temp2.setType("tosca.nodes.Compute");
		temp2.getProperties().put("disk_size", 11);
		temp2.getProperties().put("num_cpus", 1);
		temp2.getProperties().put("mem_size", 1);
		temp2.getProperties().put("os_arch", "x86_32");
		temp2.getProperties().put("os_type", "windows");
		temp2.getProperties().put("os_distribution", "rheli");
		temp2.getProperties().put("os_version", "7.0");
		node_templates.put("your_server", temp2);

		root.setNode_templates(node_templates);
		return root;
	}
}
