# Rule Management Utilities for Alfresco

This is an Alfresco hack-a-thon project intended to make it easier to manage folder rules.

See it in action [here](https://www.youtube.com/watch?v=8L3siJu_T0A).

This code started out as a one-off utility Metaversant built for a client. They allowed us to open source the code provided it would always be available for the community. At BeeCon 2017 Hack-a-thon, the following team kickstarted the project:

* Francisco Guariba
* Peter Lesty
* Jeff Potts

Hopefully you'll find it useful. There is a lot of functionality remaining that would make it even better. Pull requests are welcome!

## Description

* Folder rules are a great feature, much-loved by Alfresco users.
* Folder rules can be shared using “link to rule set” which is a lesser-known and -used feature
* When users have many rule sets, comprised of many rules, scattered across the repository, it is often a manually-intensive task to:
  * Duplicate rule sets from one folder to one or more folders
  * Convert “local” rules to “shared” rules and vice versa
  * Link a shared rule into one or more folders

## Goals

1. Create a REST API to help with these rule management tasks
1. Modify the Share rule configuration UI with additional rule management functionality
1. Create a new admin console page for bulk rule management
1. Provide an API that returns a JSON representation of a rule

All of these goals are works-in-progress, although enough has been done already to make this add-on useful in its current state.

## Security Considerations

As the module currently exists, no special permissions are required to make a rule shared. And no special permissions are required to link a shared rule to a folder using the API shown below. This means that anyone can create a rule on any folder if they know the path of that folder. When the rule runs it will run using the permissions of the person invoking the action, but still, you may want to consider whether or not you are willing to let people create rules in folders they may not control.

We can fix this by adding a new permission that can be assigned to a person or group, establishing that they have write access to the Shared rules folder. The Share customizations and the web scripts that implement the RESTful API would then check for that permission. But this is still a to do.

## API

The end points that exist at the moment are:

* POST /metaversant/link-rule
* POST /metaversant/share-rule
* POST /metaversant/move-rule
* GET /metaversant/get-rules?nodeRef={nodeRef}&traverse={traverseFlag}

See src/test/resources for example JSON.

### Get Rules

The get-rules endpoint is useful when you have scripts or other applications
that need to know what rules are set on a folder, including their actions and
parameters. The end point expects a node reference and will return back an array
of objects describing the rules on that folder.

If a node reference is provided that is "below" the rule folder, such as what
might happen if you provide a reference to one of the folders that actually
describes the rule, for example, you can use the traverse flag to tell the API
to walk up the folder hierarchy until it finds the end-user folder on which the
rules are actually set. If you don't specify traverse=true then no traversal
will happen and the response will only contain the rules that are actually
defined for the node reference provided.

## Share UI

Currently the only change to the Share UI is a new button on the rule config page labeled "Convert to Shared Rule".

## Bootstrap

The module creates a new folder in the Data Dictionary called "Shared Rules". When a rule is converted from a locally-defined rule to a shared rule, it is moved to the Shared Rules folder.
