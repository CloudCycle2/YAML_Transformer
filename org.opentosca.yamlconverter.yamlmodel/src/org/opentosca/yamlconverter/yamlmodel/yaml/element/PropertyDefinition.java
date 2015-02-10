package org.opentosca.yamlconverter.yamlmodel.yaml.element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Sebi
 */
public class PropertyDefinition extends YAMLElement {

    private String type = "";
    private String defaultValue = "";
    private boolean required = false;
    private List<Map<String, String>> constraints = new ArrayList<Map<String, String>>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (type != null) {
            this.type = type;
        }
    }

    public String getDefault() {
        return defaultValue;
    }

    public void setDefault(String defaultValue) {
        if (defaultValue != null) {
            this.defaultValue = defaultValue;
        }
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public List<Map<String, String>> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<Map<String, String>> constraints) {
        if (constraints != null) {
            this.constraints = constraints;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PropertyDefinition that = (PropertyDefinition) o;

        if (required != that.required) return false;
        if (!constraints.equals(that.constraints)) return false;
        if (!defaultValue.equals(that.defaultValue)) return false;
        if (!type.equals(that.type)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + defaultValue.hashCode();
        result = 31 * result + (required ? 1 : 0);
        result = 31 * result + constraints.hashCode();
        return result;
    }
}
