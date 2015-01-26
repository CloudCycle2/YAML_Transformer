package org.opentosca.yamlconverter.yamlmodel.yaml.element;

import java.util.Map;

public class ServiceTemplate extends YAMLElement {
	private String tosca_definitions_version;
	private String tosca_default_namespace;
	private String template_name;
	private String template_author;
	private String template_version;
	private Map<String, Import> imports;
	private Map<String, Input> inputs;
	private Map<String, NodeTemplate> node_templates;
	private Map<String, NodeType> node_types;
	private Map<String, CapabilityType> capability_types;
	private Map<String, RelationshipType> relationship_types;
	private Map<String, ArtefactType> artefact_types;
	private Map<String, Group> groups;
	private Map<String, Output> outputs;

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
			equals &= this.imports == serviceTemplateObject.imports;
			equals &= this.inputs == serviceTemplateObject.inputs;
			equals &= this.node_templates == serviceTemplateObject.node_templates;
			equals &= this.node_types == serviceTemplateObject.node_types;
			equals &= this.capability_types == serviceTemplateObject.capability_types;
			equals &= this.relationship_types == serviceTemplateObject.relationship_types;
			equals &= this.artefact_types == serviceTemplateObject.artefact_types;
			equals &= this.groups == serviceTemplateObject.groups;
			equals &= this.outputs == serviceTemplateObject.outputs;
			return equals;
		}
		return false;
	}

	public void setTosca_definitions_version(String tosca_definitions_version) {
		this.tosca_definitions_version = tosca_definitions_version;
	}

	public String getTosca_definitions_version() {
		return this.tosca_definitions_version;
	}

	public void setTosca_default_namespace(String tosca_default_namespace) {
		this.tosca_default_namespace = tosca_default_namespace;
	}

	public String getTosca_default_namespace() {
		return this.tosca_default_namespace;
	}

	public void setTemplate_name(String template_name) {
		this.template_name = template_name;
	}

	public String getTemplate_name() {
		return this.template_name;
	}

	public void setTemplate_author(String template_author) {
		this.template_author = template_author;
	}

	public String getTemplate_author() {
		return this.template_author;
	}

	public void setTemplate_version(String template_version) {
		this.template_version = template_version;
	}

	public String getTemplate_version() {
		return this.template_version;
	}

	public void setImports(Map<String, Import> imports) {
		this.imports = imports;
	}

	public Map<String, Import> getImports() {
		return this.imports;
	}

	public void setInputs(Map<String, Input> inputs) {
		this.inputs = inputs;
	}

	public Map<String, Input> getInputs() {
		return this.inputs;
	}

	public void setNode_templates(Map<String, NodeTemplate> node_templates) {
		this.node_templates = node_templates;
	}

	public Map<String, NodeTemplate> getNode_templates() {
		return this.node_templates;
	}

	public void setNode_types(Map<String, NodeType> node_types) {
		this.node_types = node_types;
	}

	public Map<String, NodeType> getNode_types() {
		return this.node_types;
	}

	public void setCapability_types(Map<String, CapabilityType> capability_types) {
		this.capability_types = capability_types;
	}

	public Map<String, CapabilityType> getCapability_types() {
		return this.capability_types;
	}

	public void setRelationship_types(Map<String, RelationshipType> relationship_types) {
		this.relationship_types = relationship_types;
	}

	public Map<String, RelationshipType> getRelationship_types() {
		return this.relationship_types;
	}

	public void setArtefact_types(Map<String, ArtefactType> artefact_types) {
		this.artefact_types = artefact_types;
	}

	public Map<String, ArtefactType> getArtefact_types() {
		return this.artefact_types;
	}

	public void setGroups(Map<String, Group> groups) {
		this.groups = groups;
	}

	public Map<String, Group> getGroups() {
		return this.groups;
	}

	public void setOutputs(Map<String, Output> outputs) {
		this.outputs = outputs;
	}

	public Map<String, Output> getOutputs() {
		return this.outputs;
	}
}