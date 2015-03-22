package org.opentosca.yamlconverter.switchmapper;

import org.opentosca.model.tosca.*;
import org.opentosca.yamlconverter.main.utils.CSARUtil;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.ServiceTemplate;

import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Map;

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
	public static final String TYPES_NS = "http://www.example.org/tosca/yamlgen/types";

	public static final String TOSCA_IMPORT_TYPE = "http://docs.oasis-open.org/tosca/ns/2011/12";

	public static final String SPECIFIC_TYPES_NS = "http://docs.oasis-open.org/tosca/ns/2011/12/ToscaSpecificTypes";

	public static final String BASE_TYPES_NS = "http://docs.oasis-open.org/tosca/ns/2011/12/ToscaBaseTypes";

	public static final String TOSCA_NS_PREFIX = "tosca";

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

	private String usedNamespace = NS;

	Definitions getToscaResult() {
		return this.toscaResult;
	}

	ServiceTemplate getServiceTemplate() {
		return this.st;
	}

	String getUsedNamespace() {
		return this.usedNamespace;
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
		final String pre = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" "
				+ "targetNamespace=\"" + TYPES_NS + "\" xmlns=\"" + TYPES_NS + "\">\n";
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

		this.toscaResult.getImport().add(createTypeImport(XMLSCHEMA_NS,
				CSARUtil.DEFINITIONS_FOLDER + "/" + CSARUtil.TYPES_XSD_FILENAME, TYPES_NS));
		// only add specific types, base types are imported within specific types XML document
		this.toscaResult.getImport().add(createTypeImport(TOSCA_IMPORT_TYPE,
				CSARUtil.DEFINITIONS_FOLDER + "/" + CSARUtil.TOSCA_SPECIFIC_TYPE_FILENAME, SPECIFIC_TYPES_NS));
		return this.toscaResult;
	}

	private void setInitialProperties(ServiceTemplate yamlServiceTemplate, TServiceTemplate serviceTemplate,
			TTopologyTemplate topologyTemplate) {
		this.toscaResult.setId(unique("root"));
		this.toscaResult.setName(unique("Root"));

		// set namespaces
		if (yamlServiceTemplate.getTosca_default_namespace() != null && !yamlServiceTemplate.getTosca_default_namespace().isEmpty()) {
			this.usedNamespace = yamlServiceTemplate.getTosca_default_namespace();
		}
		this.toscaResult.setTargetNamespace(this.usedNamespace);
		this.toscaResult.getOtherAttributes().put(new QName("xmlns:ns1"), BASE_TYPES_NS);
		this.toscaResult.getOtherAttributes().put(new QName("xmlns:ns2"), SPECIFIC_TYPES_NS);
		this.toscaResult.getOtherAttributes().put(new QName("xmlns:types"), TYPES_NS);

		setServiceAndTopologyTemplate(yamlServiceTemplate, serviceTemplate, topologyTemplate);
	}

	private void setServiceAndTopologyTemplate(final ServiceTemplate yamlServiceTemplate, final TServiceTemplate serviceTemplate,
											   final TTopologyTemplate topologyTemplate) {
		// set service and topology template
		this.toscaResult.getServiceTemplateOrNodeTypeOrNodeTypeImplementation().add(serviceTemplate);
		this.toscaResult.getDocumentation().add(toDocumentation(yamlServiceTemplate.getDescription()));
		if (yamlServiceTemplate.getTemplate_author() != null && !yamlServiceTemplate.getTemplate_author().isEmpty()) {
			this.toscaResult.getDocumentation().add(toDocumentation("Template Author: " + yamlServiceTemplate.getTemplate_author()));
		}
		if (yamlServiceTemplate.getTemplate_version() != null && !yamlServiceTemplate.getTemplate_version().isEmpty()) {
			this.toscaResult.getDocumentation().add(toDocumentation("Template Version: " + yamlServiceTemplate.getTemplate_version()));
		}
		if (yamlServiceTemplate.getTemplate_name() != null && !yamlServiceTemplate.getTemplate_name().isEmpty()) {
			serviceTemplate.setId(unique(yamlServiceTemplate.getTemplate_name()));
			serviceTemplate.setName(yamlServiceTemplate.getTemplate_name());
		} else {
			serviceTemplate.setId(unique("servicetemplate"));
			serviceTemplate.setName("ServiceTemplate");
		}
		serviceTemplate.setTopologyTemplate(topologyTemplate);
		serviceTemplate.setTargetNamespace(this.usedNamespace);
	}

	private TImport createTypeImport(final String importType, final String location, final String namespace) {
		final TImport result = new TImport();
		result.setImportType(importType);
		result.setLocation(location);
		result.setNamespace(namespace);
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
