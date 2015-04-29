package org.opentosca.yamlconverter.switchmapper.utils;

import javax.xml.namespace.QName;

import org.opentosca.yamlconverter.main.exceptions.NoTypeMappingException;
import org.opentosca.yamlconverter.switchmapper.typemapper.AbstractTypeMapper;
import org.opentosca.yamlconverter.switchmapper.typemapper.BaseTypeMapper;
import org.opentosca.yamlconverter.switchmapper.typemapper.ElementType;
import org.opentosca.yamlconverter.switchmapper.typemapper.SpecificTypeMapper;

/**
 * @author Sebi
 */
public class TypeMapperUtil {

	private final AbstractTypeMapper baseTypeMapper = new BaseTypeMapper();
	private final AbstractTypeMapper specificTypeMapper = new SpecificTypeMapper();
	private final NamespaceUtil namespaceUtil;

	public TypeMapperUtil(NamespaceUtil namespaceUtil) {
		if (namespaceUtil == null) {
			throw new IllegalArgumentException("namespace util must be set.");
		}
		this.namespaceUtil = namespaceUtil;
	}

	/**
	 * Depending on {@code elementType} and {@code initName}, a {@link javax.xml.namespace.QName} object is created containing a xml
	 * reference. {@code initName} can be a name of an existing XML base or specific type. Thus
	 * {@link org.opentosca.yamlconverter.switchmapper.typemapper.BaseTypeMapper} and
	 * {@link org.opentosca.yamlconverter.switchmapper.typemapper.SpecificTypeMapper} are used to try to get the correct XML type name. If
	 * this fails, it is assumed that initName is an individual name and can be used directly as a reference name.
	 *
	 * @param initName name of the type to reference to
	 * @param elementType element type of initName
	 * @return {@link javax.xml.namespace.QName} object containing a reference to initName or its XML representation
	 */
	public QName getCorrectTypeReferenceAsQName(String initName, ElementType elementType) {
		if (initName == null || initName.isEmpty() || elementType == null) {
			throw new IllegalArgumentException("initial type name and element type may not be null or empty!");
		}
		QName result = null;
		try {
			switch (elementType) {
			case RELATIONSHIP_TYPE:
				result = getQNameOfRelationshipType(initName);
				break;
			case CAPABILITY_TYPE:
				result = getQNameOfCapabilityType(initName);
				break;
			case INTERFACE:
				result = getQNameOfInterface(initName);
				break;
			case NODE_TYPE:
				result = getQNameOfNodeType(initName);
				break;
			case ARTIFACT_TYPE:
				result = getQNameOfArtifactType(initName);
				break;
			default:
				result = this.namespaceUtil.toTnsQName(initName);
				break;
			}
		} catch (final NoTypeMappingException e) {
			result = this.namespaceUtil.toTnsQName(initName);
		}
		return result;
	}

	public QName getCorrectTypeReferenceAsQNameForProperties(String initName, ElementType elementType) {
		if (initName == null || initName.isEmpty() || elementType == null) {
			throw new IllegalArgumentException("initial type name and element type may not be null or empty!");
		}
		QName result = null;
		try {
			switch (elementType) {
			case RELATIONSHIP_TYPE:
				result = getQNameOfRelationshipType(initName);
				break;
			case CAPABILITY_TYPE:
				result = getQNameOfCapabilityType(initName);
				break;
			case INTERFACE:
				result = getQNameOfInterface(initName);
				break;
			case NODE_TYPE:
				result = getQNameOfNodeType(initName);
				break;
			case ARTIFACT_TYPE:
				result = getQNameOfArtifactType(initName);
				break;
			default:
				result = this.namespaceUtil.toTypeNSQName(initName);
				break;
			}
		} catch (final NoTypeMappingException e) {
			result = this.namespaceUtil.toTypeNSQName(initName);
		}
		return result;
	}

	private QName getQNameOfArtifactType(final String initName) throws NoTypeMappingException {
		try {
			return this.namespaceUtil.toBaseTypesNsQName(this.baseTypeMapper.getXmlArtifactType(initName));
		} catch (final NoTypeMappingException e) {
			return this.namespaceUtil.toSpecificTypesNsQName(this.specificTypeMapper.getXmlArtifactType(initName));
		}
	}

	private QName getQNameOfNodeType(final String initName) throws NoTypeMappingException {
		try {
			return this.namespaceUtil.toBaseTypesNsQName(this.baseTypeMapper.getXmlNodeType(initName));
		} catch (final NoTypeMappingException e) {
			return this.namespaceUtil.toSpecificTypesNsQName(this.specificTypeMapper.getXmlNodeType(initName));
		}
	}

	private QName getQNameOfInterface(final String initName) throws NoTypeMappingException {
		try {
			return this.namespaceUtil.toBaseTypesNsQName(this.baseTypeMapper.getXmlInterface(initName));
		} catch (final NoTypeMappingException e) {
			return this.namespaceUtil.toSpecificTypesNsQName(this.specificTypeMapper.getXmlInterface(initName));
		}
	}

	private QName getQNameOfCapabilityType(final String initName) throws NoTypeMappingException {
		try {
			return this.namespaceUtil.toBaseTypesNsQName(this.baseTypeMapper.getXmlCapabilityType(initName));
		} catch (final NoTypeMappingException e) {
			return this.namespaceUtil.toSpecificTypesNsQName(this.specificTypeMapper.getXmlCapabilityType(initName));
		}
	}

	private QName getQNameOfRelationshipType(final String initName) throws NoTypeMappingException {
		try {
			return this.namespaceUtil.toBaseTypesNsQName(this.baseTypeMapper.getXmlRelationshipType(initName));
		} catch (final NoTypeMappingException e) {
			return this.namespaceUtil.toSpecificTypesNsQName(this.specificTypeMapper.getXmlRelationshipType(initName));
		}
	}
}
