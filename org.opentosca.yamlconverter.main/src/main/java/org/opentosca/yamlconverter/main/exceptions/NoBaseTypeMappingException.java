package org.opentosca.yamlconverter.main.exceptions;

/**
 * This exception can be thrown if no mapping is defined for a YAML BaseType to a XML BaseType.
 *
 * @author Sebi
 */
public class NoBaseTypeMappingException extends Exception {

    public NoBaseTypeMappingException() {
        super("No mapping defined for your desired request.");
    }

    public NoBaseTypeMappingException(final String expectedYamlBaseType) {
        super("No mapping defined for '"+expectedYamlBaseType+"'");
    }
}
