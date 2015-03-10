package org.opentosca.yamlconverter.main.utils;

import org.opentosca.yamlconverter.yamlmodel.yaml.element.ServiceTemplate;

import java.io.File;
import java.io.IOException;

public class CSARUtil {

	private static final String XSD_FILENAME = "types.xsd";
	private static final String XML_FILENAME = "Definitions.xml";
	private static final String DEFINITIONS_FOLDER = "Definitions";
	private static final String META_FILENAME = "TOSCA.meta";
	private static final String META_FOLDER = "TOSCA-Metadata";
	private static final String TOSCA_META_VERSION = "1.0";
	private static final String DEFAULT_TEMPLATE_VERSION = "1.0";
	private static final String DEFAULT_TEMPLATE_AUTHOR = "YAML";

	public static void createCSAR(ServiceTemplate st, String xml, String xsd, String tempfolder, String csarfilename) throws IOException {
		FileUtil.saveStringAsFile(DEFINITIONS_FOLDER + "/" + XSD_FILENAME, xsd);
		FileUtil.saveStringAsFile(DEFINITIONS_FOLDER + "/" + XML_FILENAME, xml);
		FileUtil.saveStringAsFile(META_FOLDER + "/" + META_FILENAME, generateMetaFileContent(st));
		final ZipUtils appZip = new ZipUtils();
		appZip.generateFileList(new File(META_FOLDER));
		appZip.generateFileList(new File(DEFINITIONS_FOLDER));
		appZip.zipIt(csarfilename);
		FileUtil.deleteDirectory(new File(DEFINITIONS_FOLDER));
		FileUtil.deleteDirectory(new File(META_FOLDER));
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