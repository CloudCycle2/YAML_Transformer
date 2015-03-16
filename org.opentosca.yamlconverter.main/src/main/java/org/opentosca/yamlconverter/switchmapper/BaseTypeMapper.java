package org.opentosca.yamlconverter.switchmapper;

import org.opentosca.yamlconverter.main.exceptions.NoBaseTypeMappingException;

/**
 * This class supports the mapping/conversion of type names from YAML to XML.
 * E.g. YAML BaseTypes for relationships are tosca.nodes.Root, etc. and the corresponding ones in XML are
 * RootRelationshipType, etc.
 * The methods support BaseTypes as well as SpecificTypes for YAML/XML.
 *
 * @author Sebi
 */
public final class BaseTypeMapper {

    /**
     * Base names for YAML types.
     */
    private static final String TOSCA_RELATIONSHIPS = "tosca.relationships.";
    private static final String TOSCA_CAPABILITIES = "tosca.capabilities.";
    private static final String TOSCA_NODES = "tosca.nodes.";
    private static final String TOSCA_ARTIFACTS = "tosca.artifacts.";

    /**
     * Selects the relationship type name from XML for {@code yamlRelationshipType}.
     *
     * @param yamlRelationshipType name of the relationship type
     * @return the name of its XML representation
     * @throws NoBaseTypeMappingException if no mapping exists for {@code yamlRelationshipType}
     */
    public static String getXmlRelationshipType(String yamlRelationshipType) throws NoBaseTypeMappingException {
        switch (yamlRelationshipType) {
            case TOSCA_RELATIONSHIPS + "Root":
                return "RootRelationshipType";
            case TOSCA_RELATIONSHIPS + "DependsOn":
                return "DependsOn";
            case TOSCA_RELATIONSHIPS + "HostedOn":
                return "HostedOn";
            case TOSCA_RELATIONSHIPS + "ConnectsTo":
                return "ConnectsTo";
            default:
                throw new NoBaseTypeMappingException(yamlRelationshipType);
        }
    }

    /**
     * Selects the capability type name from XML for {@code yamlCapabilityType}.
     *
     * @param yamlCapabilityType name of the capability type
     * @return the name of its XML representation
     * @throws NoBaseTypeMappingException if no mapping exists for {@code yamlCapabilityType}
     */
    public static String getXmlCapabilityType(String yamlCapabilityType) throws NoBaseTypeMappingException {
        switch (yamlCapabilityType) {
            case TOSCA_CAPABILITIES + "Root":
                return "RootCapabilityType";
            case TOSCA_CAPABILITIES + "Feature":
                return "FeatureCapability";
            case TOSCA_CAPABILITIES + "Container":
                return "ContainerCapability";
            case TOSCA_CAPABILITIES + "Endpoint":
                return "EndpointCapability";
            case TOSCA_CAPABILITIES + "DatabaseEndpoint":
                return "DatabaseEndpointCapability";
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
    public static String getXmlInterface(String yamlInterfaceName) throws NoBaseTypeMappingException {
        throw new NoBaseTypeMappingException();
    }

    /**
     * Selects the node type name from XML for {@code yamlNodeType}.
     *
     * @param yamlNodeType name of the node type
     * @return the name of its XML representation
     * @throws NoBaseTypeMappingException if no mapping exists for {@code yamlNodeType}
     */
    public static String getXmlNodeType(String yamlNodeType) throws NoBaseTypeMappingException {
        switch (yamlNodeType) {
            case TOSCA_NODES + "Root":
                return "RootNodeType";
            case TOSCA_NODES + "Compute":
                return "Server";
            case TOSCA_NODES + "SoftwareComponent":
                return "OperatingSystem";
            case TOSCA_NODES + "WebServer":
                return "WebServer";
            case TOSCA_NODES + "WebServer.Apache":
                return "ApacheWebServer";
            case TOSCA_NODES + "WebApplication":
                return "WebApplication";
            case TOSCA_NODES + "DBMS":
                return "DBMS";
            case TOSCA_NODES + "DBMS.MySQL":
                return "MySQL";
            case TOSCA_NODES + "Database.MySQL":
                return "MySQLDatabase";
            case TOSCA_NODES + "ObjectStorage":
            case TOSCA_NODES + "BlockStorage":
            case TOSCA_NODES + "Network":
            case TOSCA_NODES + "WebApplication.WordPress":
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
    public static String getXmlArtifactType(String yamlArtifactType) throws NoBaseTypeMappingException {
        switch (yamlArtifactType) {
            case TOSCA_ARTIFACTS + "Root":
                return "RootArtifactType";
            case TOSCA_ARTIFACTS + "File":
                return "FileArtifact";
            case TOSCA_ARTIFACTS + "impl.Bash":
                return "ScriptArtifact";
            default:
                throw new NoBaseTypeMappingException(yamlArtifactType);
        }
    }
}
