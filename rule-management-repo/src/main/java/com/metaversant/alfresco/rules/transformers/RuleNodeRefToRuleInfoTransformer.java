package com.metaversant.alfresco.rules.transformers;

import com.metaversant.alfresco.rules.model.RuleInfo;
import org.alfresco.model.ContentModel;
import org.alfresco.repo.rule.RuleModel;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jpotts, Metaversant on 2/27/20.
 */
public class RuleNodeRefToRuleInfoTransformer {
    public static RuleInfo transform(NodeService nodeService, NodeRef ruleNodeRef) {
        RuleInfo ruleInfo = new RuleInfo();
        ruleInfo.setNodeRef(ruleNodeRef.toString());
        ruleInfo.setCreated((Date) nodeService.getProperty(ruleNodeRef, ContentModel.PROP_CREATED));
        ruleInfo.setCreator((String) nodeService.getProperty(ruleNodeRef, ContentModel.PROP_CREATOR));
        ruleInfo.setModified((Date) nodeService.getProperty(ruleNodeRef, ContentModel.PROP_MODIFIED));
        ruleInfo.setModifier((String) nodeService.getProperty(ruleNodeRef, ContentModel.PROP_MODIFIER));
        ruleInfo.setTitle((String) nodeService.getProperty(ruleNodeRef, ContentModel.PROP_TITLE));
        ruleInfo.setDescription((String) nodeService.getProperty(ruleNodeRef, ContentModel.PROP_DESCRIPTION));
        ruleInfo.setDisabled((Boolean) nodeService.getProperty(ruleNodeRef, RuleModel.PROP_DISABLED));
        ruleInfo.setApplyToChildren((Boolean) nodeService.getProperty(ruleNodeRef, RuleModel.PROP_APPLY_TO_CHILDREN));
        ruleInfo.setAsync((Boolean) nodeService.getProperty(ruleNodeRef, RuleModel.PROP_EXECUTE_ASYNC));

        //noinspection unchecked
        ruleInfo.setRuleTypes((ArrayList<String>) nodeService.getProperty(ruleNodeRef, RuleModel.PROP_RULE_TYPE));

        ruleInfo.setCompositeActions(CompositeActionTransformer.transform(nodeService, ruleNodeRef));
        return ruleInfo;
    }
}
