package org.opentosca.yamlconverter.switchmapper.subswitches;

import org.opentosca.model.tosca.TArtifactType;
import org.opentosca.model.tosca.TExtensibleElements;
import org.opentosca.yamlconverter.switchmapper.Yaml2XmlSwitch;
import org.opentosca.yamlconverter.switchmapper.typemapper.ElementType;
import org.opentosca.yamlconverter.yamlmodel.yaml.element.ArtifactType;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class supports processing artifact types of a YAML service template.
 */
public class ArtifactTypesSubSwitch extends AbstractSubSwitch {

	public ArtifactTypesSubSwitch(Yaml2XmlSwitch parentSwitch) {
		super(parentSwitch);
	}

	/**
	 * For each artifact type of the YAML service template a {@link org.opentosca.model.tosca.TArtifactType} is created.
	 * After processing every artifact type, the whole list of them is added to {@link #getDefinitions()} object is added.
	 */
	@Override
	public void process() {
		if (getServiceTemplate().getArtifact_types() != null) {
			getDefinitions().getServiceTemplateOrNodeTypeOrNodeTypeImplementation().addAll(
					getArtifactTypesFromYaml(getServiceTemplate().getArtifact_types()));
		}
	}

	/**
	 * Creates a list of artifact types by creating a {@link org.opentosca.model.tosca.TArtifactType} for each
	 * artifact type entry. Attributes like name, derived from and properties definition are set.
	 *
	 * @param artifact_types map of YAML artifact types where the key represents an artifacts name
	 * @return collection (= list) of artifact types
	 */
	private Collection<? extends TExtensibleElements> getArtifactTypesFromYaml(Map<String, ArtifactType> artifact_types) {
		final List<TArtifactType> artifactTypes = new ArrayList<TArtifactType>();

		for (final Entry<String, ArtifactType> entry : artifact_types.entrySet()) {
			final TArtifactType artifactType = new TArtifactType();
			final ArtifactType yamlArtifactType = entry.getValue();

			artifactType.setName(entry.getKey());
			artifactType.setTargetNamespace(getTargetNamespace());

			if (yamlArtifactType.getDerived_from() != null && !yamlArtifactType.getDerived_from().equals("")) {
				final QName derivedFromReference = getTypeMapperUtil().getCorrectTypeReferenceAsQName(yamlArtifactType.getDerived_from(),
						ElementType.ARTIFACT_TYPE);
				artifactType.setDerivedFrom(parseDerivedFrom(derivedFromReference));
			}
			artifactType.setPropertiesDefinition(parsePropertiesDefinition(yamlArtifactType.getProperties(), entry.getKey()));

			artifactTypes.add(artifactType);
		}

		return artifactTypes;
	}

}
