package org.opentosca.yamlconverter.main.utils;

import java.io.File;
import java.io.IOException;

import org.opentosca.yamlconverter.yamlmodel.yaml.element.ServiceTemplate;

public class CSARUtil {

	private static final String XSD_FILENAME = "types.xsd";
	private static final String XML_FILENAME = "Definitions.xml";
	private static final String DEFINITIONS_FOLDER = "Definitions";
	private static final String META_FILENAME = "TOSCA.meta";
	private static final String META_FOLDER = "TOSCA-Metadata";
	private static final String TOSCA_META_VERSION = "1.0";

	public static void createCSAR(ServiceTemplate st, String xml, String xsd, String tempfolder, String csarfilename) throws IOException {
		FileUtil.saveStringAsFile(tempfolder + "/" + DEFINITIONS_FOLDER + "/" + XSD_FILENAME, xsd);
		FileUtil.saveStringAsFile(tempfolder + "/" + DEFINITIONS_FOLDER + "/" + XML_FILENAME, xml);
		FileUtil.saveStringAsFile(tempfolder + "/" + META_FOLDER + "/" + META_FILENAME, generateMetaFileContent(st));
		final ZipUtils appZip = new ZipUtils(tempfolder);
		appZip.generateFileList(new File(tempfolder + "/" + META_FOLDER));
		appZip.generateFileList(new File(tempfolder + "/" + DEFINITIONS_FOLDER));
		appZip.zipIt(csarfilename);
		FileUtil.deleteDirectory(new File(tempfolder));
	}

	private static String generateMetaFileContent(ServiceTemplate st) {
		final StringBuilder result = new StringBuilder();
		result.append("TOSCA-Meta-Version: " + TOSCA_META_VERSION + "\n");
		result.append("CSAR-Version: " + st.getTemplate_version() + "\n");
		result.append("Created-By: " + st.getTemplate_author() + "\n");
		result.append("Entry-Definitions: " + DEFINITIONS_FOLDER + "/" + XML_FILENAME + "\n");
		return result.toString();
	}
}