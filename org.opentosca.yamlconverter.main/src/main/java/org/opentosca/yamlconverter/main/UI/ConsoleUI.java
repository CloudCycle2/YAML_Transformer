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
	private static boolean COW = true;

	public static void main(String[] args) {
		cowsay("Hi! This is the TOSCA YAML 2 XML Cowverter! Let's start!");
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
				System.out.println("ERROR: Filename not valid! Muh..");
				read = false;
			} catch (final IOException e) {
				System.out.println("ERROR: File could not be read! Muh..");
				read = false;
			} catch (final NullPointerException e) {
				System.out.println("ERROR: File could not be found! Muh..");
			}
		}
		final Parser parser = new Parser();
		parser.parse(yaml);
		final Map<String, String> reqMap = parser.getInputRequirements();
		if (!reqMap.isEmpty()) {
			cowsay("I need some variables you have to define!");
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
		cowsay("I have some results for you!");
		System.out.println("Here is your XML-file:");
		System.out.println(xml);

		final String xmlfilename = promptString("\nIf you want to save this XML, enter a filename, else just hit ENTER...");
		if (!xmlfilename.isEmpty()) {
			try {
				fileutil.saveStringAsFile(xmlfilename, xml);
			} catch (final IOException e) {
				System.out.println("ERROR: File has not been saved, because of an IOException. Muh..");
			}
		}

		final String xsd = parser.getXSD();
		if (!xsd.isEmpty()) {
			System.out.println("\nAlso I have an XSD-file for you. Save it as types.xsd!");
			System.out.println(xsd);

			final String xsdfilename = promptString("\nIf you want to save this XSD, enter a filename, else just hit ENTER...");
			if (!xsdfilename.isEmpty()) {
				try {
					fileutil.saveStringAsFile(xsdfilename, xsd);
				} catch (final IOException e) {
					System.out.println("ERROR: File has not been saved, because of an IOException. Muh..");
				}
			}
		}

		cowsay("Wuhuu! I'm finished with converting. I hope you're happy now! Good Bye!");
		System.out.println("\n\n  exiting...");
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

	private static void cowsay(String message) {
		if (COW) {
			final int messageLength = message.length();
			String top = " ";
			String bottom = " ";
			final String contentAndBorders = "< " + message + " >";
			String speechBubble;
			String cow;
			String cowsay;

			for (int i = 1; i <= messageLength + 2; i++) {
				top += "_";
				bottom += "-";
			}

			speechBubble = top + "\n";
			speechBubble += contentAndBorders + "\n";
			speechBubble += bottom + "\n";

			cow = "        \\   ^__^" + "\n";
			cow += "         \\  (oo)\\_______" + "\n";
			cow += "            (__)\\       )\\/\\" + "\n";
			cow += "                ||----w |" + "\n";
			cow += "                ||     ||" + "\n";

			cowsay = speechBubble + cow;

			System.out.println(cowsay);
		} else {
			System.out.println("\n" + message + "\n");
		}
	}
}
