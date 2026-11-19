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
| `org.eclipse.fennec.rest.openapi` | Derives an application from an OpenAPI document |
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
| A Jakarta RS `Response` | that response, unchanged |
| Any other result | 200 (or the modelled `successStatus`) with the result as entity |
| No result | 204 (or the modelled `successStatus`) |
| Delegate throws a `WebApplicationException` | its response |
| Delegate throws anything else | 500 |
| Parameter literal cannot be converted | 400 |
| No invocation delegate registered for a dynamic operation | 503 |

The rule of thumb is the one of Jakarta RS itself: what annotations can express in Java is
modelled on the `RestMethod`; everything beyond that (status by outcome, response headers,
`Location`) is done by returning a `Response` from the delegate. In a dynamic model the return
type is an `EDataType` with the instance class name `jakarta.ws.rs.core.Response`.

## Parameter sources

`PATH`, `QUERY`, `HEADER`, `COOKIE`, `MATRIX`, `FORM` and `BODY` correspond to the Jakarta RS
parameter annotations, `defaultValue` to `@DefaultValue` and `encoded` to `@Encoded` (the value
arrives without URL decoding; `PATH`, `QUERY`, `MATRIX`, `FORM`). `FORM` and `BODY` cannot
be combined on one method.

## Request context

A `ParameterBinding` can take its value from the request context instead of the request data:

| Source | Value |
|---|---|
| `PRINCIPAL` | The authenticated caller: its name for an `EString` parameter, the principal object otherwise |
| `CONTEXT` | A Jakarta RS object, selected by the instance class name of the parameter's data type: `SecurityContext`, `UriInfo`, `HttpHeaders`, `Request`, `ContainerRequestContext` |

A mandatory parameter (`lowerBound >= 1`) that is missing answers 400, a missing mandatory
principal 401.

## Access control

`RestApplication`, `RestResource` and `RestMethod` have `rolesAllowed` and `access`
(`INHERIT`, `PUBLIC`, `AUTHENTICATED`, `DENY`). The most specific element that says something
decides: method, then resource, then application. With no rule at all a method is public; an
application with `access="DENY"` is closed by default.

| Situation | Response |
|---|---|
| Caller required, none present | 401 |
| Caller lacks all allowed roles, or access is `DENY` | 403 |

The runtime only authorizes. Authentication stays a whiteboard extension (e.g. a JWT filter)
that sets the `SecurityContext`. It is attached through the model: `properties` become service
properties of the application, `extensionSelect` the extensions it requires. Access is checked
before anything else of the request is looked at.

Annotation-driven security features (`@RolesAllowed` scanners) see nothing on a modelled
resource, because there is no resource class. A deny-by-default feature of that kind would
close every modelled method and must not select a modelled application.

## From an OpenAPI document

One factory configuration brings up a complete application:

```json
"org.eclipse.fennec.rest.OpenApiApplication~petstore": {
  "name": "petstore",
  "documentUrl": "file:/.../petstore.json",
  "delegateUri": "https://example.org/petstore/logic"
}
```

| Document | Becomes |
|---|---|
| `components/schemas` | the domain EPackage (by the codec, namespace `<nsUri>/schemas`) |
| every path operation | an `EOperation` of the service EClass `<Name>Service`, named by its `operationId` |
| parameters and request body | individual `EParameter`s, bound where the document puts them |
| the chosen 2xx JSON response | the result type; an array is a many-valued result |
| a `security` requirement | an authenticated caller; its scopes as `rolesAllowed` |

The business logic is one `EOperation.Internal.InvocationDelegate.Factory` service registered
for `delegateUri`. Every operation carries an annotation with that URI as source and the
details `operationId`, `method` and `path`.

The document is loaded and linked by the `OpenApiImporter` of emf.util, which also serves the
client side, so its limits apply: only the first 2xx `application/json` response, only `$ref`
schemas, no non-JSON bodies. What cannot be imported faithfully is logged as a diagnostic, e.g.
security requirements that need several scopes at once or offer alternatives (roles mean
"one of").

## Not covered yet

- Streams and binary bodies.
- Lists of EObjects as result: the codec has no released message body writer for collections
  yet (eclipse-fennec/emf.codec#334).
- The OpenAPI import as a QVT transformation (emf.m2x) instead of Java.
- `@BeanParam`, name-bound filters, asynchronous methods, sub-resource locators.
- The request-scoped `ResourceSet` of the codec cannot be bound as a parameter yet.
- The target EClass is instantiated per request and is stateless.
- Many-valued body parameters are rejected when the model is interpreted.
- EMF caches the delegate on the `EOperation`. A delegate factory that disappears is noticed
  (503), one that is replaced without a gap is not.
- A change of the binding model needs a new `RestApplication` service; it is not observed.
