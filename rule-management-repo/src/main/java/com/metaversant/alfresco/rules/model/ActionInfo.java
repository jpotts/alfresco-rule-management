package com.metaversant.alfresco.rules.model;

import java.util.ArrayList;

/**
 * POJO that represents what Alfresco models as an act:action.
 *
 * Created by jpotts, Metaversant on 2/27/20.
 */
public class ActionInfo {
    private String definitionName;
    private ArrayList<ParameterInfo> parameters;

    public String getDefinitionName() {
        return definitionName;
    }

    public void setDefinitionName(String definitionName) {
        this.definitionName = definitionName;
    }

    public ArrayList<ParameterInfo> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<ParameterInfo> parameters) {
        this.parameters = parameters;
    }
}
