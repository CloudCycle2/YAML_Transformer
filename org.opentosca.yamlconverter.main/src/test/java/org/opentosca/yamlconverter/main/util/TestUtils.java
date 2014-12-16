package org.opentosca.yamlconverter.main.util;

import java.io.File;
import java.io.IOException;
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
}
