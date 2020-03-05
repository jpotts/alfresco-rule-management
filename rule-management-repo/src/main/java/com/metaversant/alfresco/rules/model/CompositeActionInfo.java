package com.metaversant.alfresco.rules.model;

import java.util.ArrayList;

/**
 * Created by jpotts, Metaversant on 2/27/20.
 */
public class CompositeActionInfo {
    private String nodeRef;
    private ArrayList<ActionInfo> actions;
    private ArrayList<ConditionInfo> conditions;

    public String getNodeRef() {
        return nodeRef;
    }

    public void setNodeRef(String nodeRef) {
        this.nodeRef = nodeRef;
    }

    public ArrayList<ActionInfo> getActions() {
        return actions;
    }

    public void setActions(ArrayList<ActionInfo> actions) {
        this.actions = actions;
    }

    public ArrayList<ConditionInfo> getConditions() {
        return conditions;
    }

    public void setConditions(ArrayList<ConditionInfo> conditions) {
        this.conditions = conditions;
    }
}
