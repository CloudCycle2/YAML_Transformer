package org.opentosca.yamlconverter.main;

import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import javax.xml.namespace.QName;
import java.io.ByteArrayInputStream;
import java.util.*;

public class XmlAnyLaxTest extends BaseTest {

	// @formatter:off
	private static final String xml = ""+
			"<message to=\"john@example.com\" from=\"jane@example.com\">" +
			"	<customer>" +
			"		<name>Sue Smith</name>" +
			"		<address>" +
			"			<street>123 A Street</street>" +
			"			<city>Any Town</city>" +
			"		</address>" +
			"	</customer>" +
			"</message>";
	// @formatter:on

	/**
	 * This test only tries to prove that using generic properties in XML is possible. It doesn't really test anything
	 * like matching a string or something else!
	 * @throws Exception
	 */
	@Test
	public void testUseAnyMapInXml() throws Exception {
		final JAXBContext jc = JAXBContext
				.newInstance(Message.class, Customer.class, Address.class, GenericProperties.class, HashMap.class);

		final Unmarshaller unmarshaller = jc.createUnmarshaller();
		final Message message = (Message) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));

		final Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("key1234", "value1");
		map.put("key2", "value2");
		final GenericProperties props = new GenericProperties(map);

		message.body = props;

		final Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(message, System.out);
	}

	@XmlRootElement(name = "Properties")
	static class GenericProperties {
		@XmlAnyElement
		public List<JAXBElement<String>> entries = new ArrayList<JAXBElement<String>>();

		public GenericProperties() { // JAXB required
		}

		public GenericProperties(Map<String, String> map) {
			for (final Map.Entry<String, String> entry : map.entrySet()) {
				this.entries.add(new JAXBElement<String>(new QName(entry.getKey()), String.class, entry.getValue()));
			}
		}
	}

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	static class Message {

		@XmlAttribute
		private String to;

		@XmlAttribute
		private String from;

		@XmlAnyElement(lax = true)
		private Object body;

	}

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	static class Customer {

		private String name;
		private Address address;

	}

	@XmlAccessorType(XmlAccessType.FIELD)
	static class Address {

		private String street;
		private String city;

	}
}
