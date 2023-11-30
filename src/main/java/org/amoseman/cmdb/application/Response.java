package org.amoseman.cmdb.application;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Response {
    private final String content;

    public Response(String content) {
        this.content = content;
    }

    @JsonProperty
    public String getContent() {
        return content;
    }
}
