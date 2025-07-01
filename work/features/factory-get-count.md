# autoparams.generator.Factory.get(int count) method

## Method Signature

```java
public List<T> get(int count)
```

## Description

The `get(int count)` method in the Factory class is designed to retrieve a specified number of instances of type `T`. This method is particularly useful when you need to generate or obtain multiple objects of the same type without having to create each one individually.

## Test Scenarios

- [ ] sut returns list with specified count (use `@ParameterizedTest` with values including 0)
- [ ] sut throws exception when count is negative
- [ ] sut returns list with unique instances

## Implementation History
