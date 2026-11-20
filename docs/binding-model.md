# The binding model

A `RestApplication` contains `RestResource`s, a resource binds a path to an `EClass` of the
domain model, and its `RestMethod`s bind HTTP method and sub-path to the `EOperation`s of that
class. A `ParameterBinding` says where the argument of an `EParameter` comes from.

The rule of thumb is the one of Jakarta RS itself: what annotations can express in Java is
modelled; everything beyond that is done by returning a `Response` from the delegate.

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

## Response mapping

| Outcome of the operation | Response |
|---|---|
| A Jakarta RS `Response` | that response, unchanged |
| A list of EObjects (many-valued operation typed by an EClass) | 200 (or the modelled `successStatus`) with a JSON array, also for one or no element |
| Any other result | 200 (or the modelled `successStatus`) with the result as entity |
| No result | 204 (or the modelled `successStatus`) |
| Delegate throws a `WebApplicationException` | its response |
| Delegate throws anything else | 500 |
| Parameter literal cannot be converted | 404 for path, query and matrix parameters, 400 for the others (as Jakarta RS does) |
| Parameter that is there but empty, for anything but a string | the default value of the binding, else that of the type |
| No invocation delegate registered for a dynamic operation | 503 |

The rule of thumb is the one of Jakarta RS itself: what annotations can express in Java is
modelled on the `RestMethod`; everything beyond that (status by outcome, response headers,
`Location`) is done by returning a `Response` from the delegate. In a dynamic model the return
type is an `EDataType` with the instance class name `jakarta.ws.rs.core.Response`.

A many-valued result is handed to the codec as `List<EObject>` (a `GenericEntity`), because the
list of a dynamic model does not tell what it holds. A delegate that returns a `Response` with a
list of dynamic EObjects has to wrap it in a `GenericEntity` itself. Many-valued body parameters
are not read yet.
