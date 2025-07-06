# Design API - `of` Method

> **See also**: [Design API Vision](design.md) for the complete fluent object configuration overview.

## Overview

The `of` method is a static factory method that creates a new `Design<T>` instance for the specified type `T`. This is the entry point for the fluent Design API.

## Context

**Project**: autoparams
**Package**: autoparams.customization
**Test Project**: test.autoparams.java-17
**Test Package**: test.autoparams.customization

## API Specification

```java
static <T> Design<T> of(Class<T> type);
```

## Description

`of` is a static factory method that returns an instance of `Design<T>` for the specified type `T`. This method serves as the starting point for building a fluent configuration chain.

## Test Scenarios

- [ ] of creates Design instance for specified type
- [ ] of throws exception when type argument is null

## Implementation Guide

- Use `test.autoparams.Product` record class as the type argument for testing.

## Usage Example

```java
Design<Product> design = Design.of(Product.class);
```

## Dependencies

This method is foundational and has no dependencies on other Design API methods.

## Implementation History
