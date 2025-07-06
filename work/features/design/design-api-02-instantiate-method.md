# Design API - `instantiate` Method

> **See also**: [Design API Vision](design.md) for the complete fluent object configuration overview.

## Overview

The `instantiate` method creates an instance of the configured type `T` using the default resolution context. This is the primary method for materializing configured objects.

## Context

**Project**: autoparams
**Package**: autoparams.customization
**Test Project**: test.autoparams.java-17
**Test Package**: test.autoparams.customization

## API Specification

```java
T instantiate();
```

## Description

`instantiate` creates an instance of the configured type `T`. It uses `ResolutionContext` to resolve dependencies and applies all the configurations set on the `Design<T>` instance.

## Test Scenarios

- [ ] instantiate creates instance of configured type

## Implementation Guide

- Use `ResolutionContext.resolve` method to create an instance of `T`.

## Usage Example

```java
Design<Product> design = Design.of(Product.class);
Product product = design.instantiate();
```

## Dependencies

This method depends on:
- `of` method (to create the Design instance)
- `ResolutionContext` for object resolution

## Implementation History