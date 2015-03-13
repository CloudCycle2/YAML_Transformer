package org.opentosca.yamlconverter.main.utils;

import org.apache.commons.io.FileUtils;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.ServiceTemplate;

import java.io.File;
import java.io.IOException;

public class CSARUtil {

	private static final String TOSCA_FOLDER = "/tosca";
	private static final String TOSCA_BASE_TYPE_FILENAME = "TOSCA-v1.0-BaseTypes-Definitions.xml";
	private static final String TOSCA_BASE_TYPES = TOSCA_FOLDER + "/" + TOSCA_BASE_TYPE_FILENAME;
	private static final String TOSCA_SPECIFIC_TYPE_FILENAME = "TOSCA-v1.0-SpecificTypes-Definitions.xml";
	private static final String TOSCA_SPECIFIC_TYPES = TOSCA_FOLDER + "/" + TOSCA_SPECIFIC_TYPE_FILENAME;
	private static final String TOSCA_TYPES_FOLDER = "types";
	private static final String TOSCA_BASE_TYPE_XSD_FILENAME = "TOSCA-v1.0-BaseTypes.xsd";
	private static final String TOSCA_BASE_TYPES_XSD = TOSCA_FOLDER + "/" + TOSCA_BASE_TYPE_XSD_FILENAME;
	private static final String TOSCA_SPECIFIC_TYPES_XSD_FILENAME = "TOSCA-v1.0-SpecificTypes.xsd";
	private static final String TOSCA_SPECIFIC_TYPES_XSD = TOSCA_FOLDER + "/" + TOSCA_SPECIFIC_TYPES_XSD_FILENAME;
	private static final String TOSCA_ARTIFACTS_XSD_FILENAME = "Artifacts.xsd";
	private static final String TOSCA_ARTIFACTS_XSD = TOSCA_FOLDER + "/" + TOSCA_ARTIFACTS_XSD_FILENAME;

	private static final String XSD_FILENAME = "types.xsd";
	private static final String XML_FILENAME = "Definitions.xml";
	private static final String DEFINITIONS_FOLDER = "Definitions";
	private static final String META_FILENAME = "TOSCA.meta";
	private static final String META_FOLDER = "TOSCA-Metadata";
	private static final String TOSCA_META_VERSION = "1.0";
	private static final String DEFAULT_TEMPLATE_VERSION = "1.0";
	private static final String DEFAULT_TEMPLATE_AUTHOR = "YAML";

	public static void createCSAR(ServiceTemplate st, String xml, String xsd, String csarfilename) throws IOException {
		final ZipUtils appZip = new ZipUtils();
		final FileUtil fileUtil = new FileUtil();
		final File metaFolder = new File(META_FOLDER);
		final File definitionsFolder = new File(DEFINITIONS_FOLDER);
		final File typesFolder = new File(TOSCA_TYPES_FOLDER);
		prepareFoldersWithFiles(st, xml, xsd, fileUtil);
		// generate zip
		appZip.generateFileList(metaFolder);
		appZip.generateFileList(definitionsFolder);
		appZip.generateFileList(typesFolder);
		appZip.zipIt(csarfilename);
		// clean directory
		FileUtil.deleteDirectory(definitionsFolder);
		FileUtil.deleteDirectory(metaFolder);
		FileUtil.deleteDirectory(typesFolder);
	}

	private static void prepareFoldersWithFiles(final ServiceTemplate st, final String xml, final String xsd, final FileUtil fileUtil) throws IOException {
		// prepare folders
		FileUtil.saveStringAsFile(DEFINITIONS_FOLDER + "/" + XSD_FILENAME, xsd);
		FileUtil.saveStringAsFile(DEFINITIONS_FOLDER + "/" + XML_FILENAME, xml);
		FileUtil.saveStringAsFile(META_FOLDER + "/" + META_FILENAME, generateMetaFileContent(st));
		FileUtils.copyInputStreamToFile(fileUtil.getResourceAsStream(TOSCA_BASE_TYPES),
				new File(DEFINITIONS_FOLDER + "/" + TOSCA_BASE_TYPE_FILENAME));
		FileUtils.copyInputStreamToFile(fileUtil.getResourceAsStream(TOSCA_SPECIFIC_TYPES),
				new File(DEFINITIONS_FOLDER + "/" + TOSCA_SPECIFIC_TYPE_FILENAME));
		FileUtils.copyInputStreamToFile(fileUtil.getResourceAsStream(TOSCA_BASE_TYPES_XSD),
				new File(TOSCA_TYPES_FOLDER + "/" + TOSCA_BASE_TYPE_XSD_FILENAME));
		FileUtils.copyInputStreamToFile(fileUtil.getResourceAsStream(TOSCA_SPECIFIC_TYPES_XSD),
				new File(TOSCA_TYPES_FOLDER + "/" + TOSCA_SPECIFIC_TYPES_XSD_FILENAME));
		FileUtils.copyInputStreamToFile(fileUtil.getResourceAsStream(TOSCA_ARTIFACTS_XSD),
				new File(TOSCA_TYPES_FOLDER + "/" + TOSCA_ARTIFACTS_XSD_FILENAME));
	}

	private static String generateMetaFileContent(ServiceTemplate st) {
		final StringBuilder result = new StringBuilder();
		result.append("TOSCA-Meta-Version: " + TOSCA_META_VERSION + "\n");
		if (st.getTemplate_version() != null && !st.getTemplate_version().equals("")) {
			result.append("CSAR-Version: " + st.getTemplate_version() + "\n");
		} else {
			result.append("CSAR-Version: " + DEFAULT_TEMPLATE_VERSION + "\n");
		}
		if (st.getTemplate_author() != null && !st.getTemplate_author().equals("")) {
			result.append("Created-By: " + st.getTemplate_author() + "\n");
		} else {
			result.append("Created-By: " + DEFAULT_TEMPLATE_AUTHOR + "\n");
		}
		result.append("Entry-Definitions: " + DEFINITIONS_FOLDER + "/" + XML_FILENAME + "\n");
		return result.toString();
	}
}