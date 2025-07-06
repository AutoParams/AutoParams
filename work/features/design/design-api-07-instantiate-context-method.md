# Design API - `instantiate(ResolutionContext context)` Method

> **See also**: [Design API Vision](design.md) for the complete fluent object configuration overview.

## Overview

The `instantiate(ResolutionContext context)` method creates an instance of the configured type `T`, using the provided `ResolutionContext` to resolve dependencies. This enables custom dependency resolution scenarios.

## Context

**Project**: autoparams
**Package**: autoparams.customization
**Test Project**: test.autoparams.java-17
**Test Package**: test.autoparams.customization

## API Specification

```java
T instantiate(ResolutionContext context);
```

## Description

`instantiate(ResolutionContext context)` creates an instance of the configured type `T`, using the provided `ResolutionContext` to resolve dependencies. This allows for custom dependency injection and resolution scenarios.

## Test Scenarios

- [ ] instantiate with context creates instance using provided context
- [ ] instantiate with context throws exception when context is null

## Implementation Guide

- Create a customizer using `ArgumentCustomizationDsl.set` then apply it to the `ResolutionContext` to arrange the context for freeze some properties.

## Usage Example

```java
Design<Product> design = Design.of(Product.class)
    .set(Product::name, "Product A");
ResolutionContext context = new ResolutionContext();
Product product = design.instantiate(context);
```

## Dependencies

This method depends on:
- `of` method (to create the Design instance)
- `ResolutionContext` for dependency resolution
- `ArgumentCustomizationDsl` for context customization
- Configuration methods like `set`, `supply`, `design` (for object configuration)

## Implementation History
