package org.opentosca.yamlconverter.main;

import java.util.Map;

import org.opentosca.model.tosca.Definitions;
import org.opentosca.yamlconverter.main.interfaces.IToscaBean2BeanConverter;
import org.opentosca.yamlconverter.switchmapper.Yaml2XmlSwitch;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.ServiceTemplate;

public class SwitchMapperConverter implements IToscaBean2BeanConverter {
	private final Yaml2XmlSwitch switchmapper = new Yaml2XmlSwitch();

	@Override
	public ServiceTemplate xmlb2yamlb(Definitions xmlroot) {
		throw new UnsupportedOperationException("Not possible with this Mapper.");
	}

	@Override
	public Definitions yamlb2xmlb(ServiceTemplate yamlBean) {
		return this.switchmapper.parse(yamlBean);
	}

	public Map<String, String> getInputRequirements() {
		return this.switchmapper.getInputRequirements();
	}

}
