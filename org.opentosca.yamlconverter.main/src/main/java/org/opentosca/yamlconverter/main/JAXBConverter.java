package org.opentosca.yamlconverter.main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.opentosca.model.tosca.Definitions;
import org.opentosca.yamlconverter.main.interfaces.IToscaXml2XmlBeanConverter;
import org.opentosca.yamlconverter.main.utils.AnyMap;
import org.xml.sax.SAXException;

import com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper;

/**
 * This converter uses JAXB to convert between Tosca XML and Tosca XML beans.
 *
 * @author Jonas Heinisch
 *
 */
@SuppressWarnings("restriction")
public class JAXBConverter implements IToscaXml2XmlBeanConverter {
	private Schema toscaXSD = null;
	private NamespacePrefixMapper nsPrefixMapper = null;

	public JAXBConverter() {

	}

	public JAXBConverter(NamespacePrefixMapper nsPre) {
		this.nsPrefixMapper = nsPre;
	}

	@Override
	public String xmlbean2xml(Definitions root) {
		try {
			final OutputStream stream = new ByteArrayOutputStream();
			final JAXBContext jaxbContext = JAXBContext.newInstance(Definitions.class, AnyMap.class);
			final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			try {
				if (this.nsPrefixMapper != null) {
					jaxbMarshaller.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", this.nsPrefixMapper);
				}
			} catch (final Exception e) {
			}

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(root, stream);

			return stream.toString();

		} catch (final JAXBException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Definitions xml2xmlbean(String xmlstring) {
		final InputStream stream = string2InputStream(xmlstring);
		try {
			final JAXBContext jaxbContext = JAXBContext.newInstance(Definitions.class);

			final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			jaxbUnmarshaller.setSchema(getToscaSchema());

			final Definitions xroot = (Definitions) jaxbUnmarshaller.unmarshal(stream);
			return xroot;

		} catch (final JAXBException e) {
			e.printStackTrace();
			return null;
		}
	}

	private ByteArrayInputStream string2InputStream(String xmlstring) {
		return new ByteArrayInputStream(xmlstring.getBytes(StandardCharsets.UTF_8));
	}

	public Schema getToscaSchema() {
		if (this.toscaXSD == null) {
			try {
				final SchemaFactory fac = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
				final InputStream is = new FileInputStream(new File("TOSCA-v1.0.xsd"));
				final StreamSource ss = new StreamSource(is);
				this.toscaXSD = fac.newSchema(ss);
			} catch (final FileNotFoundException e) {
				// file not found
				System.err.println("Schema file for the report not found");
			} catch (final SAXException e) {
				// Error during parsing the schema file
				System.err.println("Schema file for the report could not be parsed:");
				e.printStackTrace();
			}
		}

		return this.toscaXSD;
	}

}
