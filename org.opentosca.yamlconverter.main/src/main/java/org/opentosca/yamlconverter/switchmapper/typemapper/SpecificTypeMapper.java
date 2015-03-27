package org.opentosca.yamlconverter.switchmapper.typemapper;

import org.opentosca.yamlconverter.main.exceptions.NoBaseTypeMappingException;

/**
 * @author Sebi
 */
public final class SpecificTypeMapper extends AbstractTypeMapper {

    /**
     * Selects the relationship type name from XML for {@code yamlRelationshipType}.
     *
     * @param yamlRelationshipType name of the relationship type
     * @return the name of its XML representation
     * @throws NoBaseTypeMappingException if no mapping exists for {@code yamlRelationshipType}
     */
    @Override
    public String getXmlRelationshipType(final String yamlRelationshipType) throws NoBaseTypeMappingException {
        throw new NoBaseTypeMappingException();
    }

    /**
     * Selects the capability type name from XML for {@code yamlCapabilityType}.
     *
     * @param yamlCapabilityType name of the capability type
     * @return the name of its XML representation
     * @throws NoBaseTypeMappingException if no mapping exists for {@code yamlCapabilityType}
     */
    @Override
    public String getXmlCapabilityType(final String yamlCapabilityType) throws NoBaseTypeMappingException {
        switch (yamlCapabilityType) {
            case TOSCA_CAPABILITIES + "DatabaseEndpoint.MySQL":
                return "MySQLDatabaseEndpointCapability";
            default:
                throw new NoBaseTypeMappingException(yamlCapabilityType);
        }
    }

    /**
     * Selects the interface name from XML for {@code yamlInterfaceName}.
     *
     * @param yamlInterfaceName name of the interface
     * @return the name of its XML representation
     * @throws NoBaseTypeMappingException if no mapping exists for {@code yamlInterfaceName}
     */
    @Override
    public String getXmlInterface(final String yamlInterfaceName) throws NoBaseTypeMappingException {
        throw new NoBaseTypeMappingException();
    }

    /**
     * Selects the node type name from XML for {@code yamlNodeType}.
     *
     * @param yamlNodeType name of the node type
     * @return the name of its XML representation
     * @throws NoBaseTypeMappingException if no mapping exists for {@code yamlNodeType}
     */
    @Override
    public String getXmlNodeType(final String yamlNodeType) throws NoBaseTypeMappingException {
        switch (yamlNodeType) {
            case TOSCA_NODES + "DBMS.MySQL":
                return "MySQL";
            case TOSCA_NODES + "Database.MySQL":
                return "MySQLDatabase";
            case TOSCA_NODES + "WebServer.Apache":
                return "ApacheWebServer";
            default:
                throw new NoBaseTypeMappingException(yamlNodeType);
        }
    }

    /**
     * Selects the artifact type name from XML for {@code yamlArtifactType}.
     *
     * @param yamlArtifactType name of the artifact type
     * @return the name of its XML representation
     * @throws NoBaseTypeMappingException if no mapping exists for {@code yamlArtifactType}
     */
    @Override
    public String getXmlArtifactType(final String yamlArtifactType) throws NoBaseTypeMappingException {
        throw new NoBaseTypeMappingException();
    }
}
