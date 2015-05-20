package org.opentosca.yamlconverter.main;

import java.util.Map;

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
		final ServiceTemplate element = this.converter.convertToYamlBean(this.testUtils.readYamlTestResource("/yaml/helloworld.yml"));
		Assert.assertNotNull(element);
		Assert.assertEquals("tosca_simple_yaml_1_0", element.getTosca_definitions_version());
		Assert.assertEquals("tosca.nodes.Compute", element.getNode_templates().get("my_server").getType());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testYaml2YamlBean_Null() throws Exception {
		this.converter.convertToYamlBean(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testYaml2YamlBean_EmptyString() throws Exception {
		this.converter.convertToYamlBean("");
	}

	@Test
	public void testYamlBean2Yaml() throws Exception {
		// construct element
		final ServiceTemplate element = new ServiceTemplate();
		final String yamlSpec = "yaml_spec_123";
		element.setTosca_definitions_version(yamlSpec);
		final String output = this.converter.convertToYaml(element);
		Assert.assertNotNull(output);
		Assert.assertTrue(output.contains(yamlSpec));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testYamlBean2Yaml_Null() throws Exception {
		this.converter.convertToYaml(null);
	}

	@Test
	public void testReadYamlInputs_useInputs() throws Exception {
		final ServiceTemplate templ = this.converter.convertToYamlBean(this.testUtils.readYamlTestResource("/yaml/useInputs.yml"));
		Assert.assertNotNull(templ);
		final Object memSize = templ.getNode_templates().get("my_server").getProperties().get("mem_size");
		Assert.assertNotNull(memSize);
		Assert.assertTrue(((Map<String, Object>) memSize).containsKey("get_input"));
	}

	@Test
	public void testReadYamlInputs_imports() throws Exception {
		final ServiceTemplate templ = this.converter.convertToYamlBean(this.testUtils.readYamlTestResource("/yaml/imports.yml"));
		Assert.assertNotNull(templ);
		Assert.assertNotNull(templ.getImports());
		Assert.assertEquals(2, templ.getImports().size());
		Assert.assertEquals("inputs.yml", templ.getImports().get(0));
	}
}
