package org.opentosca.yamlconverter.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.opentosca.model.tosca.TDefinitions;
import org.opentosca.model.yaml.YamlRootElement;
import org.opentosca.yamlconverter.main.interfaces.ItoscaBean2BeanConverter;

/**
 * This Converter uses Dozer to convert between XML and YAML beans.
 * 
 * @author Jonas Heinisch
 *
 */
public class DozerBeanConverter implements ItoscaBean2BeanConverter {
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
		mapper = new DozerBeanMapper();
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
		File folder = new File(foldername);
		for (File f : folder.listFiles()) {
			if (f.isFile()) {
				myMappingFiles.add(foldername + "/" + f.getName());
			} else if (recursive && f.isDirectory()) {
				myMappingFiles.addAll(getMappingfiles(f.getPath(), true));
			}
		}
		return myMappingFiles;
	}

	@Override
	public TDefinitions yamlb2xmlb(YamlRootElement yamlroot) {
		// TODO: not tested
		return mapper.map(yamlroot, TDefinitions.class);
	}

	@Override
	public YamlRootElement xmlb2yamlb(TDefinitions xmlroot) {
		// TODO: not tested
		return mapper.map(xmlroot, YamlRootElement.class);
	}

}
