package com.metaversant.alfresco.rules.model;

import java.io.Serializable;

/**
 * Created by jpotts, Metaversant on 2/27/20.
 */
public class ParameterInfo {
    private String name;
    private Serializable value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Serializable getValue() {
        return value;
    }

    public void setValue(Serializable value) {
        this.value = value;
    }
}
