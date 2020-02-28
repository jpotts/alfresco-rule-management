package com.metaversant.alfresco.rules.webscripts;

import com.metaversant.alfresco.rules.common.Utilities;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.search.SearchService;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * This web script will move the rules that exist in a source folder to a target folder.
 *
 * Created by jpotts on 1/19/17.
 */
public class MoveRules extends DeclarativeWebScript {

    private NodeService nodeService;
    private SearchService searchService;

    private Logger logger = Logger.getLogger(MoveRules.class);

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

            NodeRef ruleSource = Utilities.getNodeRefFromPath(searchService, sourcePath); // "/app:company_home/cm:test/cm:rule-home"
            NodeRef ruleTarget = Utilities.getNodeRefFromPath(searchService, targetPath);

            Utilities.moveRules(nodeService, ruleSource, ruleTarget);
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
