# Property Class

```java
public final class Property<T, R> {

    public static <T, R> Property<T, R> parse(FunctionDelegate<T, R> getterDelegate);

    public Class<T> getDeclaringClass();

    public Class<R> getType();

    public String getName();
}
```

**Project**: autoparams
**Package**: autoparams.internal.reflect;

The `Property<T, R>` class is an internal utility class that represents a property. It encapsulates the declaring class, type, and name of the property, and can be created using a `FunctionDelegate`.

This class replaces the `PropertyReflector` class.

## Todos

- [x] 1. Implement `parse` method to create a `Property` instance from a `FunctionDelegate` by referring to the `PropertyReflector` implementation.
- [x] 2. Find all code where `PropertyReflector` is used and replace it with `Property`.
- [x] 3. Run `./gradlew build` to check if the build succeeds.
- [x] 4. Remove `PropertyReflector`.
