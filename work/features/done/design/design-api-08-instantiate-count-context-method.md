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

- [x] instantiate with count and context creates list using provided context
- [x] instantiate with count and context throws exception when count is negative
- [x] instantiate with count and context throws exception when context is null
- [x] instantiate with count and context returns unmodifiable list
- [x] instantiate with count and context does not modify the original design

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

### Initial Implementation (2025-07-11)
- **Method signature**: Added `List<T> instantiate(int count, ResolutionContext context)` to Design class
- **TDD approach**: Implemented using test-driven development methodology
- **Core scenarios**: Implemented 4 initial test scenarios covering basic functionality, validation, and safety

### Enhanced Implementation (2025-07-11)
- **Optimization**: Improved implementation to use context branching instead of repeated method calls
- **Performance**: Enhanced to create single branched context and reuse for all instances
- **Additional scenario**: Added 5th test scenario to verify design immutability
- **Final validation**: All 5 test scenarios passing with optimized implementation

### Final Status
- **Method**: `instantiate(int count, ResolutionContext context)` - ✅ Complete
- **Tests**: 5/5 test scenarios implemented and passing
- **Implementation**: Optimized for performance and consistency
- **Documentation**: Complete with usage examples and implementation notes
