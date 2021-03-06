package org.opentosca.yamlconverter.main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.opentosca.yamlconverter.constraints.ConstraintClause;
import org.opentosca.yamlconverter.main.interfaces.IToscaYaml2YamlBeanConverter;
import org.opentosca.yamlconverter.main.interfaces.IToscaYamlParser;
import org.opentosca.yamlconverter.main.util.BaseTest;
import org.opentosca.yamlconverter.main.utils.PropertyType;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.Input;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.ServiceTemplate;

public class YamlInputTest extends BaseTest {
	private final IToscaYaml2YamlBeanConverter converter = new YamlBeansConverter();
	private final IToscaYamlParser parser = new Parser();

	@Test
	public void testReadYamlInputs_useInputs() throws Exception {
		final ServiceTemplate templ = this.converter.convertToYamlBean(this.testUtils.readYamlTestResource("/yaml/useInputs.yml"));
		Assert.assertNotNull(templ);
		final Object memSize = templ.getNode_templates().get("my_server").getProperties().get("mem_size");
		Assert.assertNotNull(memSize);
		Assert.assertTrue(((Map<String, Object>) memSize).containsKey("get_input"));
	}

	@Test
	public void testGetInputRequirements() throws Exception {
		final String yamlInput = this.testUtils.readYamlTestResource("/yaml/inputs.yml");
		this.parser.parse(yamlInput);
		final Map<String, String> inputs = this.parser.getInputRequirementsText();
		Assert.assertNotNull(inputs);
		Assert.assertEquals(3, inputs.size());
		Assert.assertTrue("must contain an input with key 'foo'", inputs.containsKey("foo"));
	}

	@Test
	public void testGetInputRequirements_WithoutConstraints() throws Exception {
		final String yamlInput = this.testUtils.readYamlTestResource("/yaml/inputs_emptyConstraints.yml");
		this.parser.parse(yamlInput);
		final Map<String, String> inputs = this.parser.getInputRequirementsText();
		Assert.assertNotNull(inputs);
		Assert.assertEquals(1, inputs.size());
		Assert.assertTrue("must contain an input with key 'foo'", inputs.containsKey("foo"));
		Assert.assertTrue("constraints are not in description", inputs.get("foo").toLowerCase().contains("constraints: none"));
	}

	@Test
	public void testReadYamlInputs() throws Exception {
		final ServiceTemplate templ = this.converter.convertToYamlBean(this.testUtils.readYamlTestResource("/yaml/inputs.yml"));
		Assert.assertNotNull(templ);
		final Map<String, Input> inputs = templ.getInputs();
		Assert.assertNotNull(inputs);
		Assert.assertEquals("template must have 3 inputs", 3, inputs.size());
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

		constraintTests.clear();
		final Calendar cal = Calendar.getInstance();
		cal.set(2002, 4, 12);
		constraintTests.add(new ConstraintTest("must be valid", cal.getTime(), true));
		cal.set(1990, 4, 12);
		constraintTests.add(new ConstraintTest("must not be valid", cal.getTime(), false));
		cal.set(3000, 4, 12);
		constraintTests.add(new ConstraintTest("must not be valid", cal.getTime(), false));

		final Input timeInput = inputs.get("time");
		constraints = toConstraints(timeInput.getConstraints(), timeInput.getType());

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

}
