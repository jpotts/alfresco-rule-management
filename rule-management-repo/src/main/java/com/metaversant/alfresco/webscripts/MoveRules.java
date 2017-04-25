package com.metaversant.alfresco.webscripts;

import org.alfresco.repo.rule.RuleModel;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.RegexQNamePattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This web script will move the rules that exist in a source folder to a target folder.
 *
 * Created by jpotts on 1/19/17.
 */
public class MoveRules extends DeclarativeWebScript {

    private NodeService nodeService;
    private SearchService searchService;

    @Override
    protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("message", "");

        Object content = req.parseContent();
        if (!(content instanceof JSONObject)) {
            status.setCode(Status.STATUS_BAD_REQUEST);
            status.setRedirect(true);
            return model;
        }

        JSONObject ruleInfo = (JSONObject) content;

        // check for required Data
        JSONArray moveList = null;
        try {
            if (ruleInfo.getString("ruleMoves") == null) {
                status.setCode(400, "List of rule moves is required");
                status.setRedirect(true);
                return model;
            } else {
                moveList = ruleInfo.getJSONArray("ruleMoves");
            }
        } catch (JSONException je) {
            status.setCode(500, "Problem parsing JSON: " + je.getMessage());
            status.setRedirect(true);
            return model;
        }

        for (int i = 0; i < moveList.length(); i++) {
            String sourcePath = null;
            String targetPath = null;
            try {
                JSONObject obj = moveList.getJSONObject(i);
                sourcePath = obj.getString("sourcePath");
                targetPath = obj.getString("targetPath");
                if (sourcePath == null || targetPath == null) {
                    continue;
                }
            } catch (JSONException e) {
                continue;
            }

            NodeRef ruleSource = getNodeRefFromPath(sourcePath); // "/app:company_home/cm:test/cm:rule-home"
            NodeRef ruleTarget = getNodeRefFromPath(targetPath);

            moveRules(ruleSource, ruleTarget);
        }

        return model;
    }

    private void moveRules(NodeRef ruleSource, NodeRef ruleTarget) {

        nodeService.addAspect(ruleTarget, RuleModel.ASPECT_RULES, null);
        nodeService.removeChild(ruleTarget, getRuleFolder(ruleTarget));

        NodeRef ruleFolder = getRuleFolder(ruleSource);
        nodeService.moveNode(ruleFolder, ruleTarget, RuleModel.ASSOC_RULE_FOLDER, RuleModel.ASSOC_RULE_FOLDER);

        nodeService.removeAspect(ruleSource, RuleModel.ASPECT_RULES);

    }

    private NodeRef getNodeRefFromPath(String path) {
        ResultSet rs = searchService.query(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE,
                SearchService.LANGUAGE_FTS_ALFRESCO,
                "PATH:\"" + path + "\"");
        if (rs.length() <= 0) {
            return null;
        }
        return rs.getNodeRef(0);
    }

    private NodeRef getRuleFolder(NodeRef target) {
        List<ChildAssociationRef> assocs = nodeService.getChildAssocs(target, RuleModel.ASSOC_RULE_FOLDER, RegexQNamePattern.MATCH_ALL);
        if (assocs.size() <= 0) {
            return null;
        }
        return assocs.get(0).getChildRef();
    }

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }

}
