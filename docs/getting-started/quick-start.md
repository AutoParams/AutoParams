# Quick Start Guide

This guide will get you up and running with AutoParams in just a few minutes.

## Overview

AutoParams eliminates the boilerplate of creating test data by automatically generating arbitrary values for your test method parameters. Simply annotate your test with `@AutoParams` and focus on your test logic rather than setting up test data.

## Basic Usage

### Simple Parameter Generation

The most basic use of AutoParams is generating arbitrary primitive values:

```java
@Test
@AutoParams
void testCalculatorAdd(int a, int b) {
    Calculator calculator = new Calculator();
    int result = calculator.add(a, b);
    assertEquals(a + b, result);
}
```

AutoParams automatically generates different arbitrary values for `a` and `b` on each test run.

### Demonstrating Arbitrary Value Generation

By default, AutoParams generates different arbitrary values for each parameter, even of the same type:

```java
@Test
@AutoParams
void testArbitraryValues(String first, String second) {
    assertNotNull(first);
    assertNotNull(second);
    assertNotEquals(first, second); // AutoParams generates different values
}
```

This shows that AutoParams doesn't use fixed or predictable values - each parameter gets its own arbitrary value by default.

### Object Generation

AutoParams can generate complex objects automatically:

```java
@Test
@AutoParams
void testUserCreation(User user) {
    assertNotNull(user);
    assertNotNull(user.getName());
    assertTrue(user.getAge() > 0);
    assertNotNull(user.getEmail());
}
```

AutoParams automatically creates a `User` instance with arbitrary values for all constructor parameters.

### Collections and Arrays

AutoParams automatically generates collections and arrays with arbitrary data:

```java
@Test
@AutoParams
void testGeneratedList(List<Integer> numbers) {
    assertNotNull(numbers);
    assertFalse(numbers.isEmpty());

    // AutoParams generates a list with arbitrary integers
    for (Integer number : numbers) {
        assertNotNull(number);
    }
}

@Test
@AutoParams
void testGeneratedArray(String[] items) {
    assertNotNull(items);
    assertTrue(items.length > 0);

    // AutoParams generates an array with arbitrary strings
    for (String item : items) {
        assertNotNull(item);
        assertFalse(item.isEmpty());
    }
}
```

## Next Steps

Now that you have the basics working, explore more advanced features:

- [Logging](../basics/logging.md) - Enable detailed logging of object resolution
- [Factory](../basics/factory.md) - Create instances of a given type
