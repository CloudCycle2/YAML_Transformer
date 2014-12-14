package org.opentosca.yamlconverter.main.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Sebi
 */
public class TestUtils {

    public String readYamlTestResource(String path) throws URISyntaxException, IOException {
        File simpleYaml = new File(getClass().getResource(path).toURI());
        return FileUtils.readFileToString(simpleYaml);
    }
}
