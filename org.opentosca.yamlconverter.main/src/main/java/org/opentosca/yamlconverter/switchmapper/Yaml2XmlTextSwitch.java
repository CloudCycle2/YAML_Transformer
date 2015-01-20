package org.opentosca.yamlconverter.switchmapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.opentosca.yamlconverter.yamlmodel.yaml.element.NodeTemplate;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.ServiceTemplate;

public class Yaml2XmlTextSwitch {
	public static final String TYPES_XSD_FILENAME = "TypesGen.xsd";

	public static final String TYPE_NS = "http://www.example.org/tosca/yamlgen/types";

	public static final String TOSCA_NS = "http://docs.oasis-open.org/tosca/ns/2011/12";

	public static final String TARGETNAMESPACE = "http://www.example.org/tosca/yamlgen";

	private int level = 0;
	private long uniqueID = 0;

	private final StringBuilder xml = new StringBuilder();
	private final StringBuilder xsd = new StringBuilder();

	// TODO: Object should be NodeType
	/**
	 * NodeType name -> NodeType
	 */
	private final Map<String, Object> nodeTypes = new HashMap<>();

	/**
	 * inputVariable -> input value
	 */
	private Map<String, String> input = new HashMap<String, String>();

	/**
	 * propertyVariable -> property value
	 */
	private final Map<String, String> propertyvalues = new HashMap<String, String>();

	/**
	 * Returns a Map with input parameters.
	 *
	 * @param root the Root Element to parse.
	 * @return inputVariable -> requirement
	 */
	public Map<String, String> getInputRequirements(ServiceTemplate root) {
		final Map<String, String> inputreq = new HashMap<String, String>();
		// for(Input in : root.getInputs()){
		// inputreq.put(in.getName(), in.getDescription());
		// }
		return inputreq;
	}

	/**
	 * Set input parameters.
	 *
	 * @param input inputVariable -> input value
	 */
	public void setInput(Map<String, String> input) {
		this.input = input;
	}

	/**
	 * Parse the
	 *
	 * @param root
	 */
	public void parse(ServiceTemplate root) {
		if (root == null) {
			return;
		}
		this.xml.append("<Definitions");
		this.xml.append(" id=\"root\"");
		this.xml.append(" name=\"root\"");
		this.xml.append(" targetNameSpace=\"" + TARGETNAMESPACE + "\"");
		this.xml.append(" xmlns=\"" + TOSCA_NS + "\"");
		this.xml.append(" xmlns:types=\"" + TYPE_NS + "\"");
		this.xml.append(">");
		parseImports(root);
		parseServiceTemplate(root);
		parseDocumentation(root.getDescription());
		line();
		this.xml.append("</Definitions>");
	}

	private void parseDocumentation(String description) {
		if (description == null || description.isEmpty()) {
			return;
		}
		linep();
		this.xml.append("<documentation>");
		lineone();
		this.xml.append(description);
		linem();
		this.xml.append("</documentation>");
	}

	private void parseImports(ServiceTemplate root) {
		lineone();
		this.xml.append("<Import");
		this.xml.append(" importType=\"http://www.w3.org/2001/XMLSchema\"");
		this.xml.append(" location=\"" + TYPES_XSD_FILENAME + "\"");
		this.xml.append(" namespace=\"" + TYPE_NS + "\" />");
	}

	private void parseServiceTemplate(ServiceTemplate root) {
		linep();
		this.xml.append("<ServiceTemplate");
		this.xml.append(" id=\"servicetemplate\"");
		this.xml.append(" name=\"servicetemplate\"");
		this.xml.append(">");
		parseTopologyTemplate(root);
		linem();
		this.xml.append("</ServiceTemplate>");
	}

	private void parseTopologyTemplate(ServiceTemplate root) {
		linep();
		this.xml.append("<TopologyTemplate>");
		for (final NodeTemplate entry : root.getNodeTemplate()) {
			parseNodeTemplate(entry);
		}
		linem();
		this.xml.append("</TopologyTemplate>");
	}

