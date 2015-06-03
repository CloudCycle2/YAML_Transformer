package splitterrejoiner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import visualparadigm.Project;

public class Main {

	public static void main(String[] args) throws JAXBException,
			TransformerException, IOException, ParserConfigurationException,
			SAXException {
		if (args != null && args.length == 2) {
			if (args[0].toLowerCase().equals("split"))
				split(args[1]);
			if (args[0].toLowerCase().equals("join"))
				join(args[1]);
		} else {
			printHelp();
		}
	}

	private static void join(String filename) throws IOException,
			ParserConfigurationException, SAXException, JAXBException {
		File file = new File(filename).getCanonicalFile();
		File dir = file.getParentFile();

		Project pr = new Project();

		for (File element : dir.listFiles()) {
			if (element.getName().endsWith(".properties")) {
				pr.attributes = readMap(element);
			}
			if (element.getName().endsWith(".ProjectInfo.part.xml")) {
				pr.ProjectInfo = readNode(element);
			}
			if (element.getName().endsWith(".models.part.xml")) {
				pr.models.add(readNode(element));
			}
			if (element.getName().endsWith(".diagram.part.xml")) {
				pr.diagram.add(readNode(element));
			}
		}

		JAXBContext context = JAXBContext.newInstance(Project.class);
		Marshaller in = context.createMarshaller();
		in.marshal(pr, file);

		System.out.println("Join Done");
	}

	private static void split(String filename) throws JAXBException,
			TransformerException, IOException {
		File file = new File(filename).getCanonicalFile();
		JAXBContext context = JAXBContext.newInstance(Project.class);
		Unmarshaller un = context.createUnmarshaller();
		Project pr = (Project) un.unmarshal(file);

		Map<QName, String> attributes = pr.attributes;
		String name = attributes.get(QName.valueOf("Name"));
		writeToFile(new File(file.getParent() + "/" + name
				+ ".properties"), attributes);

		Element ble = pr.ProjectInfo;
		name = getName(ble);
		String content = toString(ble);
		writeToFile(new File(file.getParent() + "/" + name
				+ ".ProjectInfo.part.xml"), content);

		String type;
		for (Element bl : pr.models) {
			name = getName(bl);
			type = getType(bl);
			content = toString(bl);
			writeToFile(new File(file.getParent() + "/" + type + "_" + name
					+ ".models.part.xml"), content);
		}
		for (Element bl : pr.diagram) {
			name = getName(bl);
			type = getType(bl);
			content = toString(bl);
			writeToFile(new File(file.getParent() + "/" + type + "_" + name
					+ ".diagram.part.xml"), content);
		}

		System.out.println("Split Done");
	}

	private static void printHelp() {
		System.out.println("Usage:");
		System.out.println(" split <file>");
		System.out.println(" join <file>");
	}

	public static void writeToFile(File file, String content)
			throws IOException {
		try (BufferedWriter bos = new BufferedWriter(new FileWriter(file))) {
			bos.write(content);
			bos.flush();
		}
	}

	public static void writeToFile(File file, Map<QName, String> content)
			throws IOException {
		try (BufferedWriter bos = new BufferedWriter(new FileWriter(file))) {
			Set<Map.Entry<QName, String>> set = content.entrySet();
			List<Map.Entry<QName, String>> list = new ArrayList<>();
			list.addAll(set);
			Collections.sort(list, new Comparator<Map.Entry<QName, String>>() {
				@Override
				public int compare(Entry<QName, String> o1,
						Entry<QName, String> o2) {
					return o1.getKey().getLocalPart()
							.compareTo(o2.getKey().getLocalPart());
				}
			});
			for (Map.Entry<QName, String> entry : list) {
				bos.write(entry.getKey().getLocalPart() + "="
						+ entry.getValue() + "\n");
			}
			bos.flush();
		}
	}

	public static String toString(Element node) throws TransformerException,  UnsupportedEncodingException {
		node.normalize();
		TransformerFactory transFactory = TransformerFactory.newInstance();
		transFactory.setAttribute("indent-number", 2);
		Transformer transformer = transFactory.newTransformer();
		
		StringWriter buffer = new StringWriter();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.transform(new DOMSource(node), new StreamResult(buffer));
		
		return format(buffer.toString());
	}
	
    public static String format(String xml) {

        try {
            final InputSource src = new InputSource(new StringReader(xml));
            final Node document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src).getDocumentElement();
            final Boolean keepDeclaration = Boolean.valueOf(xml.startsWith("<?xml"));

        //May need this: System.setProperty(DOMImplementationRegistry.PROPERTY,"com.sun.org.apache.xerces.internal.dom.DOMImplementationSourceImpl");


            final DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            final DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
            final LSSerializer writer = impl.createLSSerializer();

            writer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE); // Set this to true if the output needs to be beautified.
            writer.getDomConfig().setParameter("xml-declaration", keepDeclaration); // Set this to true if the declaration is needed to be outputted.

            return writer.writeToString(document);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

	public static String getName(Element node) throws TransformerException {
		return node.getAttribute("Name");
	}

	public static String getType(Element node) throws TransformerException {
		return node.getNodeName();
	}

	public static Element readNode(File file)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(file);
		return doc.getDocumentElement();
	}

	public static Map<QName, String> readMap(File element)
			throws FileNotFoundException, IOException {
		Properties p = new Properties();
		try (InputStreamReader isr = new InputStreamReader(new FileInputStream(
				element), "UTF-8")) {
			p.load(isr);
		}
		Map<QName, String> hm = new HashMap<QName, String>();
		Enumeration<Object> e = p.keys();
		while (e.hasMoreElements()) {
			String s = (String) e.nextElement();
			QName q = QName.valueOf(s);
			hm.put(q, p.getProperty(s));
		}
		return hm;
	}
}
