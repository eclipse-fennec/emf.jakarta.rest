# Getting started

Fennec Modelled REST runs Jakarta RS applications that have no resource classes. The REST
interface is a model, the behaviour is an EMF invocation delegate:

```
greeting.rest (binding model)          greeting.ecore (domain model)
  RestApplication                         GreetingService
    RestResource  --target-->               greet(name, times)   <-- invocation delegate
      RestMethod  --operation-->                                      (OSGi service)
        ParameterBinding --parameter-->
```

The binding model is separate from the domain model on purpose: the domain model stays free of
transport details.

This page builds the smallest application: one operation, one path. The complete example is the
test project `org.eclipse.fennec.rest.tests` of the repository.

## 1. The bundles

In a bnd workspace, add the workspace library to `cnf/build.bnd`. It brings a repository with the
bundles of the runtime and everything they need:

```
-library: fennecRest
```

At runtime the application needs

| Bundle | Role |
|---|---|
| `org.eclipse.fennec.rest.model` | the binding model (`rest.ecore`) |
| `org.eclipse.fennec.rest` | the interpreter and its OSGi components |
| `org.eclipse.fennec.codec.rest`, `org.eclipse.fennec.codec` | read and write EObjects as request and response bodies |
| `org.eclipse.fennec.emf.osgi.component` | the EMF runtime, including the loader for dynamic models |
| a Jakarta RS whiteboard, e.g. `org.eclipse.osgitech.rest.servlet.whiteboard` | deploys the application |

## 2. The domain model

The operations the application offers are `EOperation`s of an `EClass`. The package names the
URI of the invocation delegate, and every operation carries an annotation with that URI:

```xml
<ecore:EPackage name="greeting" nsURI="https://example.org/greeting/1.0" nsPrefix="greeting">
  <eAnnotations source="http://www.eclipse.org/emf/2002/Ecore">
    <details key="invocationDelegates" value="https://example.org/greeting/delegate"/>
  </eAnnotations>
  <eClassifiers xsi:type="ecore:EClass" name="GreetingService">
    <eOperations name="greet" eType="ecore:EDataType http://www.eclipse.org/emf/2002/Ecore#//EString">
      <eAnnotations source="https://example.org/greeting/delegate">
        <details key="body" value="greet"/>
      </eAnnotations>
      <eParameters name="name" eType="ecore:EDataType http://www.eclipse.org/emf/2002/Ecore#//EString"/>
      <eParameters name="times" eType="ecore:EDataType http://www.eclipse.org/emf/2002/Ecore#//EInt"/>
    </eOperations>
  </eClassifiers>
</ecore:EPackage>
```

The model can be generated code or, as here, a plain `.ecore` file that is loaded at runtime.

## 3. The binding model

The binding model says which request reaches which operation, and where the arguments come
from:

```xml
<rest:RestApplication xmi:version="2.0" xmlns:xmi="http://www.omg.org/XMI"
    xmlns:rest="https://eclipse.org/fennec/rest/1.0.0" name="greeting" basePath="greeting">
  <resources path="greetings">
    <target href="https://example.org/greeting/1.0#//GreetingService"/>
    <methods httpMethod="GET" path="{name}">
      <operation href="https://example.org/greeting/1.0#//GreetingService/greet"/>
      <produces>text/plain</produces>
      <parameters source="PATH">
        <parameter href="https://example.org/greeting/1.0#//GreetingService/greet/name"/>
      </parameters>
      <parameters source="QUERY" name="n" defaultValue="1">
        <parameter href="https://example.org/greeting/1.0#//GreetingService/greet/times"/>
      </parameters>
    </methods>
  </resources>
</rest:RestApplication>
```

`GET <whiteboard>/greeting/greetings/Ada?n=2` calls `greet("Ada", 2)`. What a method and a
parameter binding can say is described in [The binding model](binding-model.md).

## 4. The behaviour

The behaviour is an ordinary EMF invocation delegate factory, published as an OSGi service.
Fennec EMF OSGi puts it into the EMF delegate registry under the URI the model declares:

```java
@Component(service = EOperation.Internal.InvocationDelegate.Factory.class)
@EMFConfigurator(configuratorType = ConfiguratorType.OPERATION_INVOCATION_FACTORY,
        configuratorName = "https://example.org/greeting/delegate")
public class GreetingDelegateFactory implements EOperation.Internal.InvocationDelegate.Factory {

    @Override
    public EOperation.Internal.InvocationDelegate createInvocationDelegate(EOperation operation) {
        if ("greet".equals(operation.getName())) {
            return (target, arguments) -> String.join(" ",
                    Collections.nCopies((Integer) arguments.get(1), "Hello, " + arguments.get(0) + "!"));
        }
        return null;
    }
}
```

The delegate receives the target object and the arguments of `eInvoke` and returns the result.
It knows nothing about REST, unless it wants to: it may return a Jakarta RS `Response`.

## 5. Deployment

Two factory configurations bring the application up, e.g. in a configurator resource of the
bundle that holds the models:

```json
{
  ":configurator:resource-version": 1,

  "DynamicModelConfigurator~greeting": {
    "emf.dynamicEcoreUri": "bundleentry://.../model/greeting.ecore"
  },

  "org.eclipse.fennec.rest.RestModel~greeting": {
    "model.uri": "bundleentry://.../model/greeting.rest",
    "resourceSet.target": "(&(emf.name=rest)(emf.name=greeting))"
  }
}
```

The first registers the domain model, the second loads the binding model once both packages are
known and registers its application as a service. From there the whiteboard for modelled
applications takes over; see [Deployment](deployment.md).

Instead of writing the two models by hand, a complete application can be derived from an OpenAPI
document: [From an OpenAPI document](openapi.md).
