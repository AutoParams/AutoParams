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

- [x] instantiate with count creates list with specified number of instances
- [x] instantiate with count applies configurations to all instances
- [x] instantiate with count throws exception when count is negative
- [x] instantiate with count creates unique instances
- [x] instantiate with count returns unmodifiable list

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

### 2025-07-11 - Implementation Complete
- **Commit**: `bd46cf9` - "Add Design API instantiate method with count"
- **Implementation**: Added `instantiate(int count)` method to Design class
- **Features**:
  - Creates list with specified number of instances
  - Applies all configurations to each instance
  - Validates count parameter (throws IllegalArgumentException if negative)
  - Creates unique instances (no shared references)
  - Returns unmodifiable list for immutability
- **Test Coverage**: 5 comprehensive test scenarios covering all functionality
- **Location**: `autoparams/src/main/java/autoparams/customization/Design.java:88-98`
- **Tests**: `test-autoparams-java-17/src/test/java/test/autoparams/customization/SpecsForDesign.java`

### Implementation Details
The method internally:
1. Validates the count parameter (must be ≥ 0)
2. Creates a new ArrayList with the specified capacity
3. Calls the existing `instantiate()` method in a loop to create unique instances
4. Returns the list wrapped with `Collections.unmodifiableList()` for immutability

### Status
✅ **COMPLETE** - All test scenarios implemented and passing
