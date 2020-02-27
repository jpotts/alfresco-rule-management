{
    "nodeRef": "${nodeRef}",
    "ruleFolderNodeRef": "${ruleFolderNodeRef}",
    "rules": [
    <#list rules as rule>
        {
            "nodeRef": "${rule.nodeRef}",
            "ruleTypes": [
                <#list rule.ruleTypes as ruleType>
                    "${ruleType}"<#if ruleType_has_next>,</#if>
                </#list>
            ],
            "modifier": "${rule.modifier}",
            "modified": "${rule.modified?datetime}",
            "creator": "${rule.creator}",
            "created": "${rule.created?datetime}",
            "title": "${rule.title}",
            "description": "${rule.description}",
            "disabled": "${rule.disabled?c}",
            "applyToChildren": "${rule.applyToChildren?c}",
            "async": "${rule.async?c}",
            "compositeActions": [
                <#list rule.compositeActions as cAction>
                    {
                    "actions": [
                    <#list cAction.actions as action>
                        {
                            "parameters": [
                            <#list action.parameters as parameter>
                                {
                                    "name": "${parameter.name}",
                                    <#if parameter.value?is_enumerable>
                                        "value": "${parameter.value?join(", ")}"
                                        <#else>
                                        "value": "${parameter.value}"
                                    </#if>
                                }<#if parameter_has_next>,</#if>
                            </#list>
                            ]
                        }<#if action_has_next>,</#if>
                    </#list>
                    ],
                    "conditions": [
                    <#list cAction.conditions as condition>
                        {
                            "invert": "${condition.invert?c}",
                            "parameters": [
                            <#list condition.parameters as parameter>
                                {
                                    "name": "${parameter.name}",
                                    <#if parameter.value?is_enumerable>
                                        "value": "${parameter.value?join(", ")}"
                                    <#else>
                                        "value": "${parameter.value}"
                                    </#if>
                                }<#if parameter_has_next>,</#if>
                            </#list>
                            ]
                        }<#if condition_has_next>,</#if>
                    </#list>
                    ]
                    }<#if cAction_has_next>,</#if>
                </#list>
            ]
        }<#if rule_has_next>,</#if>
    </#list>
    ]
}