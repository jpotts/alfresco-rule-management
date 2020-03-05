package com.metaversant.alfresco.rules.transformers;

import com.metaversant.alfresco.rules.model.ActionInfo;
import org.alfresco.repo.action.ActionModel;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;

/**
 * Created by jpotts, Metaversant on 2/27/20.
 */
public class ActionNodeRefToActionInfoTransformer {
    public static ActionInfo transform(NodeService nodeService, NodeRef nodeRef) {
        ActionInfo actionInfo = new ActionInfo();
        actionInfo.setNodeRef(nodeRef.toString());
        actionInfo.setDefinitionName((String) nodeService.getProperty(nodeRef, ActionModel.PROP_DEFINITION_NAME));
        actionInfo.setParameters(ParameterInfoListTransformer.transform(nodeService, nodeRef));
        return actionInfo;
    }
}
