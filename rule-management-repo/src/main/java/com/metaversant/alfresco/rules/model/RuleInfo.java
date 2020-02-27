package com.metaversant.alfresco.rules.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jpotts, Metaversant on 2/27/20.
 */
public class RuleInfo {
    private String nodeRef;
    private ArrayList<String> ruleTypes;
    private String modifier;
    private Date modified;
    private String creator;
    private Date created;
    private String title;
    private String description;
    private boolean disabled;
    private boolean applyToChildren;
    private boolean async;
    private ArrayList<CompositeActionInfo> compositeActions;

    public String getNodeRef() {
        return nodeRef;
    }

    public void setNodeRef(String nodeRef) {
        this.nodeRef = nodeRef;
    }

    public ArrayList<String> getRuleTypes() {
        return ruleTypes;
    }

    public void setRuleTypes(ArrayList<String> ruleTypes) {
        this.ruleTypes = ruleTypes;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isApplyToChildren() {
        return applyToChildren;
    }

    public void setApplyToChildren(boolean applyToChildren) {
        this.applyToChildren = applyToChildren;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public ArrayList<CompositeActionInfo> getCompositeActions() {
        return compositeActions;
    }

    public void setCompositeActions(ArrayList<CompositeActionInfo> compositeActions) {
        this.compositeActions = compositeActions;
    }
}
