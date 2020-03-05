package com.metaversant.alfresco.rules.model;

import java.util.ArrayList;

/**
 * Created by jpotts, Metaversant on 2/27/20.
 */
public class ConditionInfo {
    private String nodeRef;
    private boolean invert;
    private ArrayList<ParameterInfo> parameters;

    public String getNodeRef() {
        return nodeRef;
    }

    public void setNodeRef(String nodeRef) {
        this.nodeRef = nodeRef;
    }

    public boolean isInvert() {
        return invert;
    }

    public void setInvert(boolean invert) {
        this.invert = invert;
    }

    public ArrayList<ParameterInfo> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<ParameterInfo> parameters) {
        this.parameters = parameters;
    }
}
