package de.opentosca.yamlconverter.main;

import java.io.StringWriter;
import java.io.Writer;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;

import de.opentosca.yamlconverter.main.interfaces.ItoscaYAML2YAMLbeanConverter;
import de.opentosca.yamlconverter.main.interfaces.YamlRootElement;

/**
 * This Converter uses YamlBeans to convert YAML to YAML beans (bi-directional).
 * 
 * @author Jonas Heinisch
 *
 */
public class YamlBeansConverter implements ItoscaYAML2YAMLbeanConverter {

	@Override
	public YamlRootElement yaml2yamlbean(String yamlstring) {
		YamlReader reader = new YamlReader(yamlstring);
		return reader.read(YamlRootElement.class);
	}

	@Override
	public String yamlbean2yaml(YamlRootElement root) {
		Writer output = new StringWriter();
		YamlWriter writer = new YamlWriter(output);
		writer.write(root);
		writer.close();
		return output.toString();
	}

}
