package org.opentosca.yamlconverter.main;

import org.opentosca.model.tosca.TDefinitions;
import org.opentosca.model.tosca.TestRoot;
import org.opentosca.yamlconverter.main.exceptions.ConverterException;
import org.opentosca.yamlconverter.main.interfaces.IToscaBean2BeanConverterNew;
import org.opentosca.yamlconverter.main.interfaces.IToscaXml2XmlBeanConverterNew;
import org.opentosca.yamlconverter.main.interfaces.IToscaYaml2XmlConverterNew;
import org.opentosca.yamlconverter.main.interfaces.IToscaYaml2YamlBeanConverterNew;
import org.opentosca.yamlconverter.yamlmodel.YamlRootElement;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.YAMLElement;

/**
 * This Converter can convert Tosca YAML to Tosca XML (bi-directional) by the
 * use of other converters.
 * 
 * @author Jonas Heinisch
 *
 */
public class ToscaYaml2XmlConverterNew implements IToscaYaml2XmlConverterNew {
	private IToscaYaml2YamlBeanConverterNew y2yb = new YamlBeansConverterNew();
	private IToscaBean2BeanConverterNew b2b = new DozerBeanConverterNew();
	private IToscaXml2XmlBeanConverterNew x2xb = new JAXBConverterNew();

	@Override
	public String yaml2xml(String yamlstring) throws ConverterException {
		//YamlRootElement yroot = y2yb.yaml2yamlbean(yamlstring);
		//TDefinitions xroot = b2b.yamlb2xmlb(yroot);
		//return x2xb.xmlbean2xml(xroot);
		
		YamlRootElement yamlroot = new YamlRootElement();
		yamlroot.description = "TestDescription";
		
		TestRoot testRoot = b2b.yamlb2xmlb(yamlroot);
		return testRoot.description;
	}

	@Override
	public String xml2yaml(String xmlstring) throws ConverterException {
		TDefinitions xroot = x2xb.xml2xmlbean(xmlstring);
		YAMLElement yroot = b2b.xmlb2yamlb(xroot);
		return y2yb.yamlbean2yaml(yroot);
	}

}
