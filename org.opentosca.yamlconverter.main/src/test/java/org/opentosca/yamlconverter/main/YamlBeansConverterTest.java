package org.opentosca.yamlconverter.main;

import org.junit.Assert;
import org.junit.Test;
import org.opentosca.yamlconverter.main.interfaces.IToscaYaml2YamlBeanConverter;
import org.opentosca.yamlconverter.main.util.BaseTest;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.ServiceTemplate;

/**
 * @author Sebi
 */
public class YamlBeansConverterTest extends BaseTest {

	private final IToscaYaml2YamlBeanConverter converter = new YamlBeansConverter();

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

}
