package org.opentosca.yamlconverter.main;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import org.opentosca.yamlconverter.main.exceptions.ConverterException;
import org.opentosca.yamlconverter.main.interfaces.IToscaYaml2YamlBeanConverter;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.YAMLElement;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.YAMLFileRoot;

import java.io.StringWriter;
import java.io.Writer;

/**
 * This Converter uses YamlBeans to convert YAML to YAML beans (bi-directional).
 * 
 * @author Jonas Heinisch
 *
 */
public class YamlBeansConverter implements IToscaYaml2YamlBeanConverter {

	@Override
	public YAMLElement yaml2yamlbean(String yamlstring) throws ConverterException {
		YamlReader reader = new YamlReader(yamlstring);
		try {
			return reader.read(YAMLFileRoot.class);
		} catch (YamlException e) {
			throw new ConverterException(e);
		}
	}

	@Override
	public String yamlbean2yaml(YAMLElement root) throws ConverterException {
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
