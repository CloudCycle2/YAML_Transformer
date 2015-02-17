package org.opentosca.yamlconverter.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.opentosca.yamlconverter.main.interfaces.IToscaYaml2YamlBeanConverter;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.Input;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.PropertyType;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.ServiceTemplate;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.constraints.ConstraintClause;

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
		Assert.assertEquals("template must have 2 inputs", 2, inputs.size());
		final Input fooInput = inputs.get("foo");
		Assert.assertEquals("fooInput must have the type 'string'", "string", fooInput.getType());
		Assert.assertEquals("fooInput must have 3 constraints", 3, fooInput.getConstraints().size());
		Assert.assertEquals("constraint min_length must equal 2", "2", fooInput.getConstraints().get(0).get("min_length"));

		// constraints validity checking

		List<ConstraintClause<Object>> constraints = toConstraints(fooInput.getConstraints(), fooInput.getType());

		final List<ConstraintTest> constraintTests = new ArrayList<ConstraintTest>();
		constraintTests.add(new ConstraintTest("'a' must not be valid because min_length is 2", "a", false));
		constraintTests.add(new ConstraintTest("'ab' must be valid because min_length is 2", "ab", true));
		constraintTests.add(new ConstraintTest("'bb' must not be valid because it does not match pattern", "bb", false));
		constraintTests.add(new ConstraintTest("'abbbb' must be valid", "abbbb", true));
		constraintTests.add(new ConstraintTest("'aaabaa' must not be valid because max_length is 5", "aaaaaa", false));

		for (final ConstraintTest test : constraintTests) {
			boolean valid = true;
			for (final ConstraintClause<Object> constraint : constraints) {
				if (!constraint.isValid(test.value)) {
					valid = false;
				}
			}
			Assert.assertTrue(test.message, valid == test.valid);
		}

		constraintTests.clear();
		constraintTests.add(new ConstraintTest("4 must not be valid because it is not greater or equal 5", 4, true));
		constraintTests.add(new ConstraintTest("5 must be valid", 5, true));
		constraintTests.add(new ConstraintTest("10 must be valid", 10, true));
		constraintTests.add(new ConstraintTest("15 must be valid", 15, true));
		constraintTests.add(new ConstraintTest("16 must not be valid because it is not in the valid values list", 16, false));
		constraintTests.add(new ConstraintTest("20 must be valid", 20, true));
		constraintTests.add(new ConstraintTest("25 must not be valid", 25, false));

		final Input barInput = inputs.get("bar");
		constraints = toConstraints(barInput.getConstraints(), barInput.getType());

		for (final ConstraintTest test : constraintTests) {
			boolean valid = true;
			for (final ConstraintClause<Object> constraint : constraints) {
				if (!constraint.isValid(test.value)) {
					valid = false;
				}
			}
			Assert.assertTrue(test.message, valid == test.valid);
		}
	}

	private List<ConstraintClause<Object>> toConstraints(List<Map<String, Object>> constraintsListMap, String dataType) {
		final List<ConstraintClause<Object>> constraints = new ArrayList<ConstraintClause<Object>>();
		for (final Map<String, Object> constraint : constraintsListMap) {
			final Class<?> dataTypeCls = PropertyType.get(dataType.toLowerCase());
			final ConstraintClause<Object> constraintClause = ConstraintClause.toConstraintClause(constraint, dataTypeCls);
			constraints.add(constraintClause);
		}
		return constraints;
	}

	class ConstraintTest {
		public String message;
		public Object value;
		public boolean valid;

		public ConstraintTest(String message, Object value, boolean valid) {
			this.message = message;
			this.value = value;
			this.valid = valid;
		}
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
