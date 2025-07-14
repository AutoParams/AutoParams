# Support for `TypeReference<T>` in the `Design.of` method

```java
public record Container<T>(T value) { }

Design<Container<Product>> design = Design.of(new TypeReference<>() { });
Container<Product> container = design
    .design(Container::value, value -> value
        .set(Product::getName, "Default Product")
        .set(Product::getPrice, 19.99))
    .instantiate();
```

This allows you to create `Design` instances for generic types using `TypeReference<T>`, enabling more flexible and type-safe object creation.

## Test Scenarios

- [ ] sut returns design instance when called with type reference for generic type
- [ ] sut instantiates generic object when design is created with type reference
- [ ] sut throws exception when type reference is null
- [ ] sut supports property configuration with type reference for generic type
- [ ] sut supports nested property configuration with type reference for generic type

## Implementation History
