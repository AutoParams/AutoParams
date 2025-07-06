# Design API - `set` Method

> **See also**: [Design API Vision](design.md) for the complete fluent object configuration overview.

## Overview

The `set` method configures a property value for the instantiated object using a method reference. It provides a type-safe way to set property values in the fluent configuration chain.

## Context

**Project**: autoparams
**Package**: autoparams.customization
**Test Project**: test.autoparams.java-17
**Test Package**: test.autoparams.customization

## API Specification

```java
<P> Design<T> set(FunctionDelegate<T, P> propertyGetter, P value);
```

## Description

`set` configures a property value for the instantiated object using a method reference. This method enables type-safe property configuration and supports method chaining.

## Test Scenarios

- [ ] set configures property value for instantiated object
- [ ] set allows chaining multiple property configurations
- [ ] set throws exception when propertyGetter is null
- [ ] set returns new Design instance

## Implementation Guide

- Use `supply` method to implement the `set` method.

## Usage Example

```java
Design<Product> design = Design.of(Product.class)
    .set(Product::name, "Product A")
    .set(Product::imageUri, "https://example.com/product-a.jpg");
```

## Dependencies

This method depends on:
- `of` method (to create the Design instance)
- `supply` method (to supply the property value)

## Implementation History
