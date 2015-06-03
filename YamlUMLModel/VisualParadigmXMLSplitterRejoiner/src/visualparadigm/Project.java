package visualparadigm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

import org.w3c.dom.Element;

@XmlRootElement(name = "Project")
@XmlAccessorType(XmlAccessType.FIELD)
public class Project {
	@XmlAnyAttribute
	public Map<QName, String> attributes;
	
	@XmlAnyElement
	public Element ProjectInfo;
	
	@XmlElementWrapper(name="Models")
	@XmlAnyElement
	public List<Element> models = new ArrayList<>();
	
	@XmlElementWrapper(name="Diagrams")
	@XmlAnyElement
	public List<Element> diagram = new ArrayList<>();
}

