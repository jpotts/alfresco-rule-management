function main()
{

   var nodeRef = (page.url.args.nodeRef != null) ? page.url.args.nodeRef : "";


   var connector = remote.connect("alfresco"),
      remoteUrl = "/api/node/" + nodeRef.replace("://", "/") + "/ruleset/inheritrules/state",
      result = connector.get(remoteUrl);


   if (result.status == status.STATUS_OK)
   {
      var json = JSON.parse(result.response);
      model.inheritRules = !!(json.data.inheritRules == "true");
   }


  result = connector.get("/api/node/" + page.url.args.nodeRef.replace("://", "/") + "/ruleset");
  if (result.status == 200)
  {
     var ruleset = JSON.parse(result).data;
     if (!ruleset)
     {
        ruleset = {};
     }
     model.ruleset = ruleset;

     var linkedToNodeRef = ruleset.linkedToRuleSet;
     if (linkedToNodeRef)
     {
       model.isLinked = true;
     }
  }

   // Widget instantiation metadata...
   var rulesHeader = {
      id : "RulesHeader",
      name : "Alfresco.RulesHeader",
      options : {
         siteId : (page.url.templateArgs.site != null) ? page.url.templateArgs.site : "",
         nodeRef : nodeRef,
         inheritRules: model.inheritRules || "",
         isLinked: model.isLinked || false
      }
   };
   model.widgets = [rulesHeader];

}

main();
