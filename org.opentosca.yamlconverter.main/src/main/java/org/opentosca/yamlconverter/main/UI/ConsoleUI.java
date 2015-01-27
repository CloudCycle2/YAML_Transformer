package org.opentosca.yamlconverter.main.UI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.opentosca.yamlconverter.main.Parser;
import org.opentosca.yamlconverter.main.utils.FileUtil;

public class ConsoleUI {
	private static FileUtil fileutil = new FileUtil();

	public static void main(String[] args) {
		System.out.println("Hi! This is the TOSCA YAML 2 XML Converter! Let's start!");
		boolean read = false;
		String yaml = "";
		while (!read) {
			String filename = "";
			if (args.length > 0) {
				filename = args[0];
				if (args.length > 1) {
					for (int i = 1; i < args.length; i++) {
						System.out.println("Warning: unknown parameter " + args[i]);
					}
				}
			} else {
				filename = promptString("Where can I find a YAML-file?");
			}
			try {
				yaml = fileutil.readYamlResource(filename);
				read = true;
			} catch (final URISyntaxException e) {
				System.out.println("Filename not valid!");
				read = false;
			} catch (final IOException e) {
				System.out.println("File could not be read!");
				read = false;
			}
		}
		final Parser parser = new Parser();
		parser.parse(yaml);
		final Map<String, String> reqMap = parser.getInputRequirements();
		if (!reqMap.isEmpty()) {
			System.out.println("I need some variables you have to define!");
			final Map<String, String> inputValues = new HashMap<String, String>();
			for (final Entry<String, String> requirement : reqMap.entrySet()) {
				final String userinput = promptString("Variable " + requirement.getKey() + " (" + requirement.getValue() + "):");
				if (!userinput.isEmpty()) {
					inputValues.put(requirement.getKey(), userinput);
				}
			}
			parser.setInputValues(inputValues);
		} else {
			// No input fields
		}
		final String xml = parser.getXML();
		System.out.println("I have some results for you!\n");
		System.out.println("Here is your XML-file:");
		System.out.println(xml);

		final String xmlfilename = promptString("\nIf you want to save this XML, enter a filename, else just hit ENTER...");
		if (!xmlfilename.isEmpty()) {
			try {
				fileutil.saveStringAsFile(xmlfilename, xml);
			} catch (final IOException e) {
				System.out.println("ERROR: File has not been saved, because of an IOException.");
			}
		}

		final String xsd = parser.getXSD();
		System.out.println("\nAlso I have an XSD-file for you. Save it as types.xsd!");
		System.out.println(xsd);

		final String xsdfilename = promptString("\nIf you want to save this XSD, enter a filename, else just hit ENTER...");
		if (!xsdfilename.isEmpty()) {
			try {
				fileutil.saveStringAsFile(xsdfilename, xsd);
			} catch (final IOException e) {
				System.out.println("ERROR: File has not been saved, because of an IOException.");
			}
		}
	}

	private static String promptString(String promptString) {
		final BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		System.out.println(promptString);
		String result = null;
		try {
			result = console.readLine();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
