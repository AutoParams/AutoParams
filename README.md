# AutoParams

[![CI](https://github.com/AutoParams/AutoParams/actions/workflows/ci.yml/badge.svg)](https://github.com/AutoParams/AutoParams/actions/workflows/ci.yml)
[![Publish](https://github.com/AutoParams/AutoParams/actions/workflows/publish.yml/badge.svg)](https://github.com/AutoParams/AutoParams/actions/workflows/publish.yml)

AutoParams is an arbitrary test data generator for parameterized tests in Java inspired by AutoFixture.

Sometimes setting all the test data manually is very annoying. Sometimes some test data is required but not that important for a particular test. AutoParams automatically generates test arguments for your parameterized test method so you can focus more on your domain and your requirements.

AutoParams is very easy to use. Just decorate your parameterized test method with the `@AutoSource` annotation just like using the `@ValueSource` annotation or the `@CsvSource` annotation. That's all. Then AutoParams will generate arbitrary arguments for the parameters of the test method automatically.

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

AutoParams makes it simpler to write test setup code. For example, if you need to create a few review entities for the same product, you can easily do it using the `@Fix` annotation.

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
void testMethod(@Fix Product product, Review[] reviews) {
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
  <groupId>io.github.autoparams</groupId>
  <artifactId>autoparams</artifactId>
  <version>1.1.1</version>
</dependency>
```

### Gradle

```groovy
testImplementation 'io.github.autoparams:autoparams:1.1.1'
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

1. The constructor decorated with the `@ConstructorProperties` annotation is preferentially selected.
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

The `List<E>` interface and the `ArrayList<E>` class supported. Generated list objects contain few elements.

```java
@ParameterizedTest
@AutoSource
void testMethod(List<String> list, ArrayList<UUID> arrayList) {
}
```

#### Set Types

The `Set<E>` interface and the `HashSet<E>` class supported. Generated set objects contain few elements.

```java
@ParameterizedTest
@AutoSource
void testMethod(Set<String> set, HashSet<UUID> hashSet) {
}
```

#### Map Interface

The `Map<K, V>` interface and the `HashMap<K, V>` class supported. Generated map objects contain few pairs.

```java
@ParameterizedTest
@AutoSource
void testMethod(Map<String, ComplexObject> map, HashMap<UUID, ComplexObject> hashMap) {
}
```

### Streams Types

#### Generic Stream Interface

AutoParams supports the generic the `Stream<T>` interface. Generated stream objects provide few elements.

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

### Repeat

Unit tests can be repeated with arbitrary test data. Set the `repeat` property of the `@AutoSource` annotation as many times as you want to repeat.

```java
@ParameterizedTest
@AutoSource
@Repeat(10)
void testMethod(int a, int b) {
    // This test method is performed ten times.
    Calculator sut = new Calculator();
    int actual = sut.add(a, b);
    assertEquals(a + b, actual);
}
```

### `@Fix` annotation

You can freeze the generated argument to the type with the `@Fix` annotation. `@AutoSource` reuses the argument of the parameter decorated with the `@Fix` annotation for subsequent value generation.

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
void testMethod(@Fix String arg1, String arg2, ValueContainer arg3) {
    assertEquals(arg1, arg2);
    assertEquals(arg1, arg3.getValue());
}
```

### `@ValueAutoSource` annotation

The `@ValueAutoSource` annotation combines the functionalities of `@ValueSource` and `@AutoSource`. It assigns the provides value to the first parameter then generates arbitrary values for other parameters.

```java
@ParameterizedTest
@ValueAutoSource(strings = {"foo"})
void testMethod(String arg1, String arg2) {
    assertEquals("foo", arg1);
    assertNotEquals(arg1, arg2);
}
```

The `@Fix` annotation correctly works with `@ValueAutoSource`.

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
void testMethod(@Fix String arg1, String arg2, ValueContainer arg3) {
    assertEquals("foo", arg2);
    assertEquals("foo", arg3.getValue());
}
```

### `@CsvAutoSource` annotation

The `@CsvAutoSource` annotation combines the functionalities of `@CsvSource` and `@AutoSource`. You can specifiy arguments in CSV for the forepart parameters. The remaining parameters will be assigned with the arbitrary values.

```java
@ParameterizedTest
@CsvAutoSource({"16, foo"})
void testMethod(int arg1, String arg2, String arg3) {
    assertEquals(16, arg1);
    assertEquals("foo", arg2);
    assertNotEquals(arg2, arg3);
}
```

The `@Fix` annotation correctly works with `@CsvAutoSource`.

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
void testMethod(int arg1, @Fix String arg2, ValueContainer arg3) {
    assertEquals("foo", arg3.getValue());
}
```

### `@MethodAutoSource` annotation

The `@MethodAutoSource` annotation combines the functionalities of `@MethodSource` and `@AutoSource`. You can specifiy names of factory method for the forepart parameters. The remaining parameters will be assigned with the arbitrary values.

```java
@ParameterizedTest
@MethodAutoSource("factoryMethod")
void testMethod(int arg1, String arg2, String arg3) {
    assertEquals(16, arg1);
    assertEquals("foo", arg2);
    assertNotEquals(arg2, arg3);
}

static Stream<Arguments> factoryMethod() {
    return Stream.of(
        Arguments.arguments(16, "foo")
    );
}
```

The `@Fix` annotation correctly works with `@MethodAutoSource`.

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
@MethodAutoSource("factoryMethod")
void testMethod(int arg1, @Fix String arg2, ValueContainer arg3) {
    assertEquals("foo", arg3.getValue());
}

static Stream<Arguments> factoryMethod() {
    return Stream.of(
        Arguments.arguments(16, "foo")
    );
}
```

### Setting the range of values

You can specify the range of arbitrary values using the `@Min` annotation and the `@Max` annotations.

```java
@ParameterizedTest
@AutoSource
void testMethod(@Min(1) @Max(10) int value) {
    assertTrue(value >= 1);
    assertTrue(value <= 10);
}
```

#### Supported types

- `int`
- `Integer`
- `float`
- `Float`
- `double`
- `Double`

### `@Customization` annotation

The `@Customization` annotation is a powerful feature. You can use this annotation to apply your business rules to test data generation. Use the `Customizer` interface to code your business rules, then decorate the test method with the `@Customization` annotation.

You have the `Product` entity that represents the product you want to sell to your customers.

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

You have following business rules for the `Product` entity.

- `listPriceAmount` is greater than or equal to `100`
- `listPriceAmount` is less than or equal to `1000`
- Offer a 10% discount

Code these rules with the `Customizer` interface.

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

`CompositeCustomizer` may help you to combine each business rules easily.

```java
public class DomainCustomization extends CompositeCustomzer {
    public DomainCustomization() {
        super(
            new EmailCustomization(),
            new UserCustomization(),
            new SupplierCustomization(),
            new ProductCustomization()
        );
    }
}
```

```java
@ParameterizedTest
@AutoSource
@Customization(DomainCustomization.class)
void testMethod(Email email, User user, Supplier supplier, Product product) {
}
```

If your object has settable properties, such as if you follow the JavaBeans spec, `SettablePropertyWriter` will help.

```java
public class User {

    private Long id;
    private String name;

    public Long getId() { return id; }
    public void setId(Long value) { id = value; }

    public String getName() { return name; }
    public void setName(String value) { name = value; }

}
```

```java
@ParameterizedTest
@AutoSource
@Customization(SettablePropertyWriter.class)
void testMethod(User user) {
    assertNotNull(user.getId());
    assertNotNull(user.getName());
}
```

`@Customization` annotation can be applied to parameters of test methods. After `Customization` has been applied to a parameter, it also affects the following parameters.

## `autoparams-mockito`

`autoparams-mockito` helps `@AutoSource` generate arguments of interfaces and abstract classes using Mockito.

### Install

#### Maven

```xml
<dependency>
  <groupId>io.github.autoparams</groupId>
  <artifactId>autoparams-mockito</artifactId>
  <version>1.1.1</version>
</dependency>
```

#### Gradle

```groovy
testImplementation 'io.github.autoparams:autoparams-mockito:1.1.1'
```

### How to generate test doubles using Mockito

There is an interface that abstracts some service.

```java
public interface Dependency {

    String getName();

}
```

And you have a system that depends on the `Dependency` interface.

```java
public class SystemUnderTest {

    private final Dependency dependency;

    public SystemUnderTest(Dependency dependency) {
        this.dependency = dependency;
    }

    public String getMessage() {
        return "Hello " + dependency.getName();
    }

}
```

If you decorates your test method with `autoparams.mockito.MockitoCustomizer`, then `@AutoSource` will generate values for parameters of interfaces and abstract classes using Mockito.

In the following example, the argument `stub` is generated using Mockito by `MockitoCustomizer` and the parameter `stub` is decorated with the `@Fix` annotation so it is injected to the object `sut`.

```java
@ParameterizedTest
@AutoSource
@Customization(MockitoCustomizer.class)
void testUsingMockito(@Fix Dependency stub, SystemUnderTest sut) {

    when(stub.getName()).thenReturn("World");

    assertEquals("Hello World", sut.getMessage());

}
```

## `autoparams-lombok`

`autoparams-lombok` helps `@AutoSource` generate arguments of types using annotations in [Project Lombok](https://projectlombok.org/).

### Install

#### Maven

```xml
<dependency>
  <groupId>io.github.autoparams</groupId>
  <artifactId>autoparams-lombok</artifactId>
  <version>1.1.1</version>
</dependency>
```

#### Gradle

```groovy
testImplementation 'io.github.autoparams:autoparams-lombok:1.1.1'
```

### `BuilderCustomizer`

You can generate arbitrary objects of types decorated with `@Builder` annotation. Decorates your test method with `autoparams.lombok.BuilderCustomizer`.

```java
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class User {

    private Long id;
    private String name;
    private String email;

}
```

```java
@ParameterizedTest
@AutoSource
@Customization(BuilderCustomizer.class)
void testMethod(User user) {
    assertThat(arg.getId()).isNotNull();
    assertThat(arg.getName()).isNotNull();
    assertThat(arg.getEmail()).isNotNull();
}
```

If you configured `builderMethodName` and `buildMetodName` you should write the customizer that inherits `BuilderCustomizer` class.

```java
import lombok.Builder;
import lombok.Getter;

@Builder(builderMethodName = "getBuilder", buildMethodName = "createUser")
@Getter
public class User {

    private Long id;
    private String name;
    private String email;

}
```

```java
public class UserBuilderCustomizer extends BuilderCustomizer {

    public UserBuilderCustomizer() {
        super("getBuilder", "createUser");
    }

}
```

```java
@ParameterizedTest
@AutoSource
@Customization(UserBuilderCustomizer.class)
void testMethod(User user) {
    assertThat(arg.getId()).isNotNull();
    assertThat(arg.getName()).isNotNull();
    assertThat(arg.getEmail()).isNotNull();
}
```
