package org.opentosca.yamlconverter.main;

import java.io.StringWriter;
import java.io.Writer;

import org.opentosca.model.yaml.YamlRootElement;
import org.opentosca.yamlconverter.main.exceptions.ConverterException;
import org.opentosca.yamlconverter.main.interfaces.ItoscaYAML2YAMLbeanConverter;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;

/**
 * This Converter uses YamlBeans to convert YAML to YAML beans (bi-directional).
 * 
 * @author Jonas Heinisch
 *
 */
public class YamlBeansConverter implements ItoscaYAML2YAMLbeanConverter {

	@Override
	public YamlRootElement yaml2yamlbean(String yamlstring) throws ConverterException {
		YamlReader reader = new YamlReader(yamlstring);
		try {
			return reader.read(YamlRootElement.class);
		} catch (YamlException e) {
			throw new ConverterException(e);
		}
	}

	@Override
	public String yamlbean2yaml(YamlRootElement root) throws ConverterException {
		Writer output = new StringWriter();
		YamlWriter writer = new YamlWriter(output);
		try {
			writer.write(root);
			writer.close();
		} catch (YamlException e) {
			throw new ConverterException(e);
		}
		return output.toString();
	}

}
