# Rule Management Utilities for Alfresco

This is an Alfresco hack-a-thon project intended to make it easier to manage folder rules.

## Description

* Folder rules are a great feature, much-loved by Alfresco users.
* Folder rules can be shared using “link to rule set” which is a lesser-known and -used feature
* When users have many rule sets, comprised of many rules, scattered across the repository, it is often a manually-intensive task to:
  * Duplicate rule sets from one folder to one or more folders
  * Convert “local” rules to “shared” rules and vice versa

## Goals

1. Create a REST API to help with these rule management tasks
1. Modify the Share rule configuration UI with additional rule management functionality
1. Create a new admin console page for bulk rule management

## API

The end points that exist at the moment are:

* POST /metaversant/link-rule
* POST /metaversant/share-rule
* POST /metaversant/move-rule

See src/test/resources for example JSON.

## Share UI

Currently the only change to the Share UI is a new button on the rule config page labeled "Convert to Shared Rule".

## Bootstrap

The module creates a new folder in the Data Dictionary called "Shared Rules". When a rule is converted from a locally-defined rule to a shared rule, it is moved to the Shared Rules folder.
