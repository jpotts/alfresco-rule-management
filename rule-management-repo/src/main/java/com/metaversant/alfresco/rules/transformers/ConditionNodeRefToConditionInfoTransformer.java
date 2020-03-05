package com.metaversant.alfresco.rules.transformers;

import com.metaversant.alfresco.rules.model.ConditionInfo;
import org.alfresco.repo.action.ActionModel;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;

/**
 * Created by jpotts, Metaversant on 2/27/20.
 */
public class ConditionNodeRefToConditionInfoTransformer {
    public static ConditionInfo transform(NodeService nodeService, NodeRef nodeRef) {
        ConditionInfo conditionInfo = new ConditionInfo();
        conditionInfo.setNodeRef(nodeRef.toString());
        conditionInfo.setInvert((Boolean) nodeService.getProperty(nodeRef, ActionModel.PROP_CONDITION_INVERT));
        conditionInfo.setParameters(ParameterInfoListTransformer.transform(nodeService, nodeRef));
        return conditionInfo;
    }
}
