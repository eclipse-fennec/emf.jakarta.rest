# Access control

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

How `properties` and `extensionSelect` reach the application is described in
[Deployment](deployment.md#service-properties-of-the-application).

## The generated OPTIONS answer

The `OPTIONS` answer the framework generates for every path is described by no modelled method,
so no rule of a method or resource applies to it: it falls under the rule of the application.
An application without a rule leaves the answer to the framework. An application with
`access="DENY"` and the roles on its resources is closed for everything that is not modelled.

Annotation-driven security features (`@RolesAllowed` scanners) see nothing on a modelled
resource, because there is no resource class. A deny-by-default feature of that kind would
close every modelled method and must not select a modelled application.

## Compiled resources are not under the rule of the model

A compiled whiteboard resource that selects a modelled application
(`osgi.jakartars.application.select=(osgi.jakartars.name=<name>)`) is put there by the whiteboard
like into any other application. The access rule of the model does not apply to it: `access` and
`rolesAllowed` are rules of the modelled methods. Such a resource answers by its own annotations
and by the extensions of the application.
