# AutoParams

Arbitrary test data generator for parameterized tests in Java.

[![CI](https://github.com/JavaUnit/AutoParams/actions/workflows/ci.yml/badge.svg)](https://github.com/JavaUnit/AutoParams/actions/workflows/ci.yml)

---

## Install

### Maven

```xml
<dependency>
  <groupId>io.github.javaunit</groupId>
  <artifactId>autoparams</artifactId>
  <version>0.0.1</version>
</dependency>
```

### Gradle

```groovy
implementation 'io.github.javaunit:autoparams:0.0.1'
```

---

## Features

### Generate arbitrary test data of primitive types

```java
@ParameterizedTest
@AutoSource
void myTestMethod(boolean x1, int x2, long x3, float x4, double x5) {
}
```

### Generate arbitrary simple objects

```java
@ParameterizedTest
@AutoSource
void myTestMethod(String x1, UUID x2) {
}
```

### Generate arbitrary complex objects

```java

class ComplexObject {

    private final int value1;
    private final Striong value2;

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
void myTestMethod(ComplexObject x1) {
}
```

### Generate generic objects

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
void myTestMethod(
    GenericObject<String, ComplexObject> x1,
    GenericObject<UUID, GenericObject<String, ComplexObject>> x2) {
}
```

### Generate collections and streams

#### Array

```java
@ParameterizedTest
@AutoSource
void myTestMethod(int[] array1, ComplexObject[] array2) {
}
```

#### List

```java
@ParameterizedTest
@AutoSource
void myTestMethod(List<ComplexObject> list) {
}
```

#### Set

```java
@ParameterizedTest
@AutoSource
void myTestMethod(Set<UUID> set) {
}
```

#### Map

```java
@ParameterizedTest
@AutoSource
void myTestMethod(Map<UUID, ComplexObject> map) {
}
```

#### Stream

```java
@ParameterizedTest
@AutoSource
void myTestMethod(Stream<ComplexObject> stream) {
}
```
