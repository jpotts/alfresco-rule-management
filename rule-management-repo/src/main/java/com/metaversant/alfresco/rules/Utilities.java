package com.metaversant.alfresco.rules;

import com.metaversant.alfresco.webscripts.MoveRules;
import org.alfresco.repo.rule.RuleModel;
import org.alfresco.repo.site.SiteModel;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.DynamicNamespacePrefixResolver;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.RegexQNamePattern;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by jpotts, Metaversant on 4/25/17.
 */
public class Utilities {
    private static Logger logger = Logger.getLogger(Utilities.class);

    public static final String SHARED_RULES_PATH = "/app:company_home/app:dictionary/cm:Shared_x0020_Rules";

    public static void linkToRuleFolder(NodeService nodeService, NodeRef ruleHome, NodeRef nodeRef) {
        nodeService.addAspect(nodeRef, RuleModel.ASPECT_RULES, null);
        nodeService.removeChild(nodeRef, getRuleFolder(nodeService, nodeRef));
        nodeService.addChild(nodeRef, getRuleFolder(nodeService, ruleHome), RuleModel.ASSOC_RULE_FOLDER, RuleModel.ASSOC_RULE_FOLDER);
    }

    public static NodeRef getNodeRefFromPath(SearchService searchService, String path) {
        ResultSet rs = searchService.query(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE,
                SearchService.LANGUAGE_FTS_ALFRESCO,
                "PATH:\"" + path + "\"");
        if (rs.length() <= 0) {
            return null;
        }
        return rs.getNodeRef(0);
    }

    public static NodeRef getSharedRulesFolder(SearchService searchService) {
        return getNodeRefFromPath(searchService, SHARED_RULES_PATH);
    }

    public static NodeRef getRuleFolder(NodeService nodeService, NodeRef target) {
        List<ChildAssociationRef> assocs = nodeService.getChildAssocs(target, RuleModel.ASSOC_RULE_FOLDER, RegexQNamePattern.MATCH_ALL);
        if (assocs.size() <= 0) {
            return null;
        }
        return assocs.get(0).getChildRef();
    }

    public static void moveRules(NodeService nodeService, NodeRef ruleSource, NodeRef ruleTarget) {
        logger.debug("Adding rules aspect");
        nodeService.addAspect(ruleTarget, RuleModel.ASPECT_RULES, null);
        nodeService.removeChild(ruleTarget, Utilities.getRuleFolder(nodeService, ruleTarget));

        NodeRef ruleFolder = Utilities.getRuleFolder(nodeService, ruleSource);
        logger.debug("Got rule folder");
        nodeService.moveNode(ruleFolder, ruleTarget, RuleModel.ASSOC_RULE_FOLDER, RuleModel.ASSOC_RULE_FOLDER);
        logger.debug("Moved node");
        nodeService.removeAspect(ruleSource, RuleModel.ASPECT_RULES);
        logger.debug("Removed aspect");
    }

    public static DynamicNamespacePrefixResolver getNamespaceResolver() {
        DynamicNamespacePrefixResolver resolver = new DynamicNamespacePrefixResolver(null);
        resolver.registerNamespace(NamespaceService.CONTENT_MODEL_PREFIX, NamespaceService.CONTENT_MODEL_1_0_URI);
        resolver.registerNamespace(NamespaceService.APP_MODEL_PREFIX, NamespaceService.APP_MODEL_1_0_URI);
        resolver.registerNamespace(SiteModel.SITE_MODEL_PREFIX, SiteModel.SITE_MODEL_URL);
        return resolver;
    }
}
