package org.opentosca.yamlconverter.main;

import org.opentosca.model.tosca.TDefinitions;
import org.opentosca.yamlconverter.main.interfaces.IToscaXml2XmlBeanConverter;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * This converter uses JAXB to convert between Tosca XML and Tosca XML beans.
 * 
 * @author Jonas Heinisch
 *
 */
public class JAXBConverter implements IToscaXml2XmlBeanConverter {
	Schema toscaXSD = null;

	@Override
	public String xmlbean2xml(TDefinitions root) {
		// TODO: this is not tested!
		try {
			OutputStream stream = new ByteArrayOutputStream();
			JAXBContext jaxbContext = JAXBContext
					.newInstance(TDefinitions.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setSchema(getToscaSchema());

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(root, stream);

			return stream.toString();

		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public TDefinitions xml2xmlbean(String xmlstring) {
		// TODO: this is not tested!
		InputStream stream = string2InputStream(xmlstring);
		try {
			JAXBContext jaxbContext = JAXBContext
					.newInstance(TDefinitions.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			jaxbUnmarshaller.setSchema(getToscaSchema());
			
			TDefinitions xroot = (TDefinitions) jaxbUnmarshaller
					.unmarshal(stream);
			return xroot;

		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
	}

	private ByteArrayInputStream string2InputStream(String xmlstring) {
		return new ByteArrayInputStream(
				xmlstring.getBytes(StandardCharsets.UTF_8));
	}

	public Schema getToscaSchema(){
		if (toscaXSD == null){
			try {
				SchemaFactory fac = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
				InputStream is = new FileInputStream(new File("TOSCA-v1.0.xsd"));
				StreamSource ss = new StreamSource(is);
				toscaXSD = fac.newSchema(ss);
			} catch (FileNotFoundException e){
				// file not found
				System.err.println("Schema file for the report not found");
			} catch (SAXException e) {
				// Error during parsing the schema file
				System.err.println("Schema file for the report could not be parsed:");
				e.printStackTrace();
			}			
		}

		return toscaXSD;
	}

}
