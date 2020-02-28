package com.metaversant.alfresco.rules.webscripts;

import com.metaversant.alfresco.rules.common.Utilities;
import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This web script will turn a local rule into a shared rule.
 *
 * Created by jpotts on 1/19/17.
 */
public class ConvertToSharedRule extends DeclarativeWebScript {

    private NodeService nodeService;
    private SearchService searchService;

    private Logger logger = Logger.getLogger(ConvertToSharedRule.class);

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
        String sourceNodeRefString = null;
        String sourceFolderName = null;
        try {
            if (ruleInfo.getString("nodeRef") == null) {
                status.setCode(400, "Rule folder nodeRef is required");
                status.setRedirect(true);
                return model;
            } else {
                sourceNodeRefString = ruleInfo.getString("nodeRef");
            }
            
            if (ruleInfo.getString("sourceFolderName") != null) {
            	sourceFolderName = ruleInfo.getString("sourceFolderName");
            } else {
            	//sourceFolderName = "";
            }
            
        } catch (JSONException je) {
            status.setCode(500, "Problem parsing JSON: " + je.getMessage());
            status.setRedirect(true);
            return model;
        }
        logger.debug("Converting to shared: " + sourceNodeRefString);

        // convert the nodeRef argument to an actual nodeRef
        NodeRef sourceNodeRef = new NodeRef(sourceNodeRefString);
        if (!nodeService.exists(sourceNodeRef)) {
            status.setCode(404, "Source nodeRef not found: " + sourceNodeRefString);
            status.setRedirect(true);
            return model;
        }

        // grab the shared rules folder
        NodeRef sharedRulesFolder = Utilities.getSharedRulesFolder(searchService);
        logger.debug("Got shared rules folder");

        // get the name of the source folder and use that as the name of the shared rule set
        if (sourceFolderName == null)
        {
        	sourceFolderName = (String) nodeService.getProperty(sourceNodeRef, ContentModel.PROP_NAME);
        }
        // TODO: Need to catch the case when this folder name already exists
        
        Map<QName, Serializable> props = new HashMap<QName, Serializable>();
        props.put(ContentModel.PROP_NAME, sourceFolderName);

        ChildAssociationRef childRef = nodeService.createNode(
                sharedRulesFolder,
                ContentModel.ASSOC_CONTAINS,
                QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI, sourceFolderName),
                ContentModel.TYPE_FOLDER,
                props
        );
        NodeRef ruleTarget = childRef.getChildRef();
        
        logger.debug("Created new folder in Shared Rules for move");

        Utilities.moveRules(nodeService, sourceNodeRef, ruleTarget);
        logger.debug("Move complete");

        // Create a link from the new Shared Rule to the existing rule folder
        Utilities.linkToRuleFolder(nodeService, ruleTarget, sourceNodeRef);
        logger.debug("Link complete");

        return model;
    }

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }

}
