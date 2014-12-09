package de.opentosca.yamlconverter.main;

import org.opentosca.model.tosca.TDefinitions;

import de.opentosca.yamlconverter.main.interfaces.ItoscaBean2BeanConverter;
import de.opentosca.yamlconverter.main.interfaces.ItoscaXML2XMLbeanConverter;
import de.opentosca.yamlconverter.main.interfaces.ItoscaYAML2XMLConverter;
import de.opentosca.yamlconverter.main.interfaces.ItoscaYAML2YAMLbeanConverter;

/**
 * This Converter can convert Tosca YAML to Tosca XML (bi-directional) by the
 * use of other converters.
 * 
 * @author Jonas Heinisch
 *
 */
public class ToscaYAML2XMLConverter implements ItoscaYAML2XMLConverter {
	private ItoscaYAML2YAMLbeanConverter y2yb = new YamlBeansConverter();
	private ItoscaBean2BeanConverter b2b = new DozerBeanConverter();
	private ItoscaXML2XMLbeanConverter x2xb = new JAXBConverter();

	@Override
	public String yaml2xml(String yamlstring) {
		YamlRootElement yroot = y2yb.yaml2yamlbean(yamlstring);
		TDefinitions xroot = b2b.yamlb2xmlb(yroot);
		return x2xb.xmlbean2xml(xroot);
	}

	@Override
	public String xml2yaml(String xmlstring) {
		TDefinitions xroot = x2xb.xml2xmlbean(xmlstring);
		YamlRootElement yroot = b2b.xmlb2yamlb(xroot);
		return y2yb.yamlbean2yaml(yroot);
	}

}
