package org.opentosca.yamlconverter.main;

import java.io.StringWriter;
import java.io.Writer;

import org.opentosca.yamlconverter.main.exceptions.ConverterException;
import org.opentosca.yamlconverter.main.interfaces.IToscaYaml2YamlBeanConverter;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.ArtifactType;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.CapabilityType;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.Group;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.Import;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.Input;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.NodeTemplate;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.NodeType;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.Output;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.RelationshipType;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.ServiceTemplate;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.YAMLElement;

import com.esotericsoftware.yamlbeans.YamlConfig;
import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import com.esotericsoftware.yamlbeans.scalar.ScalarSerializer;

/**
 * This Converter uses YamlBeans to convert YAML to YAML beans (bi-directional).
 *
 * @author Jonas Heinisch
 *
 */
public class YamlBeansConverter implements IToscaYaml2YamlBeanConverter {

	@Override
	public ServiceTemplate yaml2yamlbean(String yamlstring) throws ConverterException {
		if (yamlstring == null || yamlstring.equals("")) {
			throw new IllegalArgumentException("YAML string may not be empty!");
		}
		final YamlReader reader = new YamlReader(yamlstring);
		adjustConfig(reader.getConfig());
		try {
			return reader.read(ServiceTemplate.class);
		} catch (final YamlException e) {
			throw new ConverterException(e);
		}
	}

	@Override
	public String yamlbean2yaml(YAMLElement root) throws ConverterException {
		if (root == null) {
			throw new IllegalArgumentException("Root element may not be null!");
		}
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
		config.setScalarSerializer(String.class, new ScalarSerializer<String>() {
			@Override
			public String write(String object) throws YamlException {
				System.out.println("Write: " + object);
				return object;
			}

			@Override
			public String read(String value) throws YamlException {
				System.out.println("Read: " + value);
				return value;
			}
		});
		config.setScalarSerializer(Integer.class, new ScalarSerializer<String>() {
			@Override
			public String write(String object) throws YamlException {
				System.out.println("Write: " + object);
				return object;
			}

			@Override
			public String read(String value) throws YamlException {
				System.out.println("Read: " + value);
				return value;
			}
		});
		config.setPropertyElementType(ServiceTemplate.class, "imports", Import.class);
		config.setPropertyElementType(ServiceTemplate.class, "inputs", Input.class);
		config.setPropertyElementType(ServiceTemplate.class, "node_templates", NodeTemplate.class);
		config.setPropertyElementType(ServiceTemplate.class, "node_types", NodeType.class);
		config.setPropertyElementType(ServiceTemplate.class, "capability_types", CapabilityType.class);
		config.setPropertyElementType(ServiceTemplate.class, "relationship_types", RelationshipType.class);
		config.setPropertyElementType(ServiceTemplate.class, "artifact_types", ArtifactType.class);
		config.setPropertyElementType(ServiceTemplate.class, "groups", Group.class);
		config.setPropertyElementType(ServiceTemplate.class, "outputs", Output.class);
	}

}
