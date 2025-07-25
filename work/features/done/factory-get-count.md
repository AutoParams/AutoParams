# autoparams.generator.Factory.get(int count) method

## Method Signature

```java
public List<T> get(int count);
public List<T> get(int count, Customizer... customizers);
```

## Description

The `get(int count)` method in the Factory class is designed to retrieve a specified number of instances of type `T`. This method is particularly useful when you need to generate or obtain multiple objects of the same type without having to create each one individually.

## Test Scenarios

- [x] get with count returns list with specified count (use `@ParameterizedTest` with values including 0)
- [x] get with count throws exception when count is negative
- [x] get with count returns list with unique instances
- [x] get with count returns immutable list
- [x] get with count and customizers applies customizers to all generated instances
- [x] get with count and customizers throws exception when count is negative
- [x] get with count and customizers returns list with specified count
- [x] get with count and customizers returns list with unique instances

## Implementation History

- 2025-07-01: Added `get(int count)` method signature to Factory class
- 2025-07-01: Implemented test scenario for returning list with specified count (including 0)
- 2025-07-01: Implemented test scenario for throwing exception when count is negative
- 2025-07-01: Implemented test scenario for returning list with unique instances
- 2025-07-01: All test scenarios completed successfully
- 2025-07-03: Added test scenario for returning immutable list
- 2025-07-03: Modified `get(int count)` method to return unmodifiable list
- 2025-07-03: All test scenarios completed successfully
- 2025-07-04: Added `get(int count, Customizer...)` method signature to Factory class
- 2025-07-04: Implemented test scenario for applying customizers to all generated instances
- 2025-07-04: Implemented test scenario for throwing exception when count is negative with customizers
- 2025-07-04: Implemented test scenario for returning list with specified count with customizers
- 2025-07-04: Implemented test scenario for returning list with unique instances with customizers
- 2025-07-04: All test scenarios for get(int count, Customizer...) method completed successfully
