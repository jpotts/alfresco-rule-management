package com.metaversant.alfresco.rules.model;

/**
 * Created by jpotts, Metaversant on 2/27/20.
 */
public class ParameterInfo {
    private String nodeRef;
    private String name;
    private String value;

    public String getNodeRef() {
        return nodeRef;
    }

    public void setNodeRef(String nodeRef) {
        this.nodeRef = nodeRef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
