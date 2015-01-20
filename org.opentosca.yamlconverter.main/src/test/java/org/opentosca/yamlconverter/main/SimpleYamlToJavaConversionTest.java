package org.opentosca.yamlconverter.main;

import java.io.StringWriter;
import java.io.Writer;

import org.junit.Assert;
import org.junit.Test;
import org.opentosca.yamlconverter.main.model.Name;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;

/**
 * This class makes sure that general reading and writing from/to yaml is possible.
 *
 * @author Sebi
 */
public class SimpleYamlToJavaConversionTest extends BaseTest {

	@Test
	public void testSimpleYamlToJavaConversion() throws Exception {
		final YamlReader reader = new YamlReader(this.testUtils.readYamlTestResource("/yaml/simple.yaml"));
		final Name nameObj = reader.read(Name.class);
		Assert.assertNotNull(nameObj);
		Assert.assertNotNull(nameObj.getName());
		Assert.assertFalse(nameObj.getName().equals(""));
		Assert.assertTrue(nameObj.getName().equals("Test"));
	}

	@Test
	public void testSimpleJavaToYamlConversion() throws Exception {
		final Name yamlObj = new Name("Test");
		final Writer writer = new StringWriter();
		final YamlWriter yamlWriter = new YamlWriter(writer);
		yamlWriter.write(yamlObj);
		yamlWriter.close();
		final String output = writer.toString();
		Assert.assertNotNull(output);
		Assert.assertTrue(output.contains("name:"));
		Assert.assertTrue(output.contains("Test"));
	}

}
