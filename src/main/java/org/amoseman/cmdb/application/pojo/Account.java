package org.amoseman.cmdb.application.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Account {
    private String name;
    private String pass;

    public Account() {

    }

    public Account(String name, String pass) {
        this.name = name;
        this.pass = pass;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty
    public String getPass() {
        return pass;
    }

    @JsonProperty
    public void setPass(String pass) {
        this.pass = pass;
    }
}
