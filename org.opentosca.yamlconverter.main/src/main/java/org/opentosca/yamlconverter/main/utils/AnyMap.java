package org.opentosca.yamlconverter.main.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

@XmlRootElement(name = "Properties")
public class AnyMap extends HashMap<String, String> {

	private static final long serialVersionUID = 1L;
	@XmlAnyElement
	public List<JAXBElement<String>> entries = new ArrayList<JAXBElement<String>>();

	public AnyMap() { // JAXB required
	}

	public AnyMap(Map<String, String> map) {
		for (final Map.Entry<String, String> entry : map.entrySet()) {
			this.entries.add(new JAXBElement<String>(new QName(entry.getKey()), String.class, entry.getValue()));
		}
	}

}
