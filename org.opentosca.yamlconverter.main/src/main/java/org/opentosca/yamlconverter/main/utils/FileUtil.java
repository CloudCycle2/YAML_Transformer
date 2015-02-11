package org.opentosca.yamlconverter.main.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;

public class FileUtil {
	/**
	 * Read file from path and return String.
	 * 
	 * @param path filepath
	 * @return the file content
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public String readYamlResource(String path) throws URISyntaxException, IOException {
		final File simpleYaml = new File(getClass().getResource(path).toURI());
		return FileUtils.readFileToString(simpleYaml);
	}

	/**
	 * Saves a String to a file.
	 * 
	 * @param filename The filename.
	 * @param content Content to write to file.
	 * @throws IOException
	 */
	public void saveStringAsFile(String filename, String content) throws IOException {
		FileUtils.writeStringToFile(new File(filename), content);
	}
}
