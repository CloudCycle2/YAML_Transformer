package org.opentosca.yamlconverter.switchmapper;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.opentosca.model.tosca.Definitions;
import org.opentosca.model.tosca.TDocumentation;
import org.opentosca.model.tosca.TImport;
import org.opentosca.model.tosca.TServiceTemplate;
import org.opentosca.model.tosca.TTopologyTemplate;
import org.opentosca.yamlconverter.main.utils.CSARUtil;
import org.opentosca.yamlconverter.switchmapper.subswitches.ArtifactTypesSubSwitch;
import org.opentosca.yamlconverter.switchmapper.subswitches.CapabilityTypesSubSwitch;
import org.opentosca.yamlconverter.switchmapper.subswitches.ImportsSubSwitch;
import org.opentosca.yamlconverter.switchmapper.subswitches.NodeTemplatesSubSwitch;
import org.opentosca.yamlconverter.switchmapper.subswitches.NodeTypesSubSwitch;
import org.opentosca.yamlconverter.switchmapper.subswitches.RelationshipTypesSubSwitch;
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
	public static final String TYPES_NS = "http://www.example.org/tosca/yamlgen/types";

	public static final String TOSCA_IMPORT_TYPE = "http://docs.oasis-open.org/tosca/ns/2011/12";

	public static final String SPECIFIC_TYPES_NS = "http://docs.oasis-open.org/tosca/ns/2011/12/ToscaSpecificTypes";

	public static final String BASE_TYPES_NS = "http://docs.oasis-open.org/tosca/ns/2011/12/ToscaBaseTypes";

	public static final String TOSCA_NS_PREFIX = "tosca";
	public static final String BASE_TYPES_PREFIX = "ns1";
	public static final String SPECIFIC_TYPES_PREFIX = "ns2";

	public static final String TARGET_NS_PREFIX = "target";

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

	public Definitions getToscaResult() {
		return this.toscaResult;
	}

	public ServiceTemplate getServiceTemplate() {
		return this.st;
	}

	public String getUsedNamespace() {
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

	public StringBuilder getXSDStringBuilder() {
		return this.xsd;
	}

	/**
	 * Getter for InputVariables.
	 *
	 * @return InputVarName -> InputVarValue
	 */
	public Map<String, String> getInputs() {
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

	/**
	 * Processes the given YAML service template by creating the corresponding Tosca T* objects like
	 * {@link org.opentosca.model.tosca.TServiceTemplate} and {@link org.opentosca.model.tosca.TTopologyTemplate}, setting initial
	 * properties and adding the objects to {@link #toscaResult}. Also the following known sub-switches are started:
	 * {@link org.opentosca.yamlconverter.switchmapper.subswitches.CapabilityTypesSubSwitch},
	 * {@link org.opentosca.yamlconverter.switchmapper.subswitches.RelationshipTypesSubSwitch},
	 * {@link org.opentosca.yamlconverter.switchmapper.subswitches.ArtifactTypesSubSwitch},
	 * {@link org.opentosca.yamlconverter.switchmapper.subswitches.NodeTypesSubSwitch},
	 * {@link org.opentosca.yamlconverter.switchmapper.subswitches.ImportsSubSwitch},
	 * {@link org.opentosca.yamlconverter.switchmapper.subswitches.NodeTemplatesSubSwitch}. They process the YAML service template in their
	 * own way.
	 *
	 * @param yamlServiceTemplate YAML service template (read from a YAML file)
	 * @return XML representation as Tosca Definitions object from YAML service template
	 */
	private Definitions processServiceTemplate(ServiceTemplate yamlServiceTemplate) {
		final TServiceTemplate serviceTemplate = new TServiceTemplate();
		final TTopologyTemplate topologyTemplate = new TTopologyTemplate();
		setInitialProperties(yamlServiceTemplate, serviceTemplate, topologyTemplate);

		final ISubSwitch[] subSwitches = { new CapabilityTypesSubSwitch(this), new RelationshipTypesSubSwitch(this),
				new ArtifactTypesSubSwitch(this), new NodeTypesSubSwitch(this), new ImportsSubSwitch(this),
				new NodeTemplatesSubSwitch(this) };
		for (final ISubSwitch subSwitch : subSwitches) {
			subSwitch.process();
		}

		this.toscaResult.getImport().add(
				createTypeImport(XMLSCHEMA_NS, CSARUtil.DEFINITIONS_FOLDER + "/" + CSARUtil.TYPES_XSD_FILENAME, TYPES_NS));
		// only add specific types, base types are imported within specific types XML document
		this.toscaResult.getImport().add(
				createTypeImport(TOSCA_IMPORT_TYPE, CSARUtil.DEFINITIONS_FOLDER + "/" + CSARUtil.TOSCA_SPECIFIC_TYPE_FILENAME,
						SPECIFIC_TYPES_NS));
		return this.toscaResult;
	}

	/**
	 * Sets initial properties for {@code serviceTemplate} and {@code topologyTemplate} like namespace attributes, id and name.
	 *
	 * @param yamlServiceTemplate imported YAML service template
	 * @param serviceTemplate Tosca service template
	 * @param topologyTemplate Tosca topology template
	 */
	private void setInitialProperties(ServiceTemplate yamlServiceTemplate, TServiceTemplate serviceTemplate,
			TTopologyTemplate topologyTemplate) {
		this.toscaResult.setId(unique("root"));
		this.toscaResult.setName(unique("Root"));

		// set namespaces
		if (yamlServiceTemplate.getTosca_default_namespace() != null && !yamlServiceTemplate.getTosca_default_namespace().isEmpty()) {
			this.usedNamespace = yamlServiceTemplate.getTosca_default_namespace();
		}
		this.toscaResult.setTargetNamespace(this.usedNamespace);
		this.toscaResult.getOtherAttributes().put(new QName("xmlns:" + TARGET_NS_PREFIX), this.usedNamespace);
		this.toscaResult.getOtherAttributes().put(new QName("xmlns:" + BASE_TYPES_PREFIX), BASE_TYPES_NS);
		this.toscaResult.getOtherAttributes().put(new QName("xmlns:" + SPECIFIC_TYPES_PREFIX), SPECIFIC_TYPES_NS);
		this.toscaResult.getOtherAttributes().put(new QName("xmlns:types"), TYPES_NS);

		setServiceAndTopologyTemplate(yamlServiceTemplate, serviceTemplate, topologyTemplate);
	}

	/**
	 * Adds some documentation, attributes like id and name, connects topology template to service template and adds service template to
	 * {@link #toscaResult}.
	 *
	 * @param yamlServiceTemplate YAML service template
	 * @param serviceTemplate Tosca service template
	 * @param topologyTemplate Tosca topology template
	 */
	private void setServiceAndTopologyTemplate(final ServiceTemplate yamlServiceTemplate, final TServiceTemplate serviceTemplate,
			final TTopologyTemplate topologyTemplate) {
		// set service and topology template
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
		this.toscaResult.getServiceTemplateOrNodeTypeOrNodeTypeImplementation().add(serviceTemplate);
	}

	/**
	 * Creates a {@link org.opentosca.model.tosca.TImport} object containing a reference to another xml/xsd document.
	 *
	 * @param importType type of file to import
	 * @param location where the file is (relative path to the xml output file)
	 * @param namespace used namespace of referenced file
	 * @return import object containing the parameters
	 */
	private TImport createTypeImport(final String importType, final String location, final String namespace) {
		final TImport result = new TImport();
		result.setImportType(importType);
		result.setLocation(location);
		result.setNamespace(namespace);
		return result;
	}

	/**
	 * Creates a {@link org.opentosca.model.tosca.TDocumentation} object containing {@code desc} as content.
	 *
	 * @param desc any description
	 * @return documentation object containing the parameter
	 */
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
