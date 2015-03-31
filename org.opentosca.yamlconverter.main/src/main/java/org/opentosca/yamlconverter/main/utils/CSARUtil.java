package org.opentosca.yamlconverter.main.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.ServiceTemplate;

public class CSARUtil {

	/**
	 * TOSCA specific constants, for XML and XSD documents
	 */
	private static final String TOSCA_FOLDER = "/tosca";
	public static final String TOSCA_BASE_TYPES_DIAGRAM = TOSCA_FOLDER + "/" + "TOSCA-v1.0-BaseTypes-Diagram.xml";
	public static final String TOSCA_SPECIFIC_TYPES_DIAGRAM = TOSCA_FOLDER + "/" + "TOSCA-v1.0-SpecificTypes-Diagram.xml";
	public static final String TOSCA_BASE_TYPE_FILENAME = "TOSCA-v1.0-BaseTypes-Definitions.xml";
	private static final String TOSCA_BASE_TYPES = TOSCA_FOLDER + "/" + TOSCA_BASE_TYPE_FILENAME;
	public static final String TOSCA_SPECIFIC_TYPE_FILENAME = "TOSCA-v1.0-SpecificTypes-Definitions.xml";
	private static final String TOSCA_SPECIFIC_TYPES = TOSCA_FOLDER + "/" + TOSCA_SPECIFIC_TYPE_FILENAME;
	private static final String TOSCA_TYPES_FOLDER = "types";
	private static final String TOSCA_BASE_TYPE_XSD_FILENAME = "TOSCA-v1.0-BaseTypes.xsd";
	public static final String TOSCA_BASE_TYPES_XSD_PATH = TOSCA_TYPES_FOLDER + "/" + TOSCA_BASE_TYPE_XSD_FILENAME;
	private static final String TOSCA_BASE_TYPES_XSD = TOSCA_FOLDER + "/" + TOSCA_BASE_TYPE_XSD_FILENAME;
	private static final String TOSCA_SPECIFIC_TYPES_XSD_FILENAME = "TOSCA-v1.0-SpecificTypes.xsd";
	public static final String TOSCA_SPECIFIC_TYPES_XSD_PATH = TOSCA_TYPES_FOLDER + "/" + TOSCA_SPECIFIC_TYPES_XSD_FILENAME;
	private static final String TOSCA_SPECIFIC_TYPES_XSD = TOSCA_FOLDER + "/" + TOSCA_SPECIFIC_TYPES_XSD_FILENAME;
	private static final String TOSCA_ARTIFACTS_XSD_FILENAME = "Artifacts.xsd";
	public static final String TOSCA_ARTIFACTS_XSD_PATH = TOSCA_TYPES_FOLDER + "/" + TOSCA_ARTIFACTS_XSD_FILENAME;
	private static final String TOSCA_ARTIFACTS_XSD = TOSCA_FOLDER + "/" + TOSCA_ARTIFACTS_XSD_FILENAME;

	public static final String TYPES_XSD_FILENAME = "types.xsd";
	public static final String DEFINITIONS_XML_FILENAME = "Definitions.xml";
	public static final String DEFINITIONS_FOLDER = "Definitions";
	public static final String DEF_BASE_TYPES = DEFINITIONS_FOLDER + "/" + TOSCA_BASE_TYPE_FILENAME;
	public static final String DEF_BASE_TYPES_DIAGRAM = DEFINITIONS_FOLDER + "/" + "TOSCA-v1.0-BaseTypes-Diagram.xml";
	public static final String DEF_SPECIFIC_TYPES = DEFINITIONS_FOLDER + "/" + TOSCA_SPECIFIC_TYPE_FILENAME;
	public static final String DEF_SPECIFIC_TYPES_DIAGRAM = DEFINITIONS_FOLDER + "/" + "TOSCA-v1.0-SpecificTypes-Diagram.xml";
	private static final String META_FILENAME = "TOSCA.meta";
	private static final String META_FOLDER = "TOSCA-Metadata";
	private static final String TOSCA_META_VERSION = "1.0";
	private static final String DEFAULT_TEMPLATE_VERSION = "1.0";
	private static final String DEFAULT_TEMPLATE_AUTHOR = "YAML";

