package test.test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.esotericsoftware.yamlbeans.YamlReader;

public class TestYaml {

	public static void main(String[] args) throws IOException, Exception {
		YamlReader r = new YamlReader(new FileReader(new File("testfiles/test.yml")));
		r.getConfig().setBeanProperties(true);
		
		Object t = r.read();
		Marshaller m ;
		JAXBContext context = JAXBContext.newInstance(YamlFileModel.class);
		m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		m.marshal(t, System.out);
		
		r.close();
	}
	
	
}
