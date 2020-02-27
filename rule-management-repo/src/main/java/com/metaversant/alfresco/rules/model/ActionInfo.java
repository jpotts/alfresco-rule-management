package com.metaversant.alfresco.rules.model;

import java.util.ArrayList;

/**
 * Created by jpotts, Metaversant on 2/27/20.
 */
public class ActionInfo {
    private ArrayList<ParameterInfo> parameters;

    public ArrayList<ParameterInfo> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<ParameterInfo> parameters) {
        this.parameters = parameters;
    }
}