	private void parseNodeTemplate(NodeTemplate entry) {
		linep();
		this.xml.append("<NodeTemplate");
		// TODO: name!
		// this.xml.append(" id=\"" + entry.getName() + "\"");
		// this.xml.append(" name=\"" + entry.getName() + "\"");
		this.xml.append(" type=\"" + entry.getType() + "\"");
		this.xml.append(">");
		parseDocumentation(entry.getDescription());
		// parseNodeTemplateProperties(entry.getProperties(), entry.getType(), entry.getName());
		linem();
		this.xml.append("</NodeTemplate>");
	}

	private void parseNodeTemplateProperties(Map<String, Object> properties, String type, String nodename) {
		final String propertyname = getNodePropertyName(type);
		linep();
		this.xml.append("<Properties>");
		linep();
		this.xml.append("<" + propertyname + ">");
		for (final Entry<String, Object> entry : properties.entrySet()) {
			if (!isGetter(entry.getValue())) {
				this.propertyvalues.put(nodename + "#" + entry.getKey(), entry.getValue().toString());
			}
			parseNodeTemplatePropertyEntry(entry);
		}
		linem();
		this.xml.append("</" + propertyname + ">");
		linem();
		this.xml.append("</Properties>");
	}

	private boolean isGetter(Object value) {
		if (value instanceof Map<?, ?>) {
			return true;
		}
		return false;
	}

	private void parseNodeTemplatePropertyEntry(Entry<String, Object> entry) {
		lineone();
		this.xml.append("<" + entry.getKey() + ">");
		if (isGetter(entry.getValue())) {
			@SuppressWarnings("unchecked")
			final Map<String, List<String>> getterMap = (Map<String, List<String>>) entry.getValue();
			for (final Entry<String, List<String>> getter : getterMap.entrySet()) {
				this.xml.append(getter.getKey() + "(");
				for (final String lelem : getter.getValue()) {
					this.xml.append("#" + lelem);
				}
				this.xml.append(")");
			}
		} else {
			this.xml.append(entry.getValue());
		}
		this.xml.append("</" + entry.getKey() + ">");
	}

	private String getNodePropertyName(String type) {
		return "types:" + type + "Properties";
	}

	/**
	 * Get the parsed XML. Execute parse(..) before.
	 *
	 * @return parsed XML
	 */
	public String getXML() {
		String result = this.xml.toString();
		for (final Entry<String, String> repdata : this.input.entrySet()) {
			result = result.replace("get_input(#" + repdata.getKey() + ")", repdata.getValue());
		}
		for (final Entry<String, String> repdata : this.propertyvalues.entrySet()) {
			result = result.replace("get_property(#" + repdata.getKey() + ")", repdata.getValue());
		}
		// TODO: get_ref_property
		return result;
	}

	/**
	 * Get the parsed XSD. Execute parse(..) before.
	 *
	 * @return parsed XSD
	 */
	public String getXSD() {
		return this.xsd.toString();
	}

	/**
	 * Adds a unique number to the prefix.
	 *
	 * @param prefix The prefix
	 * @return A unique String.
	 */
	private String unique(String prefix) {
		long id = this.uniqueID++;
		if (id < 0) {
			// Negative IDs are not pretty.
			this.uniqueID = 0;
			id = this.uniqueID;
		}
		return prefix + id;
	}

	/**
	 * If name contains multiple aliases the result is the first.
	 *
	 * @param name The name field of an XML element.
	 * @return A valid ID.
	 */
	private String name2id(String name) {
		return name.split(",")[0];
	}

	private void line() {
		this.xml.append("\n");
		for (int i = 0; i < this.level; i++) {
			this.xml.append("\t");
		}
	}

	private void linep() {
		this.level++;
		this.xml.append("\n");
		for (int i = 0; i < this.level; i++) {
			this.xml.append("\t");
		}
	}

	private void linem() {
		this.xml.append("\n");
		for (int i = 0; i < this.level; i++) {
			this.xml.append("\t");
		}
		this.level--;
	}

	private void lineone() {
		this.xml.append("\n");
		for (int i = 0; i < this.level + 1; i++) {
			this.xml.append("\t");
		}
	}
}
