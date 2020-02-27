package com.metaversant.alfresco.rules.model;

import java.util.ArrayList;

/**
 * Created by jpotts, Metaversant on 2/27/20.
 */
public class CompositeActionInfo {
    private ArrayList<ActionInfo> actions;
    private ArrayList<ConditionInfo> conditions;

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
