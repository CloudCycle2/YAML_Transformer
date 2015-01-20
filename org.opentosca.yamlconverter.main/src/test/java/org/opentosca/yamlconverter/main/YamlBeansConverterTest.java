package org.opentosca.yamlconverter.main;

import org.junit.Assert;
import org.junit.Test;
import org.opentosca.yamlconverter.main.interfaces.IToscaYaml2YamlBeanConverter;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.ServiceTemplate;

/**
 * @author Sebi
 */
public class YamlBeansConverterTest extends BaseTest {

	private final IToscaYaml2YamlBeanConverter converter = new YamlBeansConverter();

	@Test
	public void testYaml2YamlBean() throws Exception {
		final ServiceTemplate element = this.converter.yaml2yamlbean(this.testUtils.readYamlTestResource("/yaml/helloworld.yaml"));
		Assert.assertNotNull(element);
		Assert.assertEquals(element.getTosca_definitions_version(), "tosca_simple_yaml_1_0");
		Assert.assertEquals("tosca.nodes.Compute", element.getNode_templates().get("my_server").getType());
	}

	@Test
	public void testYamlBean2Yaml() throws Exception {
		// construct element
		final ServiceTemplate element = new ServiceTemplate();
		final String yamlSpec = "yaml_spec_123";
		element.setTosca_definitions_version(yamlSpec);
		final String output = this.converter.yamlbean2yaml(element);
		Assert.assertNotNull(output);
		Assert.assertTrue(output.contains(yamlSpec));
	}
}
