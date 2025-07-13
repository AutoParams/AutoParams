# Design

## Overview

The `Design` API provides a fluent interface for configuring object creation in AutoParams. While `Factory` generates objects with automatically generated values, `Design` allows you to specify exact property values and create highly customized test data.

`Design` is particularly useful when you need predictable test data or want to configure complex object hierarchies with specific values.

## How to Use

### Property Configuration with Fixed Values

Use the `.set()` method to configure properties with specific values:

```java
Product product = Design.of(Product.class)
    .set(Product::getName, "Custom Product Name")
    .set(Product::getPrice, 99.99)
    .instantiate();

// product.getName() returns "Custom Product Name"
// product.getPrice() returns 99.99
```

The `.set()` method uses type-safe method references to ensure compile-time safety and refactoring support.

### Property Configuration with Dynamic Values

Use the `.supply()` method to configure properties with supplier functions:

```java
AtomicInteger counter = new AtomicInteger(0);

Design<Product> design = Design.of(Product.class)
    .supply(Product::getName, () -> "Product " + counter.incrementAndGet())
    .supply(Product::getPrice, () -> Math.random() * 100);

Product product1 = design.instantiate(); // "Product 1"
Product product2 = design.instantiate(); // "Product 2"
```

The `Supplier` functions are called each time `.instantiate()` is executed, allowing you to generate different values for each instance.

### Nested Object Configuration

Configure complex object hierarchies using the `.design()` method:

```java
Random random = new Random();

Review review = Design.of(Review.class)
    .set(Review::getRating, 5)
    .design(Review::getProduct, product -> product
        .set(Product::getName, "Amazing Widget")
        .supply(Product::getPrice, () -> random.nextInt(10, 100)))
    .instantiate();
```

The `.design()` method provides a fluent API for configuring nested object properties while maintaining type safety. You only need to configure the properties you care about for your test.

## `ResolutionContext` Integration

You can use `Design` with a custom `ResolutionContext` to control how auto-generated properties are resolved:

```java
ResolutionContext context = new ResolutionContext();
context.customize(set(Product::getPrice).to(30));

Review review = Design.of(Review.class)
    .set(Review::getRating, 5)
    .instantiate(context);

// review.getRating() returns 5 (configured by Design)
// review.getProduct().getPrice() returns 30 (configured by ResolutionContext)
```

This allows you to combine `Design`'s explicit configuration with `ResolutionContext`'s global customization patterns.

## Creating Reusable Customizers

You can create reusable customizers by extending `CompositeCustomizer` and using `Design` configurations. This allows you to define standardized object configurations that can be applied across multiple tests using the `@Customization` annotation.

```java
public class ProductCustomizer extends CompositeCustomizer {
    public ProductCustomizer() {
        super(
            Design.of(Product.class)
                .set(Product::getName, "Premium Product")
                .set(Product::getPrice, 199.99)
        );
    }
}

@Test
@AutoParams
@Customization(ProductCustomizer.class)
void reusableCustomizerWithAnnotation(Review review) {
    assertEquals("Premium Product", review.getProduct().getName());
    assertEquals(199.99, review.getProduct().getPrice());
}
```

The `@Customization` annotation automatically applies the `ProductCustomizer` to all generated objects in the test method, ensuring that any `Review` object will contain a `Product` with the configured name and price values.

## Multiple Instance Creation

You can create multiple instances with the same configuration using the `.instantiate(int count)` method:

```java
Design<Product> design = Design.of(Product.class)
    .set(Product::getPrice, 25.99)
    .set(Product::getStock, 100);

List<Product> products = design.instantiate(3);

// Verify all instances have same configuration
assertEquals(3, products.size());
for (Product product : products) {
    assertEquals(25.99, product.getPrice());
    assertEquals(100, product.getStock());
}
```

This is useful for creating test data sets where you need multiple objects with identical configurations.

## Next Steps

_Work in progress_
