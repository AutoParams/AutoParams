# Design API - Fluent Object Configuration

## Vision

```java
Design<Product> design = Design.of(Product.class) // Design<Product> implements ObjectGenerator
    .set(Product::name, "Product A")
    .set(Product::imageUri, "https://example.com/product-a.jpg")
    .supply(Product::stockQuantity, intIn(10, 100)) // intIn(10, 100) returns Supplier<Integer>
    .design(Product::category, category -> category // Design<Category> -> Design<Category>
        .set(Category::name, "Category A")
        .set(Category::description, "Description of Category A")
    );

Product product = design.instantiate(); // Creates a Product instance with the specified properties
List<Product> products = design.instantiate(5); // Creates a list of 5 Product instances
```

- The `Design<T>` class provides a fluent interface for configuring and creating objects of type `T`.
- The `Design<T>` class is immutable and thread-safe, allowing for safe concurrent use.
- The `of` method is a static factory method that returns an instance of `Design<T>` for the specified type `T`.
- The `set` method allows you to set property values using method references, providing a clear and type-safe way to configure objects.
- The `supply` method allows you to set property values using a supplier, enabling lazy evaluation of property values.
- The `design` method allows you to configure nested objects by accepting a function that takes and returns a `Design<T>` instance (where `T` is the type of the nested object), enabling a fluent interface for nested object configuration. The nested design function is evaluated lazily, meaning it is only invoked when `instantiate` is called.
- The `instantiate` method creates an instance of the configured object, applying all the specified configurations.

**Project**: autoparams
**Package**: autoparams.customization
**Test Project**: test.autoparams.java-17
**Test Package**: test.autoparams.customization

## APIs

### `of` Method

```java
static <T> Design<T> of(Class<T> type);
```

`of` is a static factory method that returns an instance of `Design<T>` for the specified type `T`.

**Test Scenarios**:
- [ ] of creates Design instance for specified type
- [ ] of throws exception when type argument is null

**Implementation Guide**:
- Use `test.autoparams.Product` record class as the type argument for testing.

### `instantiate` Method

```java
T instantiate();
```

`instantiate` creates an instance of the configured type `T`. It uses `ResolutionContext` to resolve dependencies and applies all the configurations set on the `Design<T>` instance.

**Test Scenarios**:
- [ ] instantiate creates instance of configured type

**Implementation Guide**:
- Use `ResolutionContext.resolve` method to create an instance of `T`.

### `set` Method

```java
<P> Design<T> set(FunctionDelegate<T, P> propertyGetter, P value);
```

`set` configures a property value for the instantiated object using a method reference.

**Test Scenarios**:
- [ ] set configures property value for instantiated object
- [ ] set allows chaining multiple property configurations
- [ ] set throws exception when propertyGetter is null
- [ ] set returns new Design instance

**Implementation Guide**:
- Understand deeply how `Customizer` and `ObjectGenerator` work for `ResolutionContext`.
- Use `PropertyReflector.getProperty` to retrieve the property descriptor for the provided `propertyGetter`.
- It is helpful to refer to the `ArgumentCustomizationDsl` class for examples of how to implement the `set` method.

### `supply` Method

```java
<P> Design<T> supply(FunctionDelegate<T, P> propertyGetter, Supplier<P> supplier);
```

`supply` configures a property value using a supplier, allowing for lazy evaluation of the property value.

**Test Scenarios**:
- [ ] supply configures property value using supplier
- [ ] supply calls supplier only when instantiate is called
- [ ] supply throws exception when propertyGetter is null
- [ ] supply throws exception when supplier is null

**Implementation Guide**:
- Use `AtomicInteger` or similar to test lazy evaluation of the supplier.

### `design` Method

```java
<P> Design<T> design(FunctionDelegate<T, P> propertyGetter, Function<Design<P>, Design<P>> designFunction);
```

`design` configures a nested object property value using a design function that takes and returns a `Design<P>` instance.

**Test Scenarios**:
- [ ] design configures nested object property values
- [ ] design calls design function only when instantiate is called
- [ ] design throws exception when propertyGetter is null
- [ ] design throws exception when designFunction is null

### `instantiate(int count)` Method

```java
List<T> instantiate(int count);
```

`instantiate(int count)` creates a list of instances of the configured type `T`, with the specified number of elements.

**Test Scenarios**:
- [ ] instantiate with count creates list with specified number of instances
- [ ] instantiate with count applies configurations to all instances
- [ ] instantiate with count throws exception when count is negative
- [ ] instantiate with count creates unique instances
- [ ] instantiate with count returns unmodifiable list

### `instantiate(ResolutionContext context)` Method

```java
T instantiate(ResolutionContext context);
```

`instantiate(ResolutionContext context)` creates an instance of the configured type `T`, using the provided `ResolutionContext` to resolve dependencies.

**Test Scenarios**:
- [ ] instantiate with context creates instance using provided context
- [ ] instantiate with context throws exception when context is null

**Implementation Guide**:
- Create a customizer using `ArgumentCustomizationDsl.set` then apply it to the `ResolutionContext` to arrange the context for freeze some properties.

### `instantiate(int count, ResolutionContext context)` Method

```java
List<T> instantiate(int count, ResolutionContext context);
```

`instantiate(int count, ResolutionContext context)` creates a list of instances of the configured type `T`, with the specified number of elements, using the provided `ResolutionContext` to resolve dependencies.

**Test Scenarios**:
- [ ] instantiate with count and context creates list using provided context
- [ ] instantiate with count and context throws exception when count is negative
- [ ] instantiate with count and context throws exception when context is null
- [ ] instantiate with count and context returns unmodifiable list

### `generate` Method

```java
ObjectGenerator generate(ObjectQuery query, ResolutionContext context);
```

`Design<T>` implements the `ObjectGenerator` interface, allowing it to be used in contexts where an object generator is required. The `generate` method creates an object based on the provided query and resolution context.

**Test Scenarios**:
- [ ] generate creates object using configured design
- [ ] generate integrates with context for dependency resolution
- [ ] generate returns empty when query type does not match
- [ ] generate throws exception when query is null
- [ ] generate throws exception when context is null

## Implementation History
