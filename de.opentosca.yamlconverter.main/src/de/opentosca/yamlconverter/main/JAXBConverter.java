package de.opentosca.yamlconverter.main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.opentosca.model.tosca.TDefinitions;

import de.opentosca.yamlconverter.main.interfaces.ItoscaXML2XMLbeanConverter;

/**
 * This converter uses JAXB to convert between Tosca XML and Tosca XML beans.
 * 
 * @author Jonas Heinisch
 *
 */
public class JAXBConverter implements ItoscaXML2XMLbeanConverter {

	@Override
	public String xmlbean2xml(TDefinitions root) {
		// TODO: this is not tested!
		try {
			OutputStream stream = new ByteArrayOutputStream();
			JAXBContext jaxbContext = JAXBContext
					.newInstance(TDefinitions.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

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

}
