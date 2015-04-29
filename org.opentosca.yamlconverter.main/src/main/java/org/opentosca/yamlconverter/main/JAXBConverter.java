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

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * This converter uses JAXB to convert between Tosca XML and Tosca XML beans. It needs a schema from {@link #FILE_NAME_TOSCA_V1_0_XSD} to
 * create beans based on XML.
 *
 * @author Jonas Heinisch
 *
 */
public class JAXBConverter implements IToscaXml2XmlBeanConverter {

	public static final String FILE_NAME_TOSCA_V1_0_XSD = "TOSCA-v1.0.xsd";
	public static final String DEFAULT_XMLSCHEMA = "http://www.w3.org/2001/XMLSchema";

	private Schema toscaXSD = null;
	private NamespacePrefixMapper nsPrefixMapper = null;

	public JAXBConverter() {

	}

	public JAXBConverter(NamespacePrefixMapper nsPre) {
		this.nsPrefixMapper = nsPre;
	}

	/**
	 * Produce XML based on Tosca XML bean(s) defined in {@code root}.
	 *
	 * @param root The Tosca XML root bean
	 * @return produced XML as a String
	 */
	@Override
	public String convertToXml(Definitions root) {
		try {
			final OutputStream stream = new ByteArrayOutputStream();
			final JAXBContext jaxbContext = JAXBContext.newInstance(Definitions.class, AnyMap.class);
			final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			if (this.nsPrefixMapper != null) {
				jaxbMarshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", this.nsPrefixMapper);
			} else {
				System.out.println("Can't set property 'com.sun.xml.internal.bind.namespacePrefixMapper' "
						+ "for JAXB marshaller, because no prefix mapper is defined.");
			}

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(root, stream);

			return stream.toString();
		} catch (final Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Produce Tosca XML beans based on a XML document as string.
	 *
	 * @param xmlstring A Tosca XML-containing String
	 * @return the Tosca root element
	 */
	@Override
	public Definitions convertToXmlBean(String xmlstring) {
		final InputStream stream = string2InputStream(xmlstring);
		try {
			final JAXBContext jaxbContext = JAXBContext.newInstance(Definitions.class);

			final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			jaxbUnmarshaller.setSchema(getToscaSchema());

			return (Definitions) jaxbUnmarshaller.unmarshal(stream);
		} catch (final JAXBException e) {
			e.printStackTrace();
			return null;
		}
	}

	private ByteArrayInputStream string2InputStream(String xmlstring) {
		return new ByteArrayInputStream(xmlstring.getBytes(StandardCharsets.UTF_8));
	}

	private Schema getToscaSchema() {
		if (this.toscaXSD == null) {
			try {
				final SchemaFactory fac = SchemaFactory.newInstance(DEFAULT_XMLSCHEMA);
				final InputStream is = new FileInputStream(new File(FILE_NAME_TOSCA_V1_0_XSD));
				final StreamSource ss = new StreamSource(is);
				this.toscaXSD = fac.newSchema(ss);
			} catch (final FileNotFoundException e) {
				// file not found
				System.err.println("Schema file " + FILE_NAME_TOSCA_V1_0_XSD + " for the report not found");
			} catch (final SAXException e) {
				// Error during parsing the schema file
				System.err.println("Schema file " + FILE_NAME_TOSCA_V1_0_XSD + " for the report could not be parsed:");
				e.printStackTrace();
			}
		}

		return this.toscaXSD;
	}

}
