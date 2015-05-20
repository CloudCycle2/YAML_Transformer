package org.opentosca.yamlconverter.main;

import java.util.Map;

import org.opentosca.model.tosca.Definitions;
import org.opentosca.yamlconverter.main.interfaces.IToscaBean2BeanConverter;
import org.opentosca.yamlconverter.switchmapper.Yaml2XmlSwitch;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.ServiceTemplate;

/**
 * This class implements methods to convert YAML beans to XML beans. Currently XML beans to YAML beans is not supported.
 */
public class SwitchMapperConverter implements IToscaBean2BeanConverter {

	private final Yaml2XmlSwitch switchmapper = new Yaml2XmlSwitch();

	@Override
	public ServiceTemplate convertToYamlBean(Definitions xmlRoot) {
		throw new UnsupportedOperationException("Not possible with this Mapper.");
	}

	@Override
	public Definitions convertToXmlBean(ServiceTemplate yamlBean) {
		return this.switchmapper.parse(yamlBean);
	}

	/**
	 * Returns an additional XSD to support the properties of templates.
	 *
	 * @return additional XSD
	 */
	public String getXSD() {
		return this.switchmapper.getXSD();
	}

	/**
	 * Set the map for Input-Variables.
	 * 
	 * @param inputs InputVarName -> InputVarValue
	 */
	public void setInputs(Map<String, String> inputs) {
		this.switchmapper.setInputs(inputs);
	}

}
