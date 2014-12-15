package org.opentosca.yamlconverter.main;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import org.junit.Assert;
import org.junit.Test;
import org.opentosca.yamlconverter.main.model.Name;

import java.io.StringWriter;
import java.io.Writer;

/**
 * This class makes sure that general reading and writing from/to yaml is possible.
 * @author Sebi
 */
public class SimpleYamlToJavaConversionTest extends BaseTest {

    @Test
    public void testSimpleYamlToJavaConversion() throws Exception {
        YamlReader reader = new YamlReader(testUtils.readYamlTestResource("/yaml/simple.yaml"));
        Name nameObj = reader.read(Name.class);
        Assert.assertNotNull(nameObj);
        Assert.assertNotNull(nameObj.getName());
        Assert.assertFalse(nameObj.getName().equals(""));
    }

    @Test
    public void testSimpleJavaToYamlConversion() throws Exception {
        Name yamlObj = new Name();
        yamlObj.setName("test");
        Writer writer = new StringWriter();
        YamlWriter yamlWriter = new YamlWriter(writer);
        yamlWriter.write(yamlObj);
        yamlWriter.close();
        String output = writer.toString();
        Assert.assertNotNull(output);
        Assert.assertTrue(output.contains("name:"));
        Assert.assertTrue(output.contains("test"));
    }

}
