# Design API - `generate` Method

> **See also**: [Design API Vision](design.md) for the complete fluent object configuration overview.

## Overview

The `generate` method implements the `ObjectGenerator` interface, allowing `Design<T>` instances to be used in contexts where an object generator is required. This enables integration with the broader AutoParams framework.

## Context

**Project**: autoparams
**Package**: autoparams.customization
**Test Project**: test.autoparams.java-17
**Test Package**: test.autoparams.customization

## API Specification

```java
ObjectGenerator generate(ObjectQuery query, ResolutionContext context);
```

## Description

`Design<T>` implements the `ObjectGenerator` interface, allowing it to be used in contexts where an object generator is required. The `generate` method creates an object based on the provided query and resolution context.

## Test Scenarios

- [ ] generate creates object using configured design
- [ ] generate integrates with context for dependency resolution
- [ ] generate returns empty when query type does not match
- [ ] generate throws exception when query is null
- [ ] generate throws exception when context is null

## Usage Example

```java
Design<Product> design = Design.of(Product.class)
    .set(Product::name, "Product A");
ObjectQuery query = new ObjectQuery(Product.class);
ResolutionContext context = new ResolutionContext();
ObjectGenerator generator = design.generate(query, context);
```

## Dependencies

This method depends on:
- `ObjectGenerator` interface implementation
- `ObjectQuery` for type matching
- `ResolutionContext` for dependency resolution
- `instantiate(ResolutionContext context)` method (called internally)
- Configuration methods like `set`, `supply`, `design` (for object configuration)

## Implementation History
