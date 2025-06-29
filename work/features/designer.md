# Designer API - Fluent Object Configuration

## API Design

The `Designer.design(Class<T> type)` static method returns a `Designer<T>` instance that provides a fluent interface for configuring and creating objects.

### Designer<T>

- The `design` static method of `Designer<T>` returns a `Designer<T>` instance for the specified type `T`.
- The `Designer<T>` class is located in the `autoparams.customization` package of the `autoparams` module.
- The test class for `Designer<T>` is `test.autoparams.generator.SpecsForDesigner` in the `test-autoparams-java-17` module.
- The `Designer<T>` constructor is package-private, so instances cannot be created directly from outside and can only be accessed through the `Designer.design(Class<T> type)` static method.
- The `create()` method generates an arbitrary object of type `T`.

```java
Designer<Product> designer = Designer.design(Product.class);
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
Product product = Designer
    .design(Product.class)
    .set(Product::name).to("Product A")
    .set(Product::imageUri).to("https://example.com/product-a.jpg")
    .create();
```

**Test Scenarios**:

- [x] sut sets property value when using method reference
- [x] sut overwrites property value when set multiple times
- [x] sut throws exception when property getter delegate is null


### Nested Object Configuration

- The `using` method configures a nested object by accepting a function that takes and returns a `DesignContext<T>` instance (`Function<DesignContext<T>, DesignContext<T>>`).
- The `DesignContext<T>` class provides a fluent interface for configuring nested objects, similar to the `Designer<T>` class.
- The `DesignContext<T>` class is located in the `autoparams.customization` package of the `autoparams` module.
- The `DesignContext<T>` constructor is package-private, so instances cannot be created directly from outside and can only be accessed through the `using` method of the `Designer<T>` class.
- The `DesignContext<T>` class is derived from the same parent class as the `Designer<T>` class.
- The function passed to the `using` method must return the received argument as is.
- The design function is executed lazily - it is not invoked when `using` is called, but only when the actual object generation occurs during `create()`. This enables more efficient object creation and prevents unnecessary computations.

```java
Review review = Designer
    .design(Review.class)
    .set(Review::product).using(product -> product
        .set(Product::name).to("Product A")
        .set(Product::imageUri).to("https://example.com/product-a.jpg")
    )
    .set(Review::comment).to("Great product!")
    .create();
```

**Test Scenarios**:

- [x] using configures nested object using design function
- [x] using throws exception when design function argument is null
- [x] using does not affect properties outside the nested object
- [x] using supports multiple levels of nested object configuration
- [x] create throws exception when design function does not return its argument

### Resolving and Injecting Designer Instances

`Designer<T>` can be directly injected or resolved in test code using parameter provider tools such as `@AutoParams`. This enables more flexible handling of object creation and configuration in tests.

- **Resolution via ResolutionContext**: At runtime, you can resolve a `Designer<T>` instance based on type information. By using `TypeReference`, generic type information is preserved, and `context.resolve(...)` returns a Designer instance of the specified type.
- **Automatic Injection via AutoParams**: If you declare `Designer<T>` as a parameter in a test method, the `@AutoParams` annotation automatically injects the instance. This makes test code more concise and allows for more intuitive object creation and configuration.

The following example demonstrates how to use `ResolutionContext` to resolve a `Designer<Product>` instance at runtime, configure the desired property values through method chaining, and then create the object.

```java
@Test
void testMethod() {
    var context = new ResolutionContext();
    Designer<Product> designer = context.resolve(new TypeReference<>() { });
    Product product = designer
        .set(Product::name).to("Product A")
        .set(Product::imageUri).to("https://example.com/product-a.jpg")
        .create();
}
```

The following example demonstrates how to use `@AutoParams` to automatically inject a `Designer<Product>` as a parameter in a test method and create an object using method chaining in the same way.

```java
@Test
@AutoParams
void testMethod(Designer<Product> designer) {
    Product product = designer
        .set(Product::name).to("Product A")
        .set(Product::imageUri).to("https://example.com/product-a.jpg")
        .create();
}
```

**Test Scenarios**:

- [x] sut is resolved from ResolutionContext
- [x] sut is injected as a parameter using @AutoParams

### Object Stream

- `stream` method of `Designer<T>` returns a `Stream<T>` that allows for lazy generation of multiple objects of type `T`.

```java
Stream<Product> stream = Designer
    .design(Product.class)
    .set(Product::stockQuantity).to(100)
    .stream();
assertThat(stream.limit(5))
    .allSatisfy(p -> assertThat(p.stockQuantity()).isEqualTo(100));
```

**Test Scenarios**:

- [x] sut returns stream of objects with configured property values

### Creating a List of Objects

- The `createRange(int count)` method of `Designer<T>` generates a list of objects of type `T` with the specified number of elements.

```java
List<Product> products = Designer
    .design(Product.class)
    .set(Product::priceAmount).to(new BigDecimal("19.99"))
    .createRange(5);
```

**Test Scenarios**:

- [ ] sut creates a list of objects as many as specified
- [ ] sut creates a list of objects with configured property values
- [ ] sut throws exception when count is less than 0
