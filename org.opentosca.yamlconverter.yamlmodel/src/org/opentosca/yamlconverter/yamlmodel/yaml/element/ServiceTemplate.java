package org.opentosca.yamlconverter.yamlmodel.yaml.element;

import java.util.ArrayList;

public class ServiceTemplate extends YAMLElement {
	private String tosca_definitions_version;
	private String tosca_default_namespace;
	private String template_name;
	private String template_author;
	private String template_version;
	private final ArrayList<NodeType> nodeType = new ArrayList<NodeType>();
	private final ArrayList<CapabilityType> capabilityType = new ArrayList<CapabilityType>();
	private final ArrayList<ArchitectType> architectType = new ArrayList<ArchitectType>();
	private final ArrayList<Output> output = new ArrayList<Output>();
	private final ArrayList<Input> input = new ArrayList<Input>();
	private final ArrayList<Group> group = new ArrayList<Group>();
	private final ArrayList<NodeTemplate> nodeTemplate = new ArrayList<NodeTemplate>();
	private final ArrayList<Import> imports = new ArrayList<Import>();
	private final ArrayList<RelationshipType> relationshipType = new ArrayList<RelationshipType>();

	@Override
	public int hashCode() {
		int hashCode = 0;
		if (this.tosca_definitions_version != null) {
			hashCode += this.tosca_definitions_version.hashCode();
		}
		if (this.tosca_default_namespace != null) {
			hashCode += this.tosca_default_namespace.hashCode();
		}
		if (this.template_name != null) {
			hashCode += this.template_name.hashCode();
		}
		if (this.template_author != null) {
			hashCode += this.template_author.hashCode();
		}
		if (this.template_version != null) {
			hashCode += this.template_version.hashCode();
		}
		if (this.nodeType != null) {
			hashCode += this.nodeType.hashCode();
		}
		if (this.capabilityType != null) {
			hashCode += this.capabilityType.hashCode();
		}
		if (this.architectType != null) {
			hashCode += this.architectType.hashCode();
		}
		if (this.output != null) {
			hashCode += this.output.hashCode();
		}
		if (this.input != null) {
			hashCode += this.input.hashCode();
		}
		if (this.group != null) {
			hashCode += this.group.hashCode();
		}
		if (this.nodeTemplate != null) {
			hashCode += this.nodeTemplate.hashCode();
		}
		if (this.imports != null) {
			hashCode += this.imports.hashCode();
		}
		if (this.relationshipType != null) {
			hashCode += this.relationshipType.hashCode();
		}
		if (hashCode == 0) {
			hashCode = super.hashCode();
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (object instanceof ServiceTemplate) {
			final ServiceTemplate serviceTemplateObject = (ServiceTemplate) object;
			boolean equals = true;
			equals &= this.tosca_definitions_version == serviceTemplateObject.tosca_definitions_version
					|| this.tosca_definitions_version != null
					&& this.tosca_definitions_version.equals(serviceTemplateObject.tosca_definitions_version);
			equals &= this.tosca_default_namespace == serviceTemplateObject.tosca_default_namespace || this.tosca_default_namespace != null
					&& this.tosca_default_namespace.equals(serviceTemplateObject.tosca_default_namespace);
			equals &= this.template_name == serviceTemplateObject.template_name || this.template_name != null
					&& this.template_name.equals(serviceTemplateObject.template_name);
			equals &= this.template_author == serviceTemplateObject.template_author || this.template_author != null
					&& this.template_author.equals(serviceTemplateObject.template_author);
			equals &= this.template_version == serviceTemplateObject.template_version || this.template_version != null
					&& this.template_version.equals(serviceTemplateObject.template_version);
			equals &= this.nodeType == serviceTemplateObject.nodeType || this.nodeType != null
					&& this.nodeType.equals(serviceTemplateObject.nodeType);
			equals &= this.capabilityType == serviceTemplateObject.capabilityType || this.capabilityType != null
					&& this.capabilityType.equals(serviceTemplateObject.capabilityType);
			equals &= this.architectType == serviceTemplateObject.architectType || this.architectType != null
					&& this.architectType.equals(serviceTemplateObject.architectType);
			equals &= this.output == serviceTemplateObject.output || this.output != null
					&& this.output.equals(serviceTemplateObject.output);
			equals &= this.input == serviceTemplateObject.input || this.input != null && this.input.equals(serviceTemplateObject.input);
			equals &= this.group == serviceTemplateObject.group || this.group != null && this.group.equals(serviceTemplateObject.group);
			equals &= this.nodeTemplate == serviceTemplateObject.nodeTemplate || this.nodeTemplate != null
					&& this.nodeTemplate.equals(serviceTemplateObject.nodeTemplate);
			equals &= this.imports == serviceTemplateObject.imports || this.imports != null
					&& this.imports.equals(serviceTemplateObject.imports);
			equals &= this.relationshipType == serviceTemplateObject.relationshipType || this.relationshipType != null
					&& this.relationshipType.equals(serviceTemplateObject.relationshipType);
			return equals;
		}
		return false;
	}
}