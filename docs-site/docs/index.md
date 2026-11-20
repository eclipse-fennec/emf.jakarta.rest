---
layout: home

hero:
  name: Fennec Modelled REST
  text: Jakarta RS applications as models
  tagline: The REST interface is a model, the behaviour is an EMF invocation delegate. No resource classes, deployed through the OSGi Jakarta RS whiteboard.
  image:
    src: /fennec-logo.png
    alt: Eclipse Fennec logo
  actions:
    - theme: brand
      text: Get Started
      link: /guides/getting-started
    - theme: alt
      text: The Binding Model
      link: /guides/binding-model
    - theme: alt
      text: View on GitHub
      link: https://github.com/eclipse-fennec/emf.rest

features:
  - icon: 🧭
    title: Binding model
    details: Applications, resources and methods bind paths and HTTP methods to the EOperations of a domain model. Parameters come from path, query, header, cookie, form, body or the request context.
    link: /guides/binding-model
    linkText: The Binding Model
  - icon: 🔐
    title: Access control
    details: Access and roles are modelled on application, resource and method. Authentication stays an ordinary whiteboard extension that the model selects.
    link: /guides/security
    linkText: Access Control
  - icon: 🚀
    title: Deployment
    details: A RestApplication registered as an OSGi service becomes a Jakarta RS whiteboard application, with the service properties of model and deployment, next to compiled resources.
    link: /guides/deployment
    linkText: Deployment
  - icon: 📄
    title: OpenAPI
    details: One configuration brings up a complete application from an OpenAPI document. Schemas become the domain model, path operations become EOperations.
    link: /guides/openapi
    linkText: From an OpenAPI Document
---

## About Fennec Modelled REST

Fennec Modelled REST (`org.eclipse.fennec.rest`) interprets a REST binding model at runtime. It
builds the Jakarta RS resources programmatically, registers them as an application at the
Jakarta RS whiteboard and dispatches every request to the EMF invocation delegate of the bound
`EOperation`. The binding model is separate from the domain model, so the domain model stays
free of transport details.

A modelled application is an ordinary whiteboard application: extensions such as an
authentication filter attach to it, compiled resources can run next to modelled ones, and an
existing application can be moved to a model resource by resource.
