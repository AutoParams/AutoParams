# PropertyReflector class

```java
public class PropertyReflector<T, R> {

    public static <T, R> PropertyDescriptor getProperty(
        FunctionDelegate<T, R> getterDelegate
    ) {
    }
}
```

**Project**: autoparams
**Package**: autoparams.internal.reflect;

The `PropertyReflector` class is an internal utility class that provides a way to retrieve a property descriptor for a function delegate that represents a getter method. It allows you to encapsulate a function delegate and retrieve the property associated with it.

It replaces the `GetterDelegate` class and `ParameterNameInferencer` class.

### Todos

- [ ] 1. Implement `getProperty` by referring to the `GetterDelegate` and `ParameterNameInferencer` implementations
- [ ] 2. Find all code where `GetterDelegate` and `ParameterNameInferencer` is used and replace it with `PropertyReflector`
- [ ] 3. Run `./gradlew build` to check if the build succeeds
- [ ] 4. Remove `GetterDelegate` and `ParameterNameInferencer`
