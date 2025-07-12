# Design API - Fluent Object Configuration

## Vision

```java
Design<Product> design = Design.of(Product.class) // Design<Product> implements Customizer
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

## API Implementation Order

The Design API should be implemented in the following order, with each API documented separately:

1. **[`of` Method](design-api-01-of-method.md)** - Static factory method (foundational)
2. **[`instantiate` Method](design-api-02-instantiate-method.md)** - Basic instance creation
3. **[`set` Method](design-api-03-set-method.md)** - Property value configuration
4. **[`supply` Method](design-api-04-supply-method.md)** - Lazy property evaluation
5. **[`design` Method](design-api-05-design-method.md)** - Nested object configuration
6. **[`instantiate(int count)` Method](design-api-06-instantiate-count-method.md)** - Multiple instance creation
7. **[`instantiate(ResolutionContext context)` Method](design-api-07-instantiate-context-method.md)** - Context-based instantiation
8. **[`instantiate(int count, ResolutionContext context)` Method](design-api-08-instantiate-count-context-method.md)** - Multiple instances with context
9. **[Customizer Interface Implementation](design-api-09-customizer-interface.md)** - AutoParams framework integration

Each API document contains:
- Method signature and description
- Test scenarios
- Implementation guidelines
- Usage examples
- Dependencies on other APIs
