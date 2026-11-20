# Not covered yet

- Streams and binary bodies.
- `@BeanParam`, name-bound filters, asynchronous methods, sub-resource locators.
- The request-scoped `ResourceSet` of the codec cannot be bound as a parameter yet.
- The target EClass is instantiated per request and is stateless.
- Many-valued body parameters are rejected when the model is interpreted.
- EMF caches the delegate on the `EOperation`. A delegate factory that disappears is noticed
  (503), one that is replaced without a gap is not.
- A change of the binding model needs a new `RestApplication` service; it is not observed.
