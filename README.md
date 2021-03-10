# AutoParams

Arbitrary test data generator for parameterized tests in Java.

## Features

### Generate arbitrary test data of primitive types

```java
@ParameterizedTest
@AutoSource
void myTestMethod(boolean x1, int x2, float x3, double x4) {
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

### Generate arrays

```java
@ParameterizedTest
@AutoSource
void myTestMethod(int[] array1, ComplexObject array2) {
}
```
