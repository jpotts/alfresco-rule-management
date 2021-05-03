package com.metaversant.alfresco.rules.model;

public class ParentNodeRef {
    private String nodeRef;
    private boolean isPrimary;

    public String getNodeRef() {
        return nodeRef;
    }

    public void setNodeRef(String nodeRef) {
        this.nodeRef = nodeRef;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }
}
