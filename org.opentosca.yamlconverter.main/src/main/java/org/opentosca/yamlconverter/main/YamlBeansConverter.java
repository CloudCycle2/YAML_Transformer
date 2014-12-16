package org.opentosca.yamlconverter.main;

import java.io.StringWriter;
import java.io.Writer;

import org.opentosca.yamlconverter.main.exceptions.ConverterException;
import org.opentosca.yamlconverter.main.interfaces.IToscaYaml2YamlBeanConverter;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.NodeTemplate;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.YAMLElement;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.YAMLFileRoot;

import com.esotericsoftware.yamlbeans.YamlConfig;
import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;

/**
 * This Converter uses YamlBeans to convert YAML to YAML beans (bi-directional).
 *
 * @author Jonas Heinisch
 *
 */
public class YamlBeansConverter implements IToscaYaml2YamlBeanConverter {

	@Override
	public YAMLFileRoot yaml2yamlbean(String yamlstring) throws ConverterException {
		final YamlReader reader = new YamlReader(yamlstring);
		adjustConfig(reader.getConfig());
		try {
			return reader.read(YAMLFileRoot.class);
		} catch (final YamlException e) {
			throw new ConverterException(e);
		}
	}

	@Override
	public String yamlbean2yaml(YAMLElement root) throws ConverterException {
		final Writer output = new StringWriter();
		final YamlWriter writer = new YamlWriter(output);
		adjustConfig(writer.getConfig());
		try {
			writer.write(root);
			writer.close();
		} catch (final YamlException e) {
			throw new ConverterException(e);
		}
		return output.toString();
	}

	public void adjustConfig(YamlConfig config) {
		config.setPropertyElementType(YAMLFileRoot.class, "node_templates", NodeTemplate.class);
	}

}
