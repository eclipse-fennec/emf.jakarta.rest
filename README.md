# Fennec Modelled REST

Jakarta RS applications without resource classes: the REST interface is a model, the behaviour
is an EMF invocation delegate.

```
greeting.rest (binding model)          greeting.ecore (domain model)
  RestApplication                         GreetingService
    RestResource  --target-->               greet(name, times)   <-- invocation delegate
      RestMethod  --operation-->                                      (OSGi service)
        ParameterBinding --parameter-->
```

A `RestApplication` model that is registered as an OSGi service is interpreted and deployed as
an ordinary application at the Jakarta RS whiteboard. Per request the arguments are resolved
from the request and the bound `EOperation` is invoked; EMF dispatches to its invocation
delegate. The resources are built with the Jersey programmatic resource API, the only
Jersey-specific part.

## Documentation

| Guide | Content |
|---|---|
| [Getting started](docs/getting-started.md) | The smallest application, step by step |
| [The binding model](docs/binding-model.md) | Parameter sources, request context, response mapping |
| [Access control](docs/security.md) | `access` and `rolesAllowed`, authentication as an extension |
| [Deployment](docs/deployment.md) | Applications as services, the model loader, service properties, compiled resources |
| [From an OpenAPI document](docs/openapi.md) | A complete application from one configuration |
| [Not covered yet](docs/limitations.md) | Known gaps |

## Projects

| Project | Content |
|---|---|
| `org.eclipse.fennec.rest.model` | `model/rest.ecore`, the binding model |
| `org.eclipse.fennec.rest` | Interpreter and OSGi components |
| `org.eclipse.fennec.rest.openapi` | Derives an application from an OpenAPI document |
| `org.eclipse.fennec.rest.openapi.qvto` | The same derivation as a QVT-O transformation (Fennec M2X) |
| `org.eclipse.fennec.rest.workspace.library` | bnd workspace library `fennecRest`: a repository with the runtime and its dependencies |
| `org.eclipse.fennec.rest.tests` | OSGi integration test and example (`model/greeting.ecore`, `model/greeting.rest`, `GreetingDelegateFactory`) |

## Build

```bash
./gradlew build                                   # everything, including the OSGi test
./gradlew :org.eclipse.fennec.rest:test           # interpreter unit tests (no OSGi, no HTTP)
./gradlew :org.eclipse.fennec.rest.tests:testOSGi # over HTTP through the real whiteboard
```

## Dependencies and licenses

`DEPENDENCIES` is generated with `tools/dash-licenses.sh` (`bnd repo deps` plus the Eclipse Dash
License Tool). Regenerate it after a change of the dependencies.
