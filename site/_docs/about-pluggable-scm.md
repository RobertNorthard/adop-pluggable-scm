---
layout: docs
title: About Pluggable SCM Library
permalink: /docs/about-pluggable-scm/
---

In brief, Pluggable SCM allows user to use his own desired SCM provider, currently _BitBucket_ and _Gerrit_ are supported.

By enabling a standard interface, Java reflection can be used to return dynamic groovy closures in cartridge which would act the same as default DSL methods depending on the SCM provider.

There are two parts of ADOP that are affected by using this library:

* [Load_Cartridge job](https://accenture.github.io/adop-pluggable-scm/docs/updating-load-cartridge/)
* [Cartridge DSL code](https://accenture.github.io/adop-cartridges-cookbook/docs/recipes/adding-a-pluggable-scm/)

The _pluggable.scm_ package contains:

* _SCMProvider_ - an interface which defines the specification of an SCM provider
* _SCMProviderDataStore_ - an interface for SCM provider data store specification
* _SCMProviderFactory_ - an interface for SCM provider factory definition responsible for parsing the providers properties and instantiating the correct SCM provider
* _SCMProviderHandler_ - a class responsible for dispatching SCM provider requests to the correct SCM provider factory
* _SCMProviderInfo_ - annotation to mark SCM providers

More specific information can be found in [Javadocs](https://accenture.github.io/adop-pluggable-scm/groovydocs/)

# Using Pluggable SCM Library

To use this library you will need to create your own classes which then would implement above interfaces. For example

```
  public class GerritSCMProviderFactory implements SCMProviderFactory {}
```

Then use the factory method to return the instantiated SCM provider with the provided properties 

```
  public SCMProvider create(Properties scmProviderProperties) {}
```

from where your _GerritSCMProvider_ will read properties like :

* Host
* Port
* Protocol (https, http or ssh)
* User
* Endpoint etc.


The same way it should be done for example using _SCMProvider_ interface

```
  public class GerritSCMProvider implements SCMProvider {}
```

and proceed with defining your own classes for creating repositories etc. and defining _get_ and _trigger_ closures.

---

More info:

- [Updating Load_Cartridge job](https://accenture.github.io/adop-pluggable-scm/docs/updating-load-cartridge/)
- [Adding a Pluggable SCM](https://accenture.github.io/adop-cartridges-cookbook/docs/recipes/adding-a-pluggable-scm/)