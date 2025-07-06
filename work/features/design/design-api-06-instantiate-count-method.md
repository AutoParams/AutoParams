# Design API - `instantiate(int count)` Method

> **See also**: [Design API Vision](design.md) for the complete fluent object configuration overview.

## Overview

The `instantiate(int count)` method creates a list of instances of the configured type `T`, with the specified number of elements. This enables bulk object creation with consistent configuration.

## Context

**Project**: autoparams
**Package**: autoparams.customization
**Test Project**: test.autoparams.java-17
**Test Package**: test.autoparams.customization

## API Specification

```java
List<T> instantiate(int count);
```

## Description

`instantiate(int count)` creates a list of instances of the configured type `T`, with the specified number of elements. All instances are created using the same configuration but are unique objects.

## Test Scenarios

- [ ] instantiate with count creates list with specified number of instances
- [ ] instantiate with count applies configurations to all instances
- [ ] instantiate with count throws exception when count is negative
- [ ] instantiate with count creates unique instances
- [ ] instantiate with count returns unmodifiable list

## Usage Example

```java
Design<Product> design = Design.of(Product.class)
    .set(Product::name, "Product A");
List<Product> products = design.instantiate(5);
```

## Dependencies

This method depends on:
- `of` method (to create the Design instance)
- `instantiate()` method (called multiple times internally)
- Configuration methods like `set`, `supply`, `design` (for object configuration)

## Implementation History
