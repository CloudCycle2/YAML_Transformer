package org.opentosca.yamlconverter.main;

import com.esotericsoftware.yamlbeans.YamlConfig;
import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import org.opentosca.yamlconverter.main.exceptions.ConverterException;
import org.opentosca.yamlconverter.main.interfaces.IToscaYaml2YamlBeanConverter;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.*;

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
		config.setPropertyElementType(ServiceTemplate.class, "inputs", Input.class);
		config.setPropertyElementType(ServiceTemplate.class, "node_templates", NodeTemplate.class);
		config.setPropertyElementType(ServiceTemplate.class, "node_types", NodeType.class);
		config.setPropertyElementType(ServiceTemplate.class, "capability_types", CapabilityType.class);
		config.setPropertyElementType(ServiceTemplate.class, "relationship_types", RelationshipType.class);
		config.setPropertyElementType(ServiceTemplate.class, "artifact_types", ArtifactType.class);
		config.setPropertyElementType(ServiceTemplate.class, "groups", Group.class);
		config.setPropertyElementType(ServiceTemplate.class, "outputs", Output.class);
		config.setPropertyElementType(NodeType.class, "properties", PropertyDefinition.class);
//		config.setPropertyElementType(NodeType.class, "interfaces", OperationDefinition.class);
		config.setPropertyElementType(RelationshipType.class, "properties", PropertyDefinition.class);
//		config.setPropertyElementType(RelationshipType.class, "interfaces", OperationDefinition.class);
		config.setPropertyElementType(CapabilityType.class, "properties", PropertyDefinition.class);
		config.setPropertyElementType(CapabilityDefinition.class, "properties", PropertyDefinition.class);
		config.setPropertyElementType(ArtifactType.class, "properties", PropertyDefinition.class);
	}

}
