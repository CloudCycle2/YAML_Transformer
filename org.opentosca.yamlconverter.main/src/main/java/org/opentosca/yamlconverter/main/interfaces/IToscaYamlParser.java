package org.opentosca.yamlconverter.main.interfaces;

import java.io.IOException;
import java.util.Map;

/**
 * This interface is the main interface to parse a YAML document and get its XML representation as a response. Method call order:
 *
 * <pre>
 * 1.) {@link #parse(String)}
 * 2.) {@link #getInputRequirementsText()}
 * 3.) {@link #setInputValues(java.util.Map)}
 * 4.) {@link #getXML()}
 * 5.) {@link #getXSD()}
 * 6.) {@link #makeCsar(String)}
 * </pre>
 *
 * Method calls 2, 3, 5 and 6 are not necessary, but recommended.
 */
public interface IToscaYamlParser {

	/**
	 * Tell the parser to parse the YAML string to XML.
	 *
	 * Note: The used YAML parser requires a YAML document without tabs. Instead you use two whitespaces for indentation. Also take of
	 * masking special inputs like a JSON string. E.g. {key:"some_value-with:special/characters"} has to be masked as
	 * "{key:'some_value-with:special/characters'}", because braces {} are interpreted as (Hash-)Maps as well as a colon ':'. User double
	 * quotes for masking and single quotes to label a string within. The parser only accepts a whole YAML document. You can't distribute
	 * your YAML document over multiple files at the moment.
	 *
	 * @param yamlString YAML ServiceTemplate as a String
	 */
	public void parse(String yamlString);

	/**
	 * Get the parsed YAML as a XML document as String. <br>
	 * Requires previous call of {@link #parse(String)}. <br>
	 * Optional method calls: call {@link #getInputRequirementsText()} to retrieve inputs and {@link #setInputValues(java.util.Map)} to set
	 * values for YAML inputs.
	 *
	 * @return parsed XML as String
	 */
	public String getXML();

	/**
	 * Get the corresponding XSD as a String. <br>
	 * Requires previous call of {@link #parse(String)}. <br>
	 * Optional method calls: call {@link #getInputRequirementsText()} to retrieve inputs and {@link #setInputValues(java.util.Map)} to set
	 * values for YAML inputs.
	 *
	 * @return parsed XSD
	 */
	public String getXSD();

	/**
	 * Get a Map containing required input variables and a description about the input requirements. Map: inputVariableName ->
	 * description/requirements Requires previous call of parse(..) If this method is not called, you can only create the map for
	 * {@link #setInputValues(java.util.Map)} by hand by using the names from your YAML input string.
	 *
	 * @return input requirements map
	 */
	public Map<String, String> getInputRequirementsText();

	/**
	 * Set the values for the input Variables. Map: inputVariableName -> inputVariableValue The keys must be the same as returned from
	 * {@link #getInputRequirementsText()}, otherwise a mapping is not possible. If this method is not called, default values will be set.
	 *
	 * @param input the inputVariable map
	 */
	public void setInputValues(Map<String, String> input);

	/**
	 * Creates a csar file at the specified path. Prior to calling this make sure to have called {@link #parse(String)}.
	 *
	 * @param path the path at which the csar file is to be generated
	 * @throws IOException if the file could not have been created
	 */
	public void makeCsar(String path) throws IOException;
}
