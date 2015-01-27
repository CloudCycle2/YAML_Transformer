package org.opentosca.yamlconverter.main.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;

public class FileUtil {
	public String readYamlResource(String path) throws URISyntaxException, IOException {
		final File simpleYaml = new File(getClass().getResource(path).toURI());
		return FileUtils.readFileToString(simpleYaml);
	}

	public void saveStringAsFile(String filename, String content) throws IOException {
		FileUtils.writeStringToFile(new File(filename), content);
	}
}
