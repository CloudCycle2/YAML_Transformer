package org.opentosca.yamlconverter.switchmapper;

public class ImportsSubSwitch extends AbstractSubSwitch {

	public ImportsSubSwitch(Yaml2XmlSwitch parentSwitch) {
		super(parentSwitch);
	}

	@Override
	public void process() {
		if (getServiceTemplate().getImports() != null) {
			// for (final Entry<String, Import> importelem : elem.getImports().entrySet()) {
			// TODO: How do we handle imports?
			// result.getImport().add(case_Import(importelem));
			// }
		}
	}

}
