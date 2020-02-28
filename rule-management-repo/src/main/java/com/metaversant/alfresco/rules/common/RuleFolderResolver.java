package com.metaversant.alfresco.rules.common;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.action.ActionModel;
import org.alfresco.repo.rule.RuleModel;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.QName;
import org.alfresco.service.namespace.RegexQNamePattern;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Given a node reference, finds the nearest rule folder nodeRef. If the nodeRef is a folder and that folder has one
 * or more rules, the rule folder is a child of the nodeRef. If the nodeRef is a child of the folder, the class
 * visits primary parents until the rule folder is found.
 *
 * As a reminder, the storage hierarchy of objects starting from a folder with rules set on it looks like this:
 *
 * cm:folder
 *   cm:systemfolder               <-- This class finds this object when starting from any point in this hierarchy.
 *     rule:rule
 *       act:compositeaction
 *         act:actioncondition
 *           act:actionparameter
 *         act:action
 *           act:actionparameter
 *
 * Created by jpotts, Metaversant on 2/28/20.
 */
public class RuleFolderResolver {
    private static Logger logger = Logger.getLogger(RuleFolderResolver.class);

    public static NodeRef resolve(NodeService nodeService, NodeRef nodeRef, boolean traverse) {
        List<ChildAssociationRef> assocs = nodeService.getChildAssocs(nodeRef, RuleModel.ASSOC_RULE_FOLDER, RegexQNamePattern.MATCH_ALL);
        // If we were passed a nodeRef that has an associated rule folder, that's what we want to return
        if (assocs.size() > 0) {
            logger.debug("nodeRef passed has a rule folder, returning the rule folder");
            return assocs.get(0).getChildRef();
        }

        // If the caller didn't tell us to traverse the hierarchy, looking for the rule folder, return null
        if (!traverse) return null;

        // If we were passed a nodeRef that contains rule:rule objects, that's what we want to return
        assocs = nodeService.getChildAssocs(nodeRef, ContentModel.ASSOC_CONTAINS, new RegexQNamePattern( RuleModel.RULE_MODEL_URI, "rules.*"));
        if (assocs.size() > 0) {
            logger.debug("nodeRef passed has rules as children so this must be a rule folder, returning it");
            return nodeRef;
        }

        // Otherwise, we are somewhere else in the hierarchy, so recursively search parents until we find the rule
        // then return its parent
        NodeRef ruleNodeRef = findRule(nodeService, nodeRef);
        if (ruleNodeRef == null) {
            logger.debug("Could not find instance of rule:rule");
            return null;
        } else {
            logger.debug("Found the instance of rule:rule, returning its parent");
            return nodeService.getPrimaryParent(ruleNodeRef).getParentRef();
        }
    }

    private static NodeRef findRule(NodeService nodeService, NodeRef nodeRef) {
        if (RuleModel.TYPE_RULE.equals(nodeService.getType(nodeRef))) {
            logger.debug("Found the rule object instance");
            return nodeRef;
        }
        // if the nodeRef is not one of the action types, we might not be where we think we are, so return null
        // otherwise, get the parent and recurse
        if (!isActionType(nodeService, nodeRef)) {
            logger.debug("Object is not an action type");
            return null;
        } else {
            logger.debug("Object is an action type, getting its parent and recursing");
            NodeRef parentNodeRef = nodeService.getPrimaryParent(nodeRef).getParentRef();
            return findRule(nodeService, parentNodeRef);
        }
    }

    private static boolean isActionType(NodeService nodeService, NodeRef nodeRef) {
        QName nodeType = nodeService.getType(nodeRef);
        return nodeType.equals(ActionModel.TYPE_ACTION) ||
                nodeType.equals(ActionModel.TYPE_ACTION_CONDITION) ||
                nodeType.equals(ActionModel.TYPE_ACTION_PARAMETER) ||
                nodeType.equals(ActionModel.TYPE_COMPOSITE_ACTION);
    }
}
