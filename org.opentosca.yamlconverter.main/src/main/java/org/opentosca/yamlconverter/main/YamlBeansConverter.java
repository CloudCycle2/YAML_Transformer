package org.opentosca.yamlconverter.main;

import java.io.StringWriter;
import java.io.Writer;

import org.opentosca.yamlconverter.main.exceptions.ConverterException;
import org.opentosca.yamlconverter.main.interfaces.IToscaYaml2YamlBeanConverter;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.ArtifactType;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.CapabilityDefinition;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.CapabilityType;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.Group;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.Input;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.NodeTemplate;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.NodeType;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.Output;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.PropertyDefinition;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.RelationshipType;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.ServiceTemplate;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.YAMLElement;

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

	/**
	 * Convert {@code yamlString} to {@link org.opentosca.yamlconverter.yamlmodel.yaml.element.ServiceTemplate} using
	 * {@link com.esotericsoftware.yamlbeans.YamlReader}. Makes use of {@link #adjustConfig(com.esotericsoftware.yamlbeans.YamlConfig)} to
	 * set some properties for {@link com.esotericsoftware.yamlbeans.YamlConfig}.
	 *
	 * @param yamlString A Tosca YAML in a String
	 * @return service template containing values from {@code yamlString}
	 * @throws ConverterException
	 */
	@Override
	public ServiceTemplate convertToYamlBean(String yamlString) throws ConverterException {
		if (yamlString == null || yamlString.equals("")) {
			throw new IllegalArgumentException("YAML string may not be empty!");
		}
		final YamlReader reader = new YamlReader(yamlString);
		adjustConfig(reader.getConfig());
		try {
			return reader.read(ServiceTemplate.class);
		} catch (final YamlException e) {
			throw new ConverterException(e);
		}
	}

	/**
	 * Convert {@link org.opentosca.yamlconverter.yamlmodel.yaml.element.YAMLElement} to a string using
	 * {@link com.esotericsoftware.yamlbeans.YamlWriter}. Makes use of {@link #adjustConfig(com.esotericsoftware.yamlbeans.YamlConfig)} to
	 * set properties for {@link com.esotericsoftware.yamlbeans.YamlConfig}.
	 *
	 * @param yamlRoot child element of {@link org.opentosca.yamlconverter.yamlmodel.yaml.element.YAMLElement} containing values
	 * @return {@code yamlRoot} as YAML string
	 * @throws ConverterException
	 */
	@Override
	public String convertToYaml(YAMLElement yamlRoot) throws ConverterException {
		if (yamlRoot == null) {
			throw new IllegalArgumentException("Root element may not be null!");
		}
		final Writer output = new StringWriter();
		final YamlWriter writer = new YamlWriter(output);
		adjustConfig(writer.getConfig());
		try {
			writer.write(yamlRoot);
			writer.close();
		} catch (final YamlException e) {
			throw new ConverterException(e);
		}
		return output.toString();
	}

	/**
	 * Adjusts the {@link YamlConfig} to our UseCase.
	 * 
	 * @param config The initial {@link YamlConfig}
	 */
	private void adjustConfig(YamlConfig config) {
		config.setPropertyElementType(ServiceTemplate.class, "inputs", Input.class);
		config.setPropertyElementType(ServiceTemplate.class, "node_templates", NodeTemplate.class);
		config.setPropertyElementType(ServiceTemplate.class, "node_types", NodeType.class);
		config.setPropertyElementType(ServiceTemplate.class, "capability_types", CapabilityType.class);
		config.setPropertyElementType(ServiceTemplate.class, "relationship_types", RelationshipType.class);
		config.setPropertyElementType(ServiceTemplate.class, "artifact_types", ArtifactType.class);
		config.setPropertyElementType(ServiceTemplate.class, "groups", Group.class);
		config.setPropertyElementType(ServiceTemplate.class, "outputs", Output.class);
		config.setPropertyElementType(NodeType.class, "properties", PropertyDefinition.class);
		config.setPropertyElementType(RelationshipType.class, "properties", PropertyDefinition.class);
		config.setPropertyElementType(CapabilityType.class, "properties", PropertyDefinition.class);
		config.setPropertyElementType(CapabilityDefinition.class, "properties", PropertyDefinition.class);
		config.setPropertyElementType(ArtifactType.class, "properties", PropertyDefinition.class);
	}

}
