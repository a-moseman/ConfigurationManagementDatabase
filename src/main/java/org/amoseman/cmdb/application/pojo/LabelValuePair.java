package org.amoseman.cmdb.application.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LabelValuePair {
    private String label;
    private String value;

    public LabelValuePair() {

    }

    public LabelValuePair(String label, String value) {
        this.label = label;
        this.value = value;
    }

    @JsonProperty
    public String getLabel() {
        return label;
    }

    @JsonProperty
    public String getValue() {
        return value;
    }

    @JsonProperty
    public void setLabel(String label) {
        this.label = label;
    }

    @JsonProperty
    public void setValue(String value) {
        this.value = value;
    }
}
