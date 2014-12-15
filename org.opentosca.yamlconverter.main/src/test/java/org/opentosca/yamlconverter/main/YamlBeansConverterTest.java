package org.opentosca.yamlconverter.main;

import org.junit.Assert;
import org.junit.Test;
import org.opentosca.yamlconverter.main.interfaces.IToscaYaml2YamlBeanConverter;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.YAMLFileRoot;

/**
 * @author Sebi
 */
public class YamlBeansConverterTest extends BaseTest {

    private IToscaYaml2YamlBeanConverter converter = new YamlBeansConverter();

    @Test
    public void testYaml2YamlBean() throws Exception {
        YAMLFileRoot element = (YAMLFileRoot)
                converter.yaml2yamlbean(testUtils.readYamlTestResource("/yaml/helloworld.yaml"));
        Assert.assertNotNull(element);
        Assert.assertEquals(element.getTosca_definitions_version(), "tosca_simple_yaml_1_0");
    }

    @Test
    public void testYamlBean2Yaml() throws Exception {
        // construct element
        YAMLFileRoot element = new YAMLFileRoot();
        final String yamlSpec = "yaml_spec_123";
        element.setTosca_definitions_version(yamlSpec);
        String output = converter.yamlbean2yaml(element);
        Assert.assertNotNull(output);
        Assert.assertTrue(output.contains(yamlSpec));
    }
}
