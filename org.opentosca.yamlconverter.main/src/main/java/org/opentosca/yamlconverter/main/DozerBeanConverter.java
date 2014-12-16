package org.opentosca.yamlconverter.main;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.DozerBeanMapperSingletonWrapper;
import org.opentosca.model.tosca.TDefinitions;
import org.opentosca.model.tosca.TestRoot;
import org.opentosca.yamlconverter.main.interfaces.IToscaBean2BeanConverter;
import org.opentosca.yamlconverter.yamlmodel.YamlRootElement;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.YAMLElement;

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
	private final DozerBeanMapper mapper;

	/**
	 * Instantiates dozer mapper.
	 */
	public DozerBeanConverter() {
		final List<String> myMappingFiles = getMappingfiles(DOZERMAPPINGS_FOLDER, true);
		this.mapper = (DozerBeanMapper) DozerBeanMapperSingletonWrapper.getInstance();
		this.mapper.setMappingFiles(myMappingFiles);
	}

	/**
	 * Crawls a folder and returns all filenames.
	 *
	 * @param foldername The name of the folder that should be crawled.
	 * @param recursive Should the folder be crawled recursive?
	 * @return A List of filenames.
	 */
	private List<String> getMappingfiles(String foldername, boolean recursive) {
		// TODO: not test
		final List<String> myMappingFiles = new ArrayList<String>();
		final File[] filesOfFolder = getFilesOfFolder(foldername);
		for (final File f : filesOfFolder) {
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
		} catch (final URISyntaxException e) {
			folder = new File("");
		}
		File[] folderContent = folder.listFiles();
		if (folderContent == null) {
			folderContent = new File[0];
		}
		return folderContent;
	}

	@Override
	public TestRoot yamlb2xmlb(YamlRootElement yamlroot) {
		// TODO: not tested
		final TestRoot test = this.mapper.map(yamlroot, TestRoot.class);
		return test;
	}

	@Override
	public YAMLElement xmlb2yamlb(TDefinitions xmlroot) {
		// TODO: not tested
		return this.mapper.map(xmlroot, YAMLElement.class);
	}

	@Override
	public TDefinitions yamlb2xmlb(YAMLElement yamlroot) {
		// TODO: not tested
		return this.mapper.map(yamlroot, TDefinitions.class);
	}

}