package org.opentosca.yamlconverter.yamlmodel.yaml.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceTemplate extends YAMLElement {

	private String tosca_definitions_version = "";
	private String tosca_default_namespace = "";
	private String template_name = "";
	private String template_author = "";
	private String template_version = "";
	private List<String> imports = new ArrayList<String>();
	private Map<String, Input> inputs = new HashMap<String, Input>();
	private Map<String, NodeTemplate> node_templates = new HashMap<String, NodeTemplate>();
	private Map<String, NodeType> node_types = new HashMap<String, NodeType>();
	private Map<String, CapabilityType> capability_types = new HashMap<String, CapabilityType>();
	private Map<String, RelationshipType> relationship_types = new HashMap<String, RelationshipType>();
	private Map<String, ArtifactType> artifact_types = new HashMap<String, ArtifactType>();
	private Map<String, Group> groups = new HashMap<String, Group>();
	private Map<String, Output> outputs = new HashMap<String, Output>();


	public void setTosca_definitions_version(String tosca_definitions_version) {
		if (tosca_definitions_version != null) {
			this.tosca_definitions_version = tosca_definitions_version;
		}
	}

	public String getTosca_definitions_version() {
		return this.tosca_definitions_version;
	}

	public void setTosca_default_namespace(String tosca_default_namespace) {
		if (tosca_default_namespace != null) {
			this.tosca_default_namespace = tosca_default_namespace;
		}
	}

	public String getTosca_default_namespace() {
		return this.tosca_default_namespace;
	}

	public void setTemplate_name(String template_name) {
		if (template_name != null) {
			this.template_name = template_name;
		}
	}

	public String getTemplate_name() {
		return this.template_name;
	}

	public void setTemplate_author(String template_author) {
		if (template_author != null) {
			this.template_author = template_author;
		}
	}

	public String getTemplate_author() {
		return this.template_author;
	}

	public void setTemplate_version(String template_version) {
		if (template_version != null) {
			this.template_version = template_version;
		}
	}

	public String getTemplate_version() {
		return this.template_version;
	}

	public void setImports(List<String> imports) {
		if (imports != null) {
			this.imports = imports;
		}
	}

	public List<String> getImports() {
		return this.imports;
	}

	public void setInputs(Map<String, Input> inputs) {
		if (inputs != null) {
			this.inputs = inputs;
		}
	}

	public Map<String, Input> getInputs() {
		return this.inputs;
	}

	public void setNode_templates(Map<String, NodeTemplate> node_templates) {
		if (node_templates != null) {
			this.node_templates = node_templates;
		}
	}

	public Map<String, NodeTemplate> getNode_templates() {
		return this.node_templates;
	}

	public void setNode_types(Map<String, NodeType> node_types) {
		if (node_types != null) {
			this.node_types = node_types;
		}
	}

	public Map<String, NodeType> getNode_types() {
		return this.node_types;
	}

	public void setCapability_types(Map<String, CapabilityType> capability_types) {
		if (capability_types != null) {
			this.capability_types = capability_types;
		}
	}

	public Map<String, CapabilityType> getCapability_types() {
		return this.capability_types;
	}

	public void setRelationship_types(Map<String, RelationshipType> relationship_types) {
		if (relationship_types != null) {
			this.relationship_types = relationship_types;
		}
	}

	public Map<String, RelationshipType> getRelationship_types() {
		return this.relationship_types;
	}

	public void setArtifact_types(Map<String, ArtifactType> artifact_types) {
		if (artifact_types != null) {
			this.artifact_types = artifact_types;
		}
	}

	public Map<String, ArtifactType> getArtifact_types() {
		return this.artifact_types;
	}

	public void setGroups(Map<String, Group> groups) {
		if (groups != null) {
			this.groups = groups;
		}
	}

	public Map<String, Group> getGroups() {
		return this.groups;
	}

	public void setOutputs(Map<String, Output> outputs) {
		if (outputs != null) {
			this.outputs = outputs;
		}
	}

	public Map<String, Output> getOutputs() {
		return this.outputs;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		ServiceTemplate that = (ServiceTemplate) o;

		if (!artifact_types.equals(that.artifact_types)) return false;
		if (!capability_types.equals(that.capability_types)) return false;
		if (!groups.equals(that.groups)) return false;
		if (!imports.equals(that.imports)) return false;
		if (!inputs.equals(that.inputs)) return false;
		if (!node_templates.equals(that.node_templates)) return false;
		if (!node_types.equals(that.node_types)) return false;
		if (!outputs.equals(that.outputs)) return false;
		if (!relationship_types.equals(that.relationship_types)) return false;
		if (!template_author.equals(that.template_author)) return false;
		if (!template_name.equals(that.template_name)) return false;
		if (!template_version.equals(that.template_version)) return false;
		if (!tosca_default_namespace.equals(that.tosca_default_namespace)) return false;
		if (!tosca_definitions_version.equals(that.tosca_definitions_version)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + tosca_definitions_version.hashCode();
		result = 31 * result + tosca_default_namespace.hashCode();
		result = 31 * result + template_name.hashCode();
		result = 31 * result + template_author.hashCode();
		result = 31 * result + template_version.hashCode();
		result = 31 * result + imports.hashCode();
		result = 31 * result + inputs.hashCode();
		result = 31 * result + node_templates.hashCode();
		result = 31 * result + node_types.hashCode();
		result = 31 * result + capability_types.hashCode();
		result = 31 * result + relationship_types.hashCode();
		result = 31 * result + artifact_types.hashCode();
		result = 31 * result + groups.hashCode();
		result = 31 * result + outputs.hashCode();
		return result;
	}
}