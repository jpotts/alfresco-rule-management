package com.metaversant.alfresco.rules.transformers;

import com.metaversant.alfresco.rules.model.ActionInfo;
import com.metaversant.alfresco.rules.model.CompositeActionInfo;
import com.metaversant.alfresco.rules.model.ConditionInfo;
import org.alfresco.repo.action.ActionModel;
import org.alfresco.repo.rule.RuleModel;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.RegexQNamePattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jpotts, Metaversant on 2/27/20.
 */
public class CompositeActionTransformer {
    public static ArrayList<CompositeActionInfo> transform(NodeService nodeService, NodeRef nodeRef) {
        ArrayList<CompositeActionInfo> compositeActionInfoList = new ArrayList<>();
        List<ChildAssociationRef> childAssocList = nodeService.getChildAssocs(nodeRef, RuleModel.ASSOC_ACTION, RegexQNamePattern.MATCH_ALL);
        for (ChildAssociationRef childRef : childAssocList) {
            CompositeActionInfo compositeActionInfo = new CompositeActionInfo();
            NodeRef caActionNodeRef = childRef.getChildRef();

            // Grab the actions for this composite action
            List<ChildAssociationRef> actChildRefList = nodeService.getChildAssocs(caActionNodeRef, ActionModel.ASSOC_ACTIONS, RegexQNamePattern.MATCH_ALL);
            ArrayList<ActionInfo> actionInfoList = new ArrayList<>();
            for (ChildAssociationRef actChildRef : actChildRefList) {
                NodeRef actNodeRef = actChildRef.getChildRef();
                ActionInfo actionInfo = ActionNodeRefToActionInfoTransformer.transform(nodeService, actNodeRef);
                actionInfoList.add(actionInfo);
            }
            compositeActionInfo.setActions(actionInfoList);

            // Grab the conditions for this composite action
            List<ChildAssociationRef> condChildRefList = nodeService.getChildAssocs(caActionNodeRef, ActionModel.ASSOC_CONDITIONS, RegexQNamePattern.MATCH_ALL);
            ArrayList<ConditionInfo> conditionInfoList = new ArrayList<>();
            for (ChildAssociationRef condChildRef : condChildRefList) {
                NodeRef condNodeRef = condChildRef.getChildRef();
                ConditionInfo condInfo = ConditionNodeRefToConditionInfoTransformer.transform(nodeService, condNodeRef);
                conditionInfoList.add(condInfo);
            }
            compositeActionInfo.setConditions(conditionInfoList);
            compositeActionInfoList.add(compositeActionInfo);
        }
        return compositeActionInfoList;
    }
}
