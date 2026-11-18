# Fennec Modelled REST (Spike)

Jakarta RS applications without resource classes: the REST interface is a model, the behaviour
is an EMF invocation delegate.

## How it works

```
greeting.rest (binding model)          greeting.ecore (domain model)
  RestApplication                         GreetingService
    RestResource  --target-->               greet(name, times)   <-- invocation delegate
      RestMethod  --operation-->                                      (OSGi service)
        ParameterBinding --parameter-->
```

1. A `RestApplication` model is registered as an OSGi service, e.g. by the `RestModelLoader`
   (factory PID `org.eclipse.fennec.rest.RestModel`, property `model.uri`).
2. `ModelledApplicationWhiteboard` interprets it into a `ModelledApplication` and registers that
   as a Jakarta RS whiteboard application (`emf=true`, so the Fennec codec handles EObject bodies).
3. `ModelledApplication` builds the resources with the Jersey programmatic resource API
   (`Resource.builder`, added through a `ModelProcessor`). This is the only Jersey-specific part.
4. Per request, `OperationInflector` resolves the arguments from the request, creates an instance
   of the target EClass and calls `eInvoke`. EMF dispatches to the invocation delegate.

The binding model is separate from the domain model on purpose: the domain model stays free of
transport details.

## Projects

| Project | Content |
|---|---|
| `org.eclipse.fennec.rest.model` | `model/rest.ecore`, the binding model |
| `org.eclipse.fennec.rest` | Interpreter and OSGi components |
| `org.eclipse.fennec.rest.tests` | OSGi integration test and example (`model/greeting.ecore`, `model/greeting.rest`, `GreetingDelegateFactory`) |

## Build

```bash
./gradlew build                                   # everything, including the OSGi test
./gradlew :org.eclipse.fennec.rest:test           # interpreter unit tests (no OSGi, no HTTP)
./gradlew :org.eclipse.fennec.rest.tests:testOSGi # over HTTP through the real whiteboard
```

## Response mapping

| Outcome of the operation | Response |
|---|---|
| Result | 200 (or the modelled `successStatus`) with the result as entity |
| No result | 204 (or the modelled `successStatus`) |
| Delegate throws a `WebApplicationException` | its response |
| Delegate throws anything else | 500 |
| Parameter literal cannot be converted | 400 |
| No invocation delegate registered for a dynamic operation | 503 |

## Not covered yet

- Request context for the delegate (`UriInfo`, `SecurityContext`, ...): a delegate only sees the
  target object and the arguments.
- The target EClass is instantiated per request and is stateless.
- Mandatory parameters are not enforced; an absent parameter without default arrives as `null`.
- Many-valued body parameters are rejected when the model is interpreted.
- EMF caches the delegate on the `EOperation`. A delegate factory that disappears is noticed
  (503), one that is replaced without a gap is not.
- A change of the binding model needs a new `RestApplication` service; it is not observed.
