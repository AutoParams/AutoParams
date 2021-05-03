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

AutoParams makes it simpler to write test setup code. For example, if you need to create a few review entities for the same product, you can easily do it using `@Fixed` annotation.

```java
public class Product {

    private final UUID id;
    private final String name;
    private final BigDecimal priceAmount;

    public Product(UUID id, String name, BigDecimal priceAmount) {
        this.id = id;
        this.name = name;
        this.priceAmount = priceAmount;
    }

    public UUID getId() { return id; }

    public String getName() { return name; }

    public BigDecimal getPriceAmount() { return priceAmount; }

}

public class Review {

    private final UUId id;
    private final Product product;
    private final String comment;

    public Review(UUID id, Product product, String comment) {
        this.id = id;
        this.product = product;
        this.comment = comment;
    }

    public UUID getId() { return id; }

    public Product getProduct() { return product; }

    public String getComment() { return comment; }

}
```

```java
@ParameterizedTest
@AutoSource
void testMethod(@Fixed Product product, Review[] reviews) {
    for (Review review : reviews) {
        assertSame(product, review.getProduct());
    }
}
```

That's cool!

## Requirements

- JDK 1.8 or higher

## Install

### Maven

```xml
<dependency>
  <groupId>io.github.javaunit</groupId>
  <artifactId>autoparams</artifactId>
  <version>0.2.0</version>
</dependency>
```

### Gradle

```groovy
testImplementation 'io.github.javaunit:autoparams:0.2.0'
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

AutoParams supports a variety of collection types.

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

### `@Fixed` annotation

You can freeze the generated argument to the type with `@Fixed` annotation. `@AutoSource` reuses the argument of the parameter decorated with `@Fixed` annotation for subsequent value generation.

```java
class ValueContainer {

    private final String value;

    public ValueContainer(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}

@ParameterizedTest
@AutoSource
void testMethod(@Fixed String arg1, String arg2, ValueContainer arg3) {
    assertEquals(arg1, arg2);
    assertEquals(arg1, arg3.getValue());
}
```

### `@ValueAutoSource` annotation

`@ValueAutoSource` annotation combines the functionalities of `@ValueSource` and `@AutoSource`. It assigns the provides value to the first parameter then generates arbitrary values for other parameters.

```java
@ParameterizedTest
@ValueAutoSource(strings = {"foo"})
void testMethod(String arg1, String arg2) {
    assertEquals("foo", arg1);
    assertNotEquals(arg1, arg2);
}
```

`@Fixed` annotation correctly works with `@ValueAutoSource`.

```java
class ValueContainer {

    private final String value;

    public ValueContainer(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}

@ParameterizedTest
@ValueAutoSource(strings = {"foo"})
void testMethod(@Fixed String arg1, String arg2, ValueContainer arg3) {
    assertEquals("foo", arg2);
    assertEquals("foo", arg3.getValue());
}
```

### `@CsvAutoSource` annotation

`@CsvAutoSource` annotation combines the functionalities of `@CsvSource` and `@AutoSource`. You can specifiy arguments in CSV for the forepart parameters. The remaining parameters will be assigned with the arbitrary values.

```java
@ParameterizedTest
@CsvAutoSource({"16, foo"})
void testMethod(int arg1, String arg2, String arg3) {
    assertEquals(16, arg1);
    assertEquals("foo", arg2);
    assertNotEquals(arg2, arg3);
}
```

`@Fixed` annotation correctly works with `@CsvAutoSource`.

```java
class ValueContainer {

    private final String value;

    public ValueContainer(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}

@ParameterizedTest
@CsvAutoSource({"16, foo"})
void testMethod(int arg1, @Fixed String arg2, ValueContainer arg3) {
    assertEquals("foo", arg3.getValue());
}
```

### `@Customization` annotation

`@Customization` annotation is a powerful feature. You can use this annotation to apply your business rules to test data generation. Use `Customizer` interface to code your business rules, then decorate the test method with `@Customization` annotation.

You have `Product` entity that represents the product you want to sell to your customers.

```java
public class Product {

    private final UUID id;
    private final String name;
    private final BigDecimal listPriceAmount;
    private final BigDecimal sellingPriceAmount;

    public Product(UUID id, String name, BigDecimal listPriceAmount, BigDecimal sellingPriceAmount) {
        this.id = id;
        this.name = name;
        this.listPriceAmount = listPriceAmount;
        this.sellingPriceAmount = sellingPriceAmount;
    }

    public UUID getId() { return id; }

    public String getName() { return name; }

    public BigDecimal getListPriceAmount() { return listPriceAmount; }

    public BigDecimal getSellingPriceAmount() { return sellingPriceAmount; }

}
```

You have following business rules for `Product` entity.

- `listPriceAmount` is greater than or equal to `100`
- `listPriceAmount` is less than or equal to `1000`
- Offer a 10% discount

Code these rules with `Customizer` interface.

```java
public class ProductCustomization implements Customizer {

    public ObjectGenerator customize(ObjectGenerator generator) {
        return (query, context) -> query.getType().equals(Product.class)
            ? new ObjectContainer(factory(context))
            : generator.generate(query, context);
    }

    private Product factory(ObjectGenerationContext context) {
        UUID id = (UUID) context.generate(() -> UUID.class);
        String name = (String) context.generate(() -> String.class);

        BigDecimal listPriceAmount = new BigDecimal(ThreadLocalRandom.current().nextInt(100, 1000 + 1));
        BigDecimal sellingPriceAmount = listPriceAmount.multiply(new BigDecimal(0.9));

        return new Product(id, name, listPriceAmount, sellingPriceAmount);
    }

}
```

Now decorate your test method with `ProductCustomization` and the generated `Product` object will satisfy your business rules.

```java
@ParameterizedTest
@AutoSource
@Customization(ProductCustomization.class)
void testMethod(Product product) {
    assertTrue(product.getSellingPriceAmount().compareTo(product.getListPriceAmount()) < 0);
}
```
