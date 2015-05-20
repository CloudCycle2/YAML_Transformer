package org.opentosca.yamlconverter.main.exceptions;

/**
 * Used to encapsulate the different Exceptions that can be thrown by the backends for e.g. YAML
 *
 */
public class ConverterException extends Exception {

	/**
	 * generated
	 */
	private static final long serialVersionUID = 4475509585300112934L;

	public ConverterException(Exception e) {
		super(e);
	}
}
