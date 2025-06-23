# Designer API - Fluent Object Configuration

## API Design

The `Factory.design(Class<T> type)` method returns a `Designer<T>` instance that provides a fluent interface for configuring and creating objects.

### Factory and Designer<T>

- The `design` static method of `Factory` returns a `Designer<T>` instance for the specified type `T`.
- The `Designer<T>` class is located in the `autoparams.generator` package of the `autoparams` module, where the `Factory<T>` class is also defined.
- The test class for `Designer<T>` is `test.autoparams.generator.SpecsForDesigner` in the `test-autoparams-java-17` module.
- The `Designer<T>` constructor is package-private, so instances cannot be created directly from outside and can only be accessed through the `Factory.design(Class<T> type)` method.
- The `create()` method generates an arbitrary object of type `T`.

```java
Designer<Product> designer = Factory.design(Product.class);
Product product = designer.create();
```

**Test Scenarios**:

- [x] design throws exception when argument type is null
- [x] sut creates an object of the specified type

### Setting Constructor Argument Values Corresponding to Properties

- The `<P>set` method accepts a method reference of type `FunctionDelegate<T, P>`.
- The `<P>set` method returns an instance of the `ParameterBinding<P>` class, which is provided by the `Designer<T>` class.
- The `ParameterBinding<P>` class allows you to set the property value using the `to(P value)` method.

```java
Product product = Factory
    .design(Product.class)
    .set(Product::name).to("Product A")
    .set(Product::imageUri).to("https://example.com/product-a.jpg")
    .create();
```

**Test Scenarios**:

- [x] sut sets property value when using method reference
- [x] sut overwrites property value when set multiple times
- [x] sut throws exception when property getter delegate is null

### Processing the Created Object

- The `process` method accepts a `Consumer<T>` that allows you to perform additional operations on the created object before returning it.

```java
Order order = Factory
    .design(Order.class)
    .process(o -> o.applyPercentDiscount(10))
    .create();
```

**Test Scenarios**:

- [x] sut applies processor to created object
- [x] sut applies multiple processors in sequence
- [x] sut throws exception when processor is null

### Nested Object Configuration

- The `withDesign` method configures a nested object by accepting a function that takes and returns a `DesignContext<T>` instance (`Function<DesignContext<T>, DesignContext<T>>`).
- The `DesignContext<T>` class provides a fluent interface for configuring nested objects, similar to the `Designer<T>` class.
- The `DesignContext<T>` class is located in the `autoparams.generator` package of the `autoparams` module.
- The `DesignContext<T>` constructor is package-private, so instances cannot be created directly from outside and can only be accessed through the `withDesign` method of the `Designer<T>` class.
- The `DesignContext<T>` class is derived from the same parent class as the `Designer<T>` class.
- The function passed to the `withDesign` method must return the received argument as is.
- The design function is executed lazily - it is not invoked when `withDesign` is called, but only when the actual object generation occurs during `create()`. This enables more efficient object creation and prevents unnecessary computations.

```java
Review review = Factory
    .design(Review.class)
    .set(Review::product).withDesign(product -> product
        .set(Product::name).to("Product A")
        .set(Product::imageUri).to("https://example.com/product-a.jpg")
    )
    .set(Review::comment).to("Great product!")
    .create();
```

**Test Scenarios**:

- [x] withDesign configures nested object using design function
- [x] withDesign throws exception when design function argument is null
- [x] withDesign does not affect properties outside the nested object
- [x] withDesign supports multiple levels of nested object configuration
- [x] create throws exception when design function does not return its argument
