package org.opentosca.yamlconverter.main;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.opentosca.yamlconverter.main.interfaces.IToscaYaml2YamlBeanConverter;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.Input;
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
		Assert.assertEquals("tosca_simple_yaml_1_0", element.getTosca_definitions_version());
		Assert.assertEquals("tosca.nodes.Compute", element.getNode_templates().get("my_server").getType());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testYaml2YamlBean_Null() throws Exception {
		this.converter.yaml2yamlbean(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testYaml2YamlBean_EmptyString() throws Exception {
		this.converter.yaml2yamlbean("");
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

	@Test(expected = IllegalArgumentException.class)
	public void testYamlBean2Yaml_Null() throws Exception {
		this.converter.yamlbean2yaml(null);
	}

	@Test
	public void testReadYamlInputs() throws Exception {
		final ServiceTemplate templ = this.converter.yaml2yamlbean(this.testUtils.readYamlTestResource("/yaml/inputs.yaml"));
		Assert.assertNotNull(templ);
		final Map<String, Input> inputs = templ.getInputs();
		Assert.assertNotNull(inputs);
		Assert.assertEquals("template must have 1 input", 1, inputs.size());
		final Input fooInput = inputs.get("foo");
		Assert.assertEquals("fooInput must have the type 'string'", "string", fooInput.getType());
		Assert.assertEquals("fooInput must have 1 constraint", 2, fooInput.getConstraints().size());
		Assert.assertEquals("constraint min_length must equal 2", "2", fooInput.getConstraints().get(0).get("min_length"));
	}

	@Test
	public void testReadYamlInputs_useInputs() throws Exception {
		final ServiceTemplate templ = this.converter.yaml2yamlbean(this.testUtils.readYamlTestResource("/yaml/useInputs.yaml"));
		Assert.assertNotNull(templ);
		final Object memSize = templ.getNode_templates().get("my_server").getProperties().get("mem_size");
		Assert.assertNotNull(memSize);
		Assert.assertTrue(((Map<String, Object>) memSize).containsKey("get_input"));
	}

	@Test
	public void testReadYamlInputs_imports() throws Exception {
		final ServiceTemplate templ = this.converter.yaml2yamlbean(this.testUtils.readYamlTestResource("/yaml/imports.yaml"));
		Assert.assertNotNull(templ);
		Assert.assertNotNull(templ.getImports());
		Assert.assertEquals(2, templ.getImports().size());
		Assert.assertEquals("inputs.yaml", templ.getImports().get(0));
	}
}
