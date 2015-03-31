package org.opentosca.yamlconverter.main.exceptions;

/**
 * This exception can be thrown if no mapping is defined for a YAML base or specific type to a XML base or specific type.
 *
 * @author Sebi
 */
public class NoTypeMappingException extends Exception {

    public NoTypeMappingException() {
        super("No mapping defined for your desired request.");
    }

    public NoTypeMappingException(final String expectedYamlBaseType) {
        super("No mapping defined for '"+expectedYamlBaseType+"'");
    }
}
