package com.metaversant.alfresco.rules.webscripts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaversant.alfresco.rules.common.Utilities;
import com.metaversant.alfresco.rules.model.ParentNodeRef;
import com.metaversant.alfresco.rules.model.RuleInfo;
import com.metaversant.alfresco.rules.transformers.RuleNodeRefToRuleInfoTransformer;
import org.alfresco.repo.rule.RuleModel;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.search.SearchService;
import org.apache.log4j.Logger;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jpotts, Metaversant on 2/27/20.
 */
public class GetRules extends AbstractWebScript {
    private Logger logger = Logger.getLogger(GetRules.class);

    // Dependencies
    NodeService nodeService;
    SearchService searchService;

    public void execute(WebScriptRequest req, WebScriptResponse res) {
        res.setContentType("application/json");
        Map<String, Object> response = new HashMap<>();

        // Grab the nodeRef from the request parameter
        String nodeRefStr = req.getParameter("nodeRef");
        String traverseStr = req.getParameter("traverse");
        boolean traverse = false;
        if (traverseStr != null && traverseStr.length() > 0 && "true".equals(traverseStr.toLowerCase())) {
            traverse = true;
        }

        // Make sure the specified nodeRef exists before continuing
        NodeRef nodeRef = new NodeRef(nodeRefStr);
        if (!nodeService.exists(nodeRef)) {
            res.setStatus(Status.STATUS_NOT_FOUND);
            return;
        }

        // Get the rule folder for this nodeRef
        NodeRef ruleFolderNodeRef = Utilities.getRuleFolder(nodeService, nodeRef, traverse);
        if (ruleFolderNodeRef == null) {
            // If there is no rule folder there are no rules
            response.put("nodeRef", nodeRef.toString());
            response.put("rules", Collections.EMPTY_LIST);
        } else {
            response.put("ruleFolderNodeRef", ruleFolderNodeRef.toString());

            // For each rule folder, convert the rule folder nodeRef into a POJO and add it to the list
            List<ChildAssociationRef> childNodeRefList = nodeService.getChildAssocs(ruleFolderNodeRef);
            ArrayList<RuleInfo> ruleInfoList = new ArrayList<>();
            for (ChildAssociationRef childRef : childNodeRefList) {
                NodeRef ruleNodeRef = childRef.getChildRef();
                if (!nodeService.getType(ruleNodeRef).equals(RuleModel.TYPE_RULE)) {
                    continue;
                }
                RuleInfo ruleInfo = RuleNodeRefToRuleInfoTransformer.transform(nodeService, ruleNodeRef);
                ruleInfoList.add(ruleInfo);
            }

            // Set the model and return
            response.put("rules", ruleInfoList);

            // Get the folder on which these rules are set (It could be different than the nodeRef requested
            NodeRef folderNodeRef = nodeService.getPrimaryParent(ruleFolderNodeRef).getParentRef();
            response.put("nodeRef", folderNodeRef.toString());

            // Determine if this folder ignores rules inherited from its parent
            boolean ignoreInheritedRules = nodeService.hasAspect(folderNodeRef, RuleModel.ASPECT_IGNORE_INHERITED_RULES);
            response.put("ignoreInheritedRules", ignoreInheritedRules);

            // Find list of the rule folder node ref's parents. A rule folder has multiple parents when the rule
            // folder is being linked to this folder instead of having the rules defined locally to this folder.
            ArrayList<ParentNodeRef> ruleFolderParents = new ArrayList<>();
            List<ChildAssociationRef> parentAssocsInfos = nodeService.getParentAssocs(ruleFolderNodeRef);
            for (ChildAssociationRef parentAssoc : parentAssocsInfos) {
                ParentNodeRef parent = new ParentNodeRef();
                parent.setPrimary(parentAssoc.isPrimary());
                parent.setNodeRef(parentAssoc.getParentRef().toString());
                ruleFolderParents.add(parent);
            }
            response.put("ruleFolderParents", ruleFolderParents);
            response.put("isLinkedRule", parentAssocsInfos.size() > 1);
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            res.getWriter().write(mapper.writeValueAsString(response));
        } catch (IOException ioe) {
            logger.error(ioe.getMessage());
        }
    }

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }
}
