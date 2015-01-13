package org.opentosca.yamlconverter.main;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

public class XmlAnyLaxTest {

	// @formatter:off
	private static final String xml = "<message to=\"john@example.com\" from=\"jane@example.com\">" +
			"<customer>" +
			"<name>Sue Smith</name>" +
			"<address>" +
			"<street>123 A Street</street>" +
			"<city>Any Town</city>" +
			"</address>" +
			"</customer>" +
			"</message>";
	// @formatter:on

	public static void main(String[] args) throws Exception {
		final JAXBContext jc = JAXBContext
				.newInstance(Message.class, Customer.class, Address.class, GenericProperties.class, HashMap.class);

		final Unmarshaller unmarshaller = jc.createUnmarshaller();
		final Message message = (Message) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));

		// TODO Does not work!
		final GenericProperties props = new GenericProperties();
		props.properties = new HashMap<String, String>();
		props.properties.put("lol", "yeah");

		message.body = props;

		final Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(message, System.out);
	}

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	static class GenericProperties {

		public Map<String, String> properties;

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
