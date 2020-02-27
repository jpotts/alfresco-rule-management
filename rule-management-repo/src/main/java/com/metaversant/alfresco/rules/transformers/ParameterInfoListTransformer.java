package com.metaversant.alfresco.rules.transformers;

import com.metaversant.alfresco.rules.model.ParameterInfo;
import org.alfresco.repo.action.ActionModel;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.RegexQNamePattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jpotts, Metaversant on 2/27/20.
 */
public class ParameterInfoListTransformer {
    public static ArrayList<ParameterInfo> transform(NodeService nodeService, NodeRef nodeRef) {
        List<ChildAssociationRef> childNodeRefList = nodeService.getChildAssocs(nodeRef, ActionModel.ASSOC_PARAMETERS, RegexQNamePattern.MATCH_ALL);
        ArrayList<ParameterInfo> parameterInfoList = new ArrayList<>();
        for (ChildAssociationRef childRef : childNodeRefList) {
            NodeRef paramNodeRef = childRef.getChildRef();
            ParameterInfo parameterInfo = ParameterNodeRefToParameterInfoTransformer.transform(nodeService, paramNodeRef);
            parameterInfoList.add(parameterInfo);
        }
        return parameterInfoList;
    }
}
