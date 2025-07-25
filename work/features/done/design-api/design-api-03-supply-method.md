# Design API - `supply` Method

> **See also**: [Design API Vision](design.md) for the complete fluent object configuration overview.

## Overview

The `supply` method configures a property value using a supplier, allowing for lazy evaluation of property values. This enables dynamic value generation at instantiation time.

## Context

**Project**: autoparams
**Package**: autoparams.customization
**Test Project**: test.autoparams.java-17
**Test Package**: test.autoparams.customization

## API Specification

```java
<P> Design<T> supply(FunctionDelegate<T, P> propertyGetter, Supplier<P> supplier);
```

## Description

`supply` configures a property value using a supplier, allowing for lazy evaluation of the property value. The supplier is only called when `instantiate` is invoked, enabling dynamic value generation.

## Test Scenarios

- [x] supply configures property value using supplier
- [x] supply calls supplier only when instantiate is called
- [x] supply throws exception when propertyGetter is null
- [x] supply throws exception when supplier is null

## Implementation Guide

- Understand deeply how `Customizer` and `ObjectGenerator` work for `ResolutionContext`.
- Store configurations in an immutable list of `Customizer` instances.
- Use `AtomicInteger` or a similar mechanism to verify that the supplier is evaluated lazily.

## Usage Example

```java
Design<Product> design = Design.of(Product.class)
    .supply(Product::stockQuantity, () -> new Random().nextInt(100));
```

## Dependencies

This method depends on:
- `of` method (to create the Design instance)
- `PropertyReflector.getProperty` for property reflection
- `instantiate` method (where the supplier is actually called)

## Implementation History

- **2025-07-06**: Added `supply` method signature to `Design` class
- **2025-07-06**: Implemented supplier-based property configuration using `PropertyReflector` and custom `Customizer`
- **2025-07-06**: Added comprehensive test coverage for all scenarios including lazy evaluation verification
- **2025-07-06**: All test scenarios completed successfully
