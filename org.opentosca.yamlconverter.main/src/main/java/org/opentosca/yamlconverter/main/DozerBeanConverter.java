package org.opentosca.yamlconverter.main;

import org.dozer.DozerBeanMapper;
import org.dozer.DozerBeanMapperSingletonWrapper;
import org.opentosca.model.tosca.TDefinitions;
import org.opentosca.model.tosca.TestRoot;
import org.opentosca.yamlconverter.main.interfaces.IToscaBean2BeanConverter;
import org.opentosca.yamlconverter.yamlmodel.YamlRootElement;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.YAMLElement;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * This Converter uses Dozer to convert between XML and YAML beans.
 *
 * @author Jonas Heinisch
 *
 */
public class DozerBeanConverter implements IToscaBean2BeanConverter {

	/**
	 * Name of the folder in which the dozer mapping xmls are stored.
	 */
	private static final String DOZERMAPPINGS_FOLDER = "dozermappings";

	/**
	 * The dozer mapper.
	 */
	private DozerBeanMapper mapper;

	/**
	 * Instantiates dozer mapper.
	 */
	public DozerBeanConverter() {
		List<String> myMappingFiles = getMappingfiles(DOZERMAPPINGS_FOLDER,
				true);
		mapper = (DozerBeanMapper) DozerBeanMapperSingletonWrapper.getInstance();
		mapper.setMappingFiles(myMappingFiles);
	}

	/**
	 * Crawls a folder and returns all filenames.
	 *
	 * @param foldername
	 *            The name of the folder that should be crawled.
	 * @param recursive
	 *            Should the folder be crawled recursive?
	 * @return A List of filenames.
	 */
	private List<String> getMappingfiles(String foldername, boolean recursive) {
		// TODO: not test
		List<String> myMappingFiles = new ArrayList<String>();
		File[] filesOfFolder = getFilesOfFolder(foldername);
		for (File f : filesOfFolder) {
			if (f.isFile() && f.getName().endsWith(".xml")) {
				myMappingFiles.add("file:" + foldername + "/" + f.getName());
			} else if (recursive && f.isDirectory()) {
				myMappingFiles.addAll(getMappingfiles(f.getPath(), true));
			}
		}
		return myMappingFiles;
	}

	private File[] getFilesOfFolder(String foldername) {
		File folder = null;
		try {
			folder = new File(getClass().getResource(foldername).toURI());
		} catch (URISyntaxException e) {
			folder = new File("");
		}
		File[] folderContent = folder.listFiles();
		if(folderContent == null) {
			folderContent = new File[0];
		}
		return folderContent;
	}

	@Override
	public TestRoot yamlb2xmlb(YamlRootElement yamlroot) {
		// TODO: not tested
		TestRoot test = mapper.map(yamlroot, TestRoot.class);
		return test;
	}

	public YAMLElement xmlb2yamlb(TDefinitions xmlroot) {
		// TODO: not tested
		return mapper.map(xmlroot, YAMLElement.class);
	}

	@Override
	public TDefinitions yamlb2xmlb(YAMLElement yamlroot) {
		// TODO: not tested
		return mapper.map(yamlroot, TDefinitions.class);
	}

}