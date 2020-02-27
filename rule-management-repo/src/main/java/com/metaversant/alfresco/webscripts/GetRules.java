package com.metaversant.alfresco.webscripts;

import com.metaversant.alfresco.rules.Utilities;
import com.metaversant.alfresco.rules.model.RuleInfo;
import com.metaversant.alfresco.rules.transformers.RuleNodeRefToRuleInfoTransformer;
import org.alfresco.model.ContentModel;
import org.alfresco.repo.rule.RuleModel;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.search.SearchService;
import org.apache.log4j.Logger;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jpotts, Metaversant on 2/27/20.
 */
public class GetRules extends DeclarativeWebScript {
    private Logger logger = Logger.getLogger(MoveRules.class);

    // Dependencies
    NodeService nodeService;
    SearchService searchService;

    @Override
    protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache) {
        Map<String, Object> model = new HashMap<>();

        String nodeRefStr = req.getParameter("nodeRef");
        model.put("nodeRef", nodeRefStr);

        NodeRef nodeRef = new NodeRef(nodeRefStr);
        if (!nodeService.exists(nodeRef)) {
            status.setCode(Status.STATUS_BAD_REQUEST);
            status.setMessage("Specified nodeRef does not exist: " + nodeRef);
            return model;
        }

        NodeRef ruleFolderNodeRef = Utilities.getRuleFolder(nodeService, nodeRef);
        if (ruleFolderNodeRef != null) {
            model.put("ruleFolderNodeRef", ruleFolderNodeRef.toString());
        }

        List<ChildAssociationRef> childNodeRefList = nodeService.getChildAssocs(ruleFolderNodeRef);
        ArrayList<RuleInfo> ruleInfoList = new ArrayList<>();
        for (ChildAssociationRef childRef : childNodeRefList) {
            NodeRef ruleNodeRef = childRef.getChildRef();
            if (!nodeService.getType(ruleNodeRef).equals(RuleModel.TYPE_RULE)) {
                continue;
            };
            RuleInfo ruleInfo = RuleNodeRefToRuleInfoTransformer.transform(nodeService, ruleNodeRef);
            ruleInfoList.add(ruleInfo);
        }
        model.put("rules", ruleInfoList);
        return model;
    }

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }
}
