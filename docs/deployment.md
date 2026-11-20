# Deployment

A modelled application is deployed through the OSGi service registry: whoever registers a
`RestApplication` object as a service gets it deployed, and it is gone when the service goes.

1. A `RestApplication` model is registered as an OSGi service, e.g. by the `RestModelLoader`
   (factory PID `org.eclipse.fennec.rest.RestModel`, property `model.uri`).
2. `ModelledApplicationWhiteboard` interprets it into a `ModelledApplication` and registers that
   as a Jakarta RS whiteboard application (`emf=true`, so the Fennec codec handles EObject bodies).
3. `ModelledApplication` builds the resources with the Jersey programmatic resource API
   (`Resource.builder`, added through a `ModelProcessor`). This is the only Jersey-specific part.
4. Per request, `OperationInflector` resolves the arguments from the request, creates an instance
   of the target EClass and calls `eInvoke`. EMF dispatches to the invocation delegate.

## Registering an application

**From a file.** The `RestModelLoader` (factory PID `org.eclipse.fennec.rest.RestModel`) loads a
binding model and registers every application in it:

| Property | Meaning |
|---|---|
| `model.uri` | The URI of the binding model, e.g. an XMI file in a bundle |
| `resourceSet.target` | LDAP filter for the ResourceSet that loads the model. Use it to wait for the packages the model references, e.g. `(&(emf.name=rest)(emf.name=greeting))`. Default: `(emf.name=rest)` |
| anything else | becomes a service property of the application, see below |

**As a service.** Anything else can register a `RestApplication` under that interface itself:
code that builds the model, or the OpenAPI component ([From an OpenAPI document](openapi.md)).
A changed model needs a new service; changes of a registered model are not observed.

## Service properties of the application

The Jakarta RS application of a model carries, from weak to strong:

1. the `properties` of the model,
2. the service properties of the `RestApplication` service. With the `RestModelLoader` these are
   the properties of its configuration that are not its own (`model.uri`, `resourceSet.target`).
   They replace a modelled property of the same name, so one model can be deployed more than once
   with different values, and they follow when the service properties change,
3. what the whiteboard relies on: `osgi.jakartars.name` and `osgi.jakartars.application.base`
   are always the ones of the model, `emf=true` and `fennec.rest.modelled=<name>` are set, and
   `osgi.jakartars.extension.select` is what the model requires plus what the service requires.

The identity of the model service (id, PID, object class, scope, bundle, component) and private
properties (starting with a dot) are not passed on.

## Next to compiled resources

A modelled application is an ordinary whiteboard application, so compiled resources and
extensions can be added to it with `osgi.jakartars.application.select`.

With the name `.default` and the base path `/` it takes the place of the default application of
the whiteboard. Whiteboard resources that select no application then run in it without any
change, at the URLs they had. An existing application can be moved to a model resource by
resource that way (`DefaultApplicationIT`).

The access rule of the model does not apply to a compiled resource, see
[Access control](security.md#compiled-resources-are-not-under-the-rule-of-the-model).
