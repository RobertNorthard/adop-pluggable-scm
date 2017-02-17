---
layout: docs
title: Updating Load_Cartridge job
permalink: /docs/updating-load-cartridge/
---

This section describes two ways how to update your ADOP Instance to inherit Pluggable SCM -

* **Automatically** - quick way
* **Manually** - more like a workaround

# Automatic way

* Re-run **Load_Platform** job to grab all the DSL jobs for the new **Load_Cartridge** job
* Update your Jenkins image to be the latest version on the Github repository in your compose and then re-initialise ADOP using **./adop compose init -m _name-of-your-machine_**
  - _Note:_ Just check the latest Jenkins image version [here](https://github.com/Accenture/adop-docker-compose/blob/master/docker-compose.yml#L203), it contains a couple of additional environment variables, plugins and some properties files in the right location. This change has been described below using _Manual_ way.
* Re-generate your Workspace and Project to ensure you have the latest version of **Load_Cartridge** job which should contain some additional fields, take a loot at picture below.


# Manual way

This basically describes what changes have been done on the Jenkins image and if you prefer not to use the latest version.

_Note: Please use your own values, below are just examples!_

* Get onto the ADOP/C host and add the following files in the respective locations on the Jenkins volume (you have to create all directories manually)

  - /var/jenkins_home/userContent/datastore/pluggable/scm/CartridgeLoader/adop-gerrit-1.loader.props

  ```
  loader.id=adop-gerrit-1
  gerrit.endpoint=gerrit
  gerrit.user=jenkins
  gerrit.port=29418
  gerrit.protocol=ssh
  gerrit.permissions.path=${PROJECT_NAME}/permissions
  gerrit.permissions.with_review.path=${PROJECT_NAME}/permissions-with-review
  ```
  - /var/jenkins_home/userContent/datastore/pluggable/scm/ScmProviders/adop-gerrit-1.ssh.props

  ```
  scm.loader.id=adop-gerrit-1
  scm.id=adop-gerrit-ssh
  scm.type=gerrit
  scm.code_review.enabled=true
  scm.protocol=ssh
  scm.port=29418
  scm.host=10.0.0.1
  scm.url=http://<IP>/gerrit/

  scm.gerrit.server.profile=ADOP Gerrit
  scm.gerrit.ssh.clone.user=jenkins
  ```
  - /var/jenkins_home/userContent/datastore/pluggable/scm/ScmProviders/adop-gerrit-1.http.props

  ```
  scm.loader.id=adop-gerrit-1
  scm.id=adop-gerrit-http
  scm.type=gerrit
  scm.code_review.enabled=true
  scm.protocol=http
  scm.port=8080
  scm.host=10.0.0.1
  scm.url=http://<IP>/gerrit/

  scm.gerrit.server.profile=ADOP Gerrit
  ```

* Add the following Environment Variables in _Manage Jenkins -> Configure System -> Global Properties_
  - PLUGGABLE_SCM_PROVIDER_PROPERTIES_PATH="/var/jenkins_home/userContent/datastore/pluggable/scm"
  - PLUGGABLE_SCM_PROVIDER_PATH="/var/jenkins_home/userContent/job_dsl_additional_classpath/"
* Install the _Active Choices_ plugin from _Manage Jenkins_ (You will probably need to restart Jenkins)
* Add your Jenkins private key (used to connect to Gerrit) as a secret file type credential with the ID *adop-jenkins-private*. This can be done in _Credentials_ section.
* Create a new Workspace and a new project (which should also have a new parameter), and you should have a new **Load_Cartridge** job ready to go! 
  - _Note:_ If there is nothing in drop-down menu, go in to Configure Job and just save it.

After all the steps, the **Load_Cartridge** job should look something like this -

![Updated Load Cartridge job](/pluggable-scm-library/images/docs/updated-load-cartridge.JPG)

---

More info:

- [About Pluggable SCM](https://kristapsm.github.io/adop-pluggable-scm/docs/about-pluggable-scm/)
- [Adding a Pluggable SCM](https://kristapsm.github.io/adop-cartridges-cookbook/docs/recipes/adding-a-pluggable-scm/)