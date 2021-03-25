# AutoParams

[![CI](https://github.com/JavaUnit/AutoParams/actions/workflows/ci.yml/badge.svg)](https://github.com/JavaUnit/AutoParams/actions/workflows/ci.yml)
[![Publish](https://github.com/JavaUnit/AutoParams/actions/workflows/publish.yml/badge.svg)](https://github.com/JavaUnit/AutoParams/actions/workflows/publish.yml)

AutoParams is an arbitrary test data generator for parameterized tests in Java inspired by AutoFixture.

Sometimes setting all the test data manually is very annoying. Sometimes some test data is required but not that important for a particular test. AutoParams automatically generates test arguments for your parameterized test method so you can focus more on your domain and your requirements.

AutoParams is very easy to use. Just decorate your parameterized test method with `@AutoSource` annotation just like using `@ValueSource` annotation or `@CsvSource` annotation. That's all. Then AutoParams wiil generate arbitrary arguments for the parameters of the test method automatically.

```java
@ParameterizedTest
@AutoSource
void parameterizedTest(int a, int b) {
    Calculator sut = new Calculator();
    int actual = sut.add(a, b);
    assertEquals(a + b, actual);
}
```

In the example above, you can see that the arbitrary test data may eliminate the need for triangulation from tests.

## Requirements

- JDK 1.8 or higher

## Install

### Maven

```xml
<dependency>
  <groupId>io.github.javaunit</groupId>
  <artifactId>autoparams</artifactId>
  <version>0.1.2</version>
</dependency>
```

### Gradle

```groovy
testImplementation 'io.github.javaunit:autoparams:0.1.2'
```

## Features

### Primitive Types

AutoParams generates arbitrary test arguments of primitive types.

```java
@ParameterizedTest
@AutoSource
void testMethod(boolean x1, int x2, long x3, float x4, double x5, char x6) {
}
```

### Simple Objects

AutoParams generates arbitrary simple objects for the test parameters.

```java
@ParameterizedTest
@AutoSource
void testMethod(String x1, UUID x2, BigInteger x3) {
}
```

### Enum Types

Enum types are also supported. AutoParams randomly selects enum values.

```java
public enum Day {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY,
    THURSDAY, FRIDAY, SATURDAY
}

@ParameterizedTest
@AutoSource
void testMethod(Day day) {
}
```

### Complex Objects

AutoParams creates complex objects using a public constructor with arbitrary arguments. If the type has more than one public constructors, AutoParams chooses the constructor with the fewest parameters.

```java
class ComplexObject {

    private final int value1;
    private final String value2;

    public ComplexObject(int value1, String value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public int getValue1() {
        return value1;
    }

    public String getValue2() {
        return value2;
    }

}

@ParameterizedTest
@AutoSource
void testMethod(ComplexObject object) {
}
```

#### Constructor Selection Policy

When AutoParams creates objects of complex types it follows the following rules.

1. The constructor decorated with `@ConstructorProperties` annotation is preferentially selected.
2. AutoParams selects the constructor with the fewest parameters.

```java
class ComplexObject {

    private final int value1;
    private final String value2;
    private final UUID value3;

    @ConstructorProperties({"value1", "value2, value3"})
    public ComplexObject(int value1, String value2, UUID value3) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
    }

    @ConstructorProperties({"value1", "value2"})
    public ComplexObject(int value1, String value2) {
        this(value1, value2, null);
    }

    public ComplexObject(int value1) {
        this(value1, null, null);
    }

    public int getValue1() {
        return value1;
    }

    public String getValue2() {
        return value2;
    }

    public UUID getValue3() {
        return value3;
    }

}

@ParameterizedTest
@AutoSource
void testMethod(ComplexObject object) {
    assertNotNull(object.getValue2());
    assertNull(object.getValue3());
}
```

### Generic Types

AutoParams creates objects of generic type using a public constructor with arbitrary arguments. If the type has more than one public constructors, AutoParams chooses the constructor with the fewest parameters.

```java
class GenericObject<T1, T2> {

    private final T1 value1;
    private final T2 value2;

    public GenericObject(T1 value1, T2 value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public T1 getValue1() {
        return value2;
    }

    public T2 getValue2() {
        return value3;
    }

}

@ParameterizedTest
@AutoSource
void testMethod(
    GenericObject<String, ComplexObject> object1,
    GenericObject<UUID, GenericObject<String, ComplexObject>> object2) {
}
```

### Collection Types

AutoParams supports a variety of collection types and streams.

#### Arrays

AutoParams generates an array instance with three elements.

```java
@ParameterizedTest
@AutoSource
void testMethod(int[] array1, String[] array2) {
}
```

#### List Types

`List<E>` interface and `ArrayList<E>` class supported. Generated list objects contain few elements.

```java
@ParameterizedTest
@AutoSource
void testMethod(List<String> list, ArrayList<UUID> arrayList) {
}
```

#### Set Types

`Set<E>` interface and `HashSet<E>` class supported. Generated set objects contain few elements.

```java
@ParameterizedTest
@AutoSource
void testMethod(Set<String> set, HashSet<UUID> hashSet) {
}
```

#### Map Interface

`Map<K, V>` interface and `HashMap<K, V>` class supported. Generated map objects contain few pairs.

```java
@ParameterizedTest
@AutoSource
void testMethod(Map<String, ComplexObject> map, HashMap<UUID, ComplexObject> hashMap) {
}
```

### Streams Types

#### Generic Stream Interface

AutoParams supports the generic `Stream<T>` interface. Generated stream objects provide few elements.

```java
@ParameterizedTest
@AutoSource
void testMethod(Stream<ComplexObject> stream) {
}
```

#### Stream Interfaces of Primitive Types

Stream Interfaces specific to primitive types are supported.

```java
@ParameterizedTest
@AutoSource
void testMethod(IntStream intStream, LongStream longStream, DoubleStream doubleStream) {
}
```

####

### Repeat

Unit tests can be repeated with arbitrary test data. Set `repeat` property of `@AutoSource` annotation as many times as you want to repeat.

```java
@ParameterizedTest
@AutoSource(repeat = 10)
void testMethod(int a, int b) {
    // This test method is performed ten times.
    Calculator sut = new Calculator();
    int actual = sut.add(a, b);
    assertEquals(a + b, actual);
}
```
