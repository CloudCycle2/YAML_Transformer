package org.opentosca.yamlconverter.main;

import org.opentosca.model.tosca.Definitions;
import org.opentosca.yamlconverter.main.interfaces.IToscaBean2BeanConverter;
import org.opentosca.yamlconverter.switchmapper.Yaml2XmlSwitch;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.ServiceTemplate;

import java.util.Map;

/**
 *
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

	public String getXSD() {
		return this.switchmapper.getXSD();
	}

	public void setInputs(Map<String, String> inputs) {
		this.switchmapper.setInputs(inputs);
	}

}
