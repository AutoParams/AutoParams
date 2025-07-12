# Design API - Customizer Interface Implementation

> **See also**: [Design API Vision](design-api.md) for the complete fluent object configuration overview.

## Overview

The `Design<T>` class implements the `Customizer` interface to enable seamless integration with the AutoParams framework. This allows `Design<T>` instances to be used as reusable customizers that can be applied to object generation and resolution processes throughout the AutoParams ecosystem.

## Context

**Project**: autoparams
**Package**: autoparams.customization
**Test Project**: test.autoparams.java-17
**Test Package**: test.autoparams.customization

## Interface Implementation

```java
public class Design<T> implements Customizer {
}
```

## Description

By implementing the `Customizer` interface, `Design<T>` instances become first-class citizens in the AutoParams framework. The `customize` method applies the configured design patterns (property settings, suppliers, nested designs) to objects that match the design's target type during the resolution process.

### Key Behaviors

- **Type Matching**: Only customizes objects that match the `Design<T>` target type
- **Configuration Application**: Applies all configured `.set()`, `.supply()`, and `.design()` settings
- **Context Integration**: Works seamlessly with `ResolutionContext` and other customizers
- **Reusability**: Can be registered once and applied to multiple object creation scenarios

## Test Scenarios

- [x] customize integrates with ResolutionContext for dependency resolution
- [x] customize can be combined with other customizers in the same context
- [x] customize with generator throws exception when generator is null
- [x] sut can be composed with other customizers

## Usage Examples

### As ResolutionContext Customizer

```java
Design<Product> design = Design.of(Product.class)
    .set(Product::stockQuantity, 100);

ResolutionContext context = new ResolutionContext();
context.customize(design);

// All Product instances created through this context will be customized
Product product = context.resolve(Product.class);

assertEquals(100, product.getStockQuantity());
```

### As Test Method Customizer

```java
public static class ProductCustomizer extends CompositeCustomizer {

    public ProductCustomizer() {
        super(new Design<Product>().set(Product::stockQuantity, 100));
    }
}

@Test
@AutoParams
@Customization(ProductCustomizer.class)
void testProductGeneration(Product product) {
    // Product will be automatically customized using the design
    assertEquals(100, product.getStockQuantity());
}
```

## Dependencies

This implementation depends on:
- `Customizer` interface from AutoParams framework
- All Design configuration methods (`set`, `supply`, `design`)

## Integration Benefits

1. **Seamless Framework Integration**: Works naturally with existing AutoParams patterns
2. **Reusability**: Define once, use across multiple tests and contexts
3. **Composability**: Can be combined with other customizers

## Implementation History

- **2025-07-12**: Implemented Customizer interface in Design class
  - Added `implements Customizer` to class declaration
  - Implemented `customize(ObjectGenerator generator)` method using CompositeCustomizer
  - Added null validation for ObjectGenerator parameter following Customizer interface contract
  - Added tests for ResolutionContext integration and multi-customizer scenarios
  - Changed customizers field from `List<Customizer>` to `Customizer[]` for better performance
  - Added test for Design composition within CompositeCustomizer
  - Updated tests to use ProductCustomizer class matching documentation examples
  - All test scenarios completed successfully
