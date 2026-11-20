# From an OpenAPI document

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

## The import as a QVT-O transformation

`org.eclipse.fennec.rest.openapi.qvto` contains `OpenApiToRest.qvto`, which derives service
package and binding model from the loaded OpenAPI model, and `OpenApiQvtoImporter`, which runs
it with the Fennec M2X engine. `QvtoMatchesJavaImportTest` imports the same document with both
variants and requires an identical result. For the pet store document of the tests it is.

So the mapping itself is expressible in QVT-O without blackbox Java. What was learned about the
limits, all with the M2X snapshot 0.1.1:

Observed

- **Keyword-named features.** `PathItem.post` and `Parameter.in` collide with keywords and have
  to be written `self._'post'` and `self._'in'`. `self._post` also parses, but is a different
  name and only fails when the transformation runs (`Unknown property '_post'`).
- **Qualified type names.** `OAS::OpenAPI`, with the model type alias as qualifier, is an
  unknown type; the names have to be unqualified. Two metamodels with a class of the same name
  would then be ambiguous. Qualifying with the package name was not tried.
- **Output extents list everything.** `getContents()` of an out extent returns every created
  object, also the contained ones. The caller picks the roots.
- **Read-only input.** The schemas package belongs to the input model. Giving it its own
  namespace, and making the packages roots of resources, is left to Java after the run.
- **Loading is outside.** The transformation starts from the model the codec loaded and relies
  on what the codec computed while loading (the link from a schema to its classifier). Reading
  the JSON or YAML document is not something the transformation does.
- **Regular expressions** (`replaceAll`, `matches`) are available and were needed for the
  names; they are an extension of the OCL library, not standard OCL.

Not done or not verified

- No diagnostics: the Java importer reports security requirements that roles cannot express
  exactly, unsupported HTTP methods and duplicate operation names. The transformation maps
  silently. `log()` would be the means; it was not tried.
- Only operation-level parameters are read. The importer of emf.util also resolves
  path-level and `$ref` parameters, so other documents may show differences.
- One document was compared. The fallback type for a parameter without schema
  (`EString.oclAsType(EClassifier)`) is type-checked but never executed by the test.
- The transformation runs in plain Java in the test. It is not run under OSGi and the
  `OpenApiApplication` component still uses the Java importer.
