# Design API - `design` Method

> **See also**: [Design API Vision](design.md) for the complete fluent object configuration overview.

## Overview

The `design` method configures a nested object property value using a design function that takes and returns a `Design<P>` instance. This enables fluent configuration of nested objects.

## Context

**Project**: autoparams
**Package**: autoparams.customization
**Test Project**: test.autoparams.java-17
**Test Package**: test.autoparams.customization

## API Specification

```java
<P> Design<T> design(FunctionDelegate<T, P> propertyGetter, Function<Design<P>, Design<P>> designFunction);
```

## Description

`design` configures a nested object property value using a design function that takes and returns a `Design<P>` instance. The nested design function is evaluated lazily, meaning it is only invoked when `instantiate` is called.

## Test Scenarios

- [ ] design configures nested object property values
- [ ] design calls design function only when instantiate is called
- [ ] design throws exception when propertyGetter is null
- [ ] design throws exception when designFunction is null

## Usage Example

```java
Design<Product> design = Design.of(Product.class)
    .design(Product::category, category -> category
        .set(Category::name, "Category A")
        .set(Category::description, "Description of Category A")
    );
```

## Dependencies

This method depends on:
- `of` method (to create the Design instance)
- `PropertyReflector.getProperty` for property reflection
- `set` method (used within the design function)
- `instantiate` method (where the design function is actually called)

## Implementation History
