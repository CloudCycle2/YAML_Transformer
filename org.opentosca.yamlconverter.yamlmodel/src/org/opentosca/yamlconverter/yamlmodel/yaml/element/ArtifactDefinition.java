package org.opentosca.yamlconverter.yamlmodel.yaml.element;

/**
 * Currently not used, but should be used in the future for type definitions!
 * @author Sebi
 */
public class ArtifactDefinition extends YAMLElement {

    private String type = "";
    private String mime_type = "";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (type != null) {
            this.type = type;
        }
    }

    public String getMime_type() {
        return mime_type;
    }

    public void setMime_type(String mime_type) {
        if (mime_type != null) {
            this.mime_type = mime_type;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ArtifactDefinition that = (ArtifactDefinition) o;

        if (!mime_type.equals(that.mime_type)) return false;
        if (!type.equals(that.type)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + mime_type.hashCode();
        return result;
    }
}
