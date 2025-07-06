# Design API - `instantiate(int count, ResolutionContext context)` Method

> **See also**: [Design API Vision](design.md) for the complete fluent object configuration overview.

## Overview

The `instantiate(int count, ResolutionContext context)` method creates a list of instances of the configured type `T`, with the specified number of elements, using the provided `ResolutionContext` to resolve dependencies.

## Context

**Project**: autoparams
**Package**: autoparams.customization
**Test Project**: test.autoparams.java-17
**Test Package**: test.autoparams.customization

## API Specification

```java
List<T> instantiate(int count, ResolutionContext context);
```

## Description

`instantiate(int count, ResolutionContext context)` creates a list of instances of the configured type `T`, with the specified number of elements, using the provided `ResolutionContext` to resolve dependencies. This combines bulk creation with custom dependency resolution.

## Test Scenarios

- [ ] instantiate with count and context creates list using provided context
- [ ] instantiate with count and context throws exception when count is negative
- [ ] instantiate with count and context throws exception when context is null
- [ ] instantiate with count and context returns unmodifiable list

## Usage Example

```java
Design<Product> design = Design.of(Product.class)
    .set(Product::name, "Product A");
ResolutionContext context = new ResolutionContext();
List<Product> products = design.instantiate(5, context);
```

## Dependencies

This method depends on:
- `of` method (to create the Design instance)
- `instantiate(ResolutionContext context)` method (called multiple times internally)
- `ResolutionContext` for dependency resolution
- Configuration methods like `set`, `supply`, `design` (for object configuration)

## Implementation History
