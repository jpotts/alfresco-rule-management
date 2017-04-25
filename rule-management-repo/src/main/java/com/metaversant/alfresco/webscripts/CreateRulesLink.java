package com.metaversant.alfresco.webscripts;

import com.metaversant.alfresco.rules.Utilities;
import org.alfresco.model.ContentModel;
import org.alfresco.repo.policy.BehaviourFilter;
import org.alfresco.repo.rule.RuleModel;
import org.alfresco.repo.site.SiteModel;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.DynamicNamespacePrefixResolver;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
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
 * This web script will create a link in the folder specified to the specified rule folder.
 *
 * Created by jpotts on 1/19/17.
 */
public class CreateRulesLink extends DeclarativeWebScript {

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
        String ruleHomePath = null;
        List<String> targetPathList = new ArrayList<String>();
        try {
            if (ruleInfo.getString("ruleHomePath") == null) {
                status.setCode(400, "Rule home path is required");
                status.setRedirect(true);
                return model;
            } else {
                ruleHomePath = ruleInfo.getString("ruleHomePath");
            }

            if (ruleInfo.getJSONArray("targetPaths") == null) {
                status.setCode(400, "A list of target paths is required");
                status.setRedirect(true);
                return model;
            } else {
                JSONArray jsonArray = ruleInfo.getJSONArray("targetPaths");
                for (int i=0; i < jsonArray.length(); i++){
                    targetPathList.add(jsonArray.get(i).toString());
                }
            }
        } catch (JSONException je) {
            status.setCode(500, "Problem parsing JSON: " + je.getMessage());
            status.setRedirect(true);
            return model;
        }

        NodeRef ruleHome = Utilities.getNodeRefFromPath(searchService, ruleHomePath); // "/app:company_home/cm:test/cm:rule-home"

        for (String targetPath : targetPathList) {
            NodeRef nodeRef = Utilities.getNodeRefFromPath(searchService, targetPath); // "/app:company_home/cm:test/cm:rule-child-2"
            Utilities.linkToRuleFolder(nodeService, ruleHome, nodeRef);
        }

        return model;
    }

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }

}
