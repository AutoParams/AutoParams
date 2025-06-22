# Designer API - Fluent Object Configuration

## API Design

The `Factory.design(Class<T> type)` method returns a `Designer<T>` instance that provides a fluent interface for configuring and creating objects.

### Factory and Designer<T>

```java
Designer<Product> designer = Factory.design(Product.class);
Product product = designer.create();
```

- The `Designer<T>` class is located in the `autoparams.generator` package of the `autoparams` module, where the `Factory<T>` class is also defined.
- The test class for `Designer<T>` is `test.autoparams.generator.SpecsForDesigner` in the `test-autoparams-java-17` module.
- The `Designer<T>` constructor is package-private, so instances cannot be created directly from outside and can only be accessed through the `Factory.design(Class<T> type)` method.
- The `create()` method generates an arbitrary object of type `T`.

**Test Scenarios**:

- [x] design throws exception when argument type is null
- [x] sut creates an object of the specified type

### Setting Constructor Argument Values Corresponding to Properties

```java
Product product = Factory
    .design(Product.class)
    .set(Product::name).to("Product A")
    .set(Product::imageUri).to("https://example.com/product-a.jpg")
    .create();
```

- The `<P>set` method accepts a method reference of type `FunctionDelegate<T, P>`.
- The `<P>set` method returns an instance of the `PropertyBinding<T, P>` class, which is defined inside the `Designer<T>` class.
- The `PropertyBinding<T, P>` class allows you to set the property value using the `to(P value)` method.

**Test Scenarios**:

- [x] sut sets property value when using method reference
- [x] sut overwrites property value when set multiple times
- [x] sut throws exception when property getter delegate is null

### Processing the Created Object

```java
Order order = Factory
    .design(Order.class)
    .process(o -> o.applyPercentDiscount(10))
    .create();
```

- The `process` method accepts a `Consumer<T>` that allows you to perform additional operations on the created object before returning it.

**Test Scenarios**:

- [x] sut applies processor to created object
- [x] sut applies multiple processors in sequence
- [x] sut throws exception when processor is null

### <WIP> Nested Object Configuration

```java
Product product = Factory
    .design(Product.class)
    .set(Product::supplier).toDesigned(supplier -> supplier
        .set(Supplier::name).to("ACME Corp")
        .set(Supplier::country).to("USA")
    )
    .create();
```

### <WIP> Type Inference

> *Needs to be checked for technical feasibility.*

```java
public class Factory {
    public static <T> Designer<T> design(T... typeHint) {
        return (Designer<T>) design(inferType(typeHint));
    }
}
```

```java
Product product = Factory
    .design() // Automatically detects the Product class using type inference
    .create();
```
