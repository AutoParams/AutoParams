# autoparams.generator.Factory.get(int count) method

## Method Signature

```java
public List<T> get(int count)
```

## Description

The `get(int count)` method in the Factory class is designed to retrieve a specified number of instances of type `T`. This method is particularly useful when you need to generate or obtain multiple objects of the same type without having to create each one individually.

## Test Scenarios

- [x] sut returns list with specified count (use `@ParameterizedTest` with values including 0)
- [x] sut throws exception when count is negative
- [x] sut returns list with unique instances
- [ ] sut returns immutable list

## Implementation History

- 2025-07-01: Added `get(int count)` method signature to Factory class
- 2025-07-01: Implemented test scenario for returning list with specified count (including 0)
- 2025-07-01: Implemented test scenario for throwing exception when count is negative
- 2025-07-01: Implemented test scenario for returning list with unique instances
- 2025-07-01: All test scenarios completed successfully
