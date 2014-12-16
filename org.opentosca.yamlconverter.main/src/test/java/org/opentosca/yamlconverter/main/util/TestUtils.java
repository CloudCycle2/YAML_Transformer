package org.opentosca.yamlconverter.main.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;

/**
 * @author Sebi
 */
public class TestUtils {

	public String readYamlTestResource(String path) throws URISyntaxException, IOException {
		final File simpleYaml = new File(getClass().getResource(path).toURI());
		return FileUtils.readFileToString(simpleYaml);
	}

	/**
	 * Checks whether the given yaml string and the yaml content in the given path are equal. The yaml content is stripped of comments.
	 * 
	 * @param yaml
	 * @param path
	 * @return
	 */
	public boolean yamlFilesEqual(String yaml, String path) {
		try (final BufferedReader bis = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(path), "UTF-8"))) {
			final StringBuffer yaml2 = new StringBuffer();
			String line = "";
			while ((line = bis.readLine()) != null) {
				if (!line.trim().startsWith("#")) {
					yaml2.append(line);
				}
			}
			return yaml2.toString().equals(yaml2);
		} catch (final IOException e) {
			return false;
		}

	}
}
