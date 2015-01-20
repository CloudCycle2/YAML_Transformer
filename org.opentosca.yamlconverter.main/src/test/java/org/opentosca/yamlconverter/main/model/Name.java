package org.opentosca.yamlconverter.main.model;

import java.beans.ConstructorProperties;

/**
 * @author Sebi
 */
public class Name {
	public String name;

	@ConstructorProperties({ "name" })
	public Name(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
