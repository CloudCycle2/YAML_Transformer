package org.opentosca.yamlconverter.switchmapper;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.opentosca.model.tosca.Definitions;
import org.opentosca.model.tosca.TDocumentation;
import org.opentosca.model.tosca.TImport;
import org.opentosca.model.tosca.TServiceTemplate;
import org.opentosca.model.tosca.TTopologyTemplate;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.ServiceTemplate;

/**
 * This class can parse ServiceTemplates (YAML bean) to Definitions (XML bean).
 *
 */
public class Yaml2XmlSwitch {
	/**
	 * The XML-Namespace of XML-Schemas.
	 */
	public static final String XMLSCHEMA_NS = "http://www.w3.org/2001/XMLSchema";

	/**
	 * The XML-Namespace of the created document.
	 */
	public static final String NS = "http://www.example.org/tosca/yamlgen";

	/**
	 * The default user input.
	 */
	public static final String DEFAULT_USER_INPUT = "DEFAULTUSERINPUT";

	/**
	 * The XML-Namespace of the types.
	 */
	public static String TYPESNS = "http://www.example.org/tosca/yamlgen/types";

	/**
	 * A counter for creating unique IDs.
	 */
	private long uniqueID = 0;

	/**
	 * StringBuilder for the XSD.
	 */
	private StringBuilder xsd;

	/**
	 * The service template to parse.
	 */
	private ServiceTemplate st;

	/**
	 * InputVarName -> InputVarValue
	 */
	private Map<String, String> inputs = new HashMap<>();

	private Definitions toscaResult = new Definitions();

	Definitions getToscaResult() {
		return this.toscaResult;
	}

	ServiceTemplate getServiceTemplate() {
		return this.st;
	}

	/**
	 * Parses {@link ServiceTemplate} to {@link Definitions}.
	 *
	 * @param st the {@link ServiceTemplate} to parse
	 * @return the parsed {@link Definitions}
	 */
	public Definitions parse(ServiceTemplate st) {
		this.xsd = new StringBuilder(); // reset
		this.st = st;
		this.toscaResult = new Definitions();
		return processServiceTemplate(st);
	}

	/**
	 * Returns an additional XSD to support the properties of templates.
	 *
	 * @return additional XSD.
	 */
	public String getXSD() {
		final String pre = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\""
				+ "targetNamespace=\"" + TYPESNS + "\" xmlns=\"" + TYPESNS + "\">\n";
		final String post = "</xs:schema>";
		return pre + this.xsd.toString() + post;
	}

	StringBuilder getXSDStringBuilder() {
		return this.xsd;
	}

	/**
	 * Getter for InputVariables.
	 *
	 * @return InputVarName -> InputVarValue
	 */
	Map<String, String> getInputs() {
		return this.inputs;
	}

	/**
	 * Set the map for Input-Variables.
	 *
	 * @param inputs InputVarName -> InputVarValue
	 */
	public void setInputs(Map<String, String> inputs) {
		this.inputs = inputs;
	}

	private Definitions processServiceTemplate(ServiceTemplate yamlServiceTemplate) {
		final TServiceTemplate serviceTemplate = new TServiceTemplate();
		final TTopologyTemplate topologyTemplate = new TTopologyTemplate();
		setInitialProperties(yamlServiceTemplate, serviceTemplate, topologyTemplate);

		final ISubSwitch[] subSwitches = { new CapabilityTypesSubSwitch(this), new RelationshipTypesSubSwitch(this),
				new ArtifactTypesSubSwitch(this), new NodeTypesSubSwitch(this), new ImportsSubSwitch(this),
				new NodeTemplatesSubSwitch(this) };
		for (final ISubSwitch sSw : subSwitches) {
			sSw.process();
		}

		this.toscaResult.getImport().add(createTypeImport());

		return this.toscaResult;
	}

	private void setInitialProperties(ServiceTemplate yamlServiceTemplate, TServiceTemplate serviceTemplate,
			TTopologyTemplate topologyTemplate) {
		this.toscaResult.setId(unique("root"));
		this.toscaResult.setName(unique("Root"));
		if (yamlServiceTemplate.getTosca_default_namespace() != null && !yamlServiceTemplate.getTosca_default_namespace().isEmpty()) {
			this.toscaResult.setTargetNamespace(yamlServiceTemplate.getTosca_default_namespace());
		} else {
			this.toscaResult.setTargetNamespace(NS);
		}
		this.toscaResult.getServiceTemplateOrNodeTypeOrNodeTypeImplementation().add(serviceTemplate);
		this.toscaResult.getDocumentation().add(toDocumentation(yamlServiceTemplate.getDescription()));
		if (yamlServiceTemplate.getTemplate_author() != null && !yamlServiceTemplate.getTemplate_author().isEmpty()) {
			this.toscaResult.getDocumentation().add(toDocumentation("Template Author: " + yamlServiceTemplate.getTemplate_author()));
		}
		if (yamlServiceTemplate.getTemplate_version() != null && !yamlServiceTemplate.getTemplate_version().isEmpty()) {
			this.toscaResult.getDocumentation().add(toDocumentation("Template Version: " + yamlServiceTemplate.getTemplate_version()));
		}
		this.toscaResult.getOtherAttributes().put(new QName("xmlns:types"), TYPESNS);
		if (yamlServiceTemplate.getTemplate_name() != null && !yamlServiceTemplate.getTemplate_name().isEmpty()) {
			serviceTemplate.setId(unique(yamlServiceTemplate.getTemplate_name()));
			serviceTemplate.setName(yamlServiceTemplate.getTemplate_name());
		} else {
			serviceTemplate.setId(unique("servicetemplate"));
			serviceTemplate.setName("ServiceTemplate");
		}
		serviceTemplate.setTopologyTemplate(topologyTemplate);
	}

	private TImport createTypeImport() {
		final TImport result = new TImport();
		result.setImportType(XMLSCHEMA_NS);
		result.setLocation("types.xsd");
		result.setNamespace(TYPESNS);
		return result;
	}

	private TDocumentation toDocumentation(String desc) {
		final TDocumentation docu = new TDocumentation();
		docu.getContent().add(desc);
		return docu;
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
}