	/**
	 * Creates a CSAR file based on a YAML {@link org.opentosca.yamlconverter.yamlmodel.yaml.element.ServiceTemplate}, a String containing
	 * the generated XML, a String containing the generated and corresponding XSD and a filename for that CSAR file. A CSAR file is simply a
	 * ZIP folder. The method creates and prepares three temporal folders: {@link #META_FOLDER}, {@link #DEFINITIONS_FOLDER} and
	 * {@link #TOSCA_TYPES_FOLDER} which contain different files. {@link #META_FOLDER} contains the CSAR meta file called
	 * {@link #META_FILENAME}. {@link #DEFINITIONS_FOLDER} contains the generated XML and two XML documents to provide the Base- and
	 * SpecificTypes for TOSCA XML. {@link #TOSCA_TYPES_FOLDER} contains the XSD files for the mentioned XML documents (exception: the XSD
	 * for the generated XML file can be found in {@link #DEFINITIONS_FOLDER}). <br />
	 * The CSAR file will be put to the current working directory, e.g. if you're using the program as a JAR, then the CSAR is put into the
	 * same folder where the JAR is. <br />
	 * After generating, the pre-mentioned temporal folders will be deleted!
	 *
	 * @param serviceTemplate Service template containing all YAML elements; will be used for the META file production only
	 * @param xml generated XML as String for {@code serviceTemplate}
	 * @param xsd generated XSD as String for {@code xml}
	 * @param csarFilename the name of the generated CSAR file
	 * @throws IOException
	 */
	public static void createCSAR(ServiceTemplate serviceTemplate, String xml, String xsd, String csarFilename) throws IOException {
		final ZipUtils appZip = new ZipUtils();
		final FileUtil fileUtil = new FileUtil();
		final File[] csarFolders = new File[] { new File(META_FOLDER), new File(DEFINITIONS_FOLDER), new File(TOSCA_TYPES_FOLDER) };
		prepareFoldersWithFiles(serviceTemplate, xml, xsd, fileUtil);

		// generate zip
		for (final File folder : csarFolders) {
			appZip.generateFileList(folder);
		}

		appZip.zipIt(csarFilename);

		// clean directory
		for (final File folder : csarFolders) {
			FileUtil.deleteDirectory(folder);
		}
	}

	private static void prepareFoldersWithFiles(final ServiceTemplate serviceTemplate, final String xml, final String xsd,
			final FileUtil fileUtil) throws IOException {
		// prepare folders
		FileUtil.saveStringAsFile(DEFINITIONS_FOLDER + "/" + TYPES_XSD_FILENAME, xsd);
		FileUtil.saveStringAsFile(DEFINITIONS_FOLDER + "/" + DEFINITIONS_XML_FILENAME, xml);
		FileUtil.saveStringAsFile(META_FOLDER + "/" + META_FILENAME, generateMetaFileContent(serviceTemplate));
		FileUtils.copyInputStreamToFile(fileUtil.getResourceAsStream(TOSCA_BASE_TYPES), new File(DEF_BASE_TYPES));
		FileUtils.copyInputStreamToFile(fileUtil.getResourceAsStream(TOSCA_BASE_TYPES_DIAGRAM), new File(DEF_BASE_TYPES_DIAGRAM));
		FileUtils.copyInputStreamToFile(fileUtil.getResourceAsStream(TOSCA_SPECIFIC_TYPES), new File(DEF_SPECIFIC_TYPES));
		FileUtils.copyInputStreamToFile(fileUtil.getResourceAsStream(TOSCA_SPECIFIC_TYPES_DIAGRAM), new File(DEF_SPECIFIC_TYPES_DIAGRAM));
		FileUtils.copyInputStreamToFile(fileUtil.getResourceAsStream(TOSCA_BASE_TYPES_XSD), new File(TOSCA_BASE_TYPES_XSD_PATH));
		FileUtils.copyInputStreamToFile(fileUtil.getResourceAsStream(TOSCA_SPECIFIC_TYPES_XSD), new File(TOSCA_SPECIFIC_TYPES_XSD_PATH));
		FileUtils.copyInputStreamToFile(fileUtil.getResourceAsStream(TOSCA_ARTIFACTS_XSD), new File(TOSCA_ARTIFACTS_XSD_PATH));
		fileUtil.copyDirectory("/icons");
		fileUtil.copyDirectory("/scripts");
	}

	private static String generateMetaFileContent(ServiceTemplate st) {
		final StringBuilder result = new StringBuilder();
		result.append("TOSCA-Meta-Version: " + TOSCA_META_VERSION + "\n");
		result.append("CSAR-Version: " + DEFAULT_TEMPLATE_VERSION + "\n");
		if (st.getTemplate_author() != null && !st.getTemplate_author().equals("")) {
			result.append("Created-By: " + st.getTemplate_author() + "\n");
		} else {
			result.append("Created-By: " + DEFAULT_TEMPLATE_AUTHOR + "\n");
		}
		result.append("Entry-Definitions: " + DEFINITIONS_FOLDER + "/" + DEFINITIONS_XML_FILENAME + "\n");
		result.append("Name: ").append(DEF_BASE_TYPES).append("\n").append("Content-Type: application/vnd.oasis.tosca.definitions\n")
		.append("Diagram: ").append(DEF_BASE_TYPES_DIAGRAM).append("\n").append("\n").append("Name: ").append(DEF_SPECIFIC_TYPES)
				.append("\n").append("Content-Type: application/vnd.oasis.tosca.definitions\n").append("Diagram: ")
				.append(DEF_SPECIFIC_TYPES_DIAGRAM);
		return result.toString();
	}
}