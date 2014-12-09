package org.opentosca.yamlconverter.main.exceptions;

/**
 * Used to encapsulate the different Exceptions that can be thrown by the backends for e.g. YAML
 *
 */
public class ConverterException extends Exception{

	private static final long serialVersionUID = 1L;

	public ConverterException(Exception e){
		super(e);
	}
}
