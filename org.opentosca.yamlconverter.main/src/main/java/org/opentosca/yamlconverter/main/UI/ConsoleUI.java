package org.opentosca.yamlconverter.main.UI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.opentosca.yamlconverter.main.Parser;
import org.opentosca.yamlconverter.main.utils.CSARUtil;
import org.opentosca.yamlconverter.main.utils.ConstraintUtils;
import org.opentosca.yamlconverter.main.utils.FileUtil;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.Input;

/**
 * A simple User Interface for Console.
 *
 */
public class ConsoleUI {
	/**
	 * The file Util.
	 */
	private static FileUtil fileutil = new FileUtil();

	/**
	 * Whether the cow says it or not.
	 */
	private static boolean COW = true;

	public static void main(String[] args) {
		cowsay("Hi! This is the TOSCA YAML 2 XML Cowverter! Let's start!");
		boolean read = false;
		String yaml = "";
		while (!read) {
			// ask for file
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

		// parse it
		// TODO: make use of the interface, i.e. IToscaYamlParser parser = new Parser();
		// TODO: but before that: put methods to interface if necessary
		final Parser parser = new Parser();
		parser.parse(yaml);
		// TODO: Why can't we just call parser.getInputRequirementsText()? They use the same map...?!
		final Map<String, Input> reqMap = parser.getInputRequirements();
		final Map<String, String> reqText = parser.getInputRequirementsText();
		if (reqMap != null && !reqMap.isEmpty()) {
			// ask for inputs
			cowsay("I need some variables you have to define!");
			final Map<String, String> inputValues = new HashMap<String, String>();
			for (final Entry<String, Input> requirement : reqMap.entrySet()) {
				String userinput = promptString("Variable " + requirement.getKey() + " (" + reqText.get(requirement.getKey()) + "):");
				boolean valid = false;
				while (!valid) {
					valid = true;
					if (userinput != null && !userinput.isEmpty()) {
						valid = ConstraintUtils.matchesConstraints(userinput, requirement.getValue());
						if (valid) {
							inputValues.put(requirement.getKey(), userinput);
						} else {
							userinput = promptString("ERROR: User Input did not fulfill the constraints. Try again.\nVariable "
									+ requirement.getKey() + " (" + reqText.get(requirement.getKey()) + "):");
						}
					}
				}
			}
			parser.setInputValues(inputValues);
		}

		// give results
		// XML
		final String xml = parser.getXML();
		cowsay("I have some results for you!");
		System.out.println("Here is your XML-file:");
		System.out.println(xml);

		final String xmlfilename = promptString("\nIf you want to save this XML, enter a filename, else just hit ENTER...");
		if (xmlfilename != null && !xmlfilename.isEmpty()) {
			try {
				FileUtil.saveStringAsFile(xmlfilename, xml);
			} catch (final IOException e) {
				System.out.println("ERROR: File has not been saved, because of an IOException. Muh..");
			}
		}

		// XSD
		final String xsd = parser.getXSD();
		if (xsd != null && !xsd.isEmpty()) {
			System.out.println("\nAlso I have an XSD-file for you. Save it as types.xsd!");
			System.out.println(xsd);

			final String xsdfilename = promptString("\nIf you want to save this XSD, enter a filename, else just hit ENTER...");
			if (xsdfilename != null && !xsdfilename.isEmpty()) {
				try {
					FileUtil.saveStringAsFile(xsdfilename, xsd);
				} catch (final IOException e) {
					System.out.println("ERROR: File has not been saved, because of an IOException. Muh..");
				}
			}
		}

		final String csarfilename = promptString("\nIf you want to save the results as a CSAR-File, enter a filename, else just hit ENTER...");
		if (csarfilename != null && !csarfilename.isEmpty()) {
			try {
				CSARUtil.createCSAR(parser.getServiceTemplate(), xml, xsd, csarfilename);
			} catch (final IOException e) {
				System.out.println("ERROR: File has not been saved, because of an IOException. Muh..");
			}
		}

		cowsay("Wuhuu! I'm finished with converting. I hope you're happy now! Good Bye!");
		System.out.println("\n\n  exiting...");
	}

	/**
	 * Uses Systems I/O to prompt the user for a lineinput.
	 *
	 * @param promptString The description for the input.
	 * @return the returned line.
	 */
	private static String promptString(String promptString) {
		final BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		System.out.println(promptString);
		String result = null;
		try {
			result = console.readLine();
		} catch (final IOException e) {
			// this suggests there is no console available.
			System.exit(0);
		}
		return result;
	}

	/**
	 * Uses System.out to print a message in cowsay, if cowsay is enabled. Else it justs prints the message
	 *
	 * @param message the message to print
	 */
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
