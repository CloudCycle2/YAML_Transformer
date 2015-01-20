package yaml.element;

import java.util.ArrayList;
import yaml.element.NodeType;
import yaml.element.CapabilityType;
import yaml.element.ArchitectType;
import yaml.element.Output;
import yaml.element.Input;
import yaml.element.Group;
import yaml.element.NodeTemplate;
import yaml.element.Import;
import yaml.element.RelationshipType;

public class ServiceTemplate extends YAMLElement {
	private String tosca_definitions_version;
	private String tosca_default_namespace;
	private String template_name;
	private String template_author;
	private String template_version;
	private ArrayList<NodeType> nodeType = new ArrayList<NodeType>();
	public ArrayList<CapabilityType> capabilityType = new ArrayList<CapabilityType>();
	private ArrayList<ArchitectType> architectType = new ArrayList<ArchitectType>();
	private ArrayList<Output> output = new ArrayList<Output>();
	private ArrayList<Input> input = new ArrayList<Input>();
	private ArrayList<Group> group = new ArrayList<Group>();
	private ArrayList<NodeTemplate> nodeTemplate = new ArrayList<NodeTemplate>();
	private ArrayList<Import> import = new ArrayList<Import>();
	private ArrayList<RelationshipType> relationshipType = new ArrayList<RelationshipType>();

	public int hashCode() {
		int hashCode = 0;
		if ( this.tosca_definitions_version != null ) {
			hashCode += this.tosca_definitions_version.hashCode();
		}
		if ( this.tosca_default_namespace != null ) {
			hashCode += this.tosca_default_namespace.hashCode();
		}
		if ( this.template_name != null ) {
			hashCode += this.template_name.hashCode();
		}
		if ( this.template_author != null ) {
			hashCode += this.template_author.hashCode();
		}
		if ( this.template_version != null ) {
			hashCode += this.template_version.hashCode();
		}
		if ( this.nodeType != null ) {
			hashCode += this.nodeType.hashCode();
		}
		if ( this.capabilityType != null ) {
			hashCode += this.capabilityType.hashCode();
		}
		if ( this.architectType != null ) {
			hashCode += this.architectType.hashCode();
		}
		if ( this.output != null ) {
			hashCode += this.output.hashCode();
		}
		if ( this.input != null ) {
			hashCode += this.input.hashCode();
		}
		if ( this.group != null ) {
			hashCode += this.group.hashCode();
		}
		if ( this.nodeTemplate != null ) {
			hashCode += this.nodeTemplate.hashCode();
		}
		if ( this.import != null ) {
			hashCode += this.import.hashCode();
		}
		if ( this.relationshipType != null ) {
			hashCode += this.relationshipType.hashCode();
		}
		if ( hashCode == 0 ) {
			hashCode = super.hashCode();
		}
		return hashCode;
	}

	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (object instanceof ServiceTemplate) {
			ServiceTemplate serviceTemplateObject = (ServiceTemplate) object;
			boolean equals = true;
			equals &= ((this.tosca_definitions_version == serviceTemplateObject.tosca_definitions_version)
				|| (this.tosca_definitions_version != null && this.tosca_definitions_version.equals(serviceTemplateObject.tosca_definitions_version)));
			equals &= ((this.tosca_default_namespace == serviceTemplateObject.tosca_default_namespace)
				|| (this.tosca_default_namespace != null && this.tosca_default_namespace.equals(serviceTemplateObject.tosca_default_namespace)));
			equals &= ((this.template_name == serviceTemplateObject.template_name)
				|| (this.template_name != null && this.template_name.equals(serviceTemplateObject.template_name)));
			equals &= ((this.template_author == serviceTemplateObject.template_author)
				|| (this.template_author != null && this.template_author.equals(serviceTemplateObject.template_author)));
			equals &= ((this.template_version == serviceTemplateObject.template_version)
				|| (this.template_version != null && this.template_version.equals(serviceTemplateObject.template_version)));
			equals &= ((this.nodeType == serviceTemplateObject.nodeType)
				|| (this.nodeType != null && this.nodeType.equals(serviceTemplateObject.nodeType)));
			equals &= ((this.capabilityType == serviceTemplateObject.capabilityType)
				|| (this.capabilityType != null && this.capabilityType.equals(serviceTemplateObject.capabilityType)));
			equals &= ((this.architectType == serviceTemplateObject.architectType)
				|| (this.architectType != null && this.architectType.equals(serviceTemplateObject.architectType)));
			equals &= ((this.output == serviceTemplateObject.output)
				|| (this.output != null && this.output.equals(serviceTemplateObject.output)));
			equals &= ((this.input == serviceTemplateObject.input)
				|| (this.input != null && this.input.equals(serviceTemplateObject.input)));
			equals &= ((this.group == serviceTemplateObject.group)
				|| (this.group != null && this.group.equals(serviceTemplateObject.group)));
			equals &= ((this.nodeTemplate == serviceTemplateObject.nodeTemplate)
				|| (this.nodeTemplate != null && this.nodeTemplate.equals(serviceTemplateObject.nodeTemplate)));
			equals &= ((this.import == serviceTemplateObject.import)
				|| (this.import != null && this.import.equals(serviceTemplateObject.import)));
			equals &= ((this.relationshipType == serviceTemplateObject.relationshipType)
				|| (this.relationshipType != null && this.relationshipType.equals(serviceTemplateObject.relationshipType)));
			return equals;
		}
		return false;
	}
}