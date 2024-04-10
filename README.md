# AutoParams

[![CI](https://github.com/AutoParams/AutoParams/actions/workflows/ci.yml/badge.svg)](https://github.com/AutoParams/AutoParams/actions/workflows/ci.yml)
[![Publish](https://github.com/AutoParams/AutoParams/actions/workflows/publish.yml/badge.svg)](https://github.com/AutoParams/AutoParams/actions/workflows/publish.yml)

AutoParams is an arbitrary test data generator designed for parameterized tests in Java, drawing inspiration from AutoFixture.

Manually configuring test data can be cumbersome, especially when certain data is necessary but not critical to a specific test. AutoParams eliminates this hassle by automatically generating test arguments for your parameterized methods, allowing you to focus more on your domain-specific requirements.

Using AutoParams is straightforward. Simply annotate your parameterized test method with the `@AutoSource` annotation, in the same way you would use the `@ValueSource` or `@CsvSource` annotations. Once this is done, AutoParams takes care of generating appropriate test arguments automatically.

```java
@ParameterizedTest
@AutoSource
void parameterizedTest(int a, int b) {
    Calculator sut = new Calculator();
    int actual = sut.add(a, b);
    assertEquals(a + b, actual);
}
```

In the example above, the automatic generation of test data by AutoParams can potentially eliminate the need for triangulation in tests, streamlining the testing process.

AutoParams also simplifies the writing of test setup code. For instance, if you need to generate multiple review entities for a single product, you can effortlessly accomplish this using the `@Fix` annotation.

```java
@AllArgsConstructor
@Getter
public class Product {
    private final UUID id;
    private final String name;
    private final BigDecimal priceAmount;
}
```

```java
@AllArgsConstructor
@Getter
public class Review {
    private final UUId id;
    private final Product product;
    private final String comment;
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

For Maven, you can add the following dependency to your pom.xml:

```xml
<dependency>
  <groupId>io.github.autoparams</groupId>
  <artifactId>autoparams</artifactId>
  <version>4.6.0</version>
</dependency>
```

### Gradle

For Gradle, use:

```groovy
testImplementation 'io.github.autoparams:autoparams:4.6.0'
```

## Features

### Support for Primitive Types

AutoParams effortlessly generates test arguments for primitive data types, such as booleans, integers, and floats.

```java
@ParameterizedTest
@AutoSource
void testMethod(boolean x1, byte x2, int x3, long x4, float x5, double x6, char x7) {
}
```

### Handling Simple Objects

The framework also provides test arguments in the form of simple objects like Strings, UUIDs, and BigIntegers.

```java
@ParameterizedTest
@AutoSource
void testMethod(String x1, UUID x2, BigInteger x3) {
}
```

### Enum Type Support

Enum types are seamlessly integrated as well. AutoParams will randomly select values from the enum for test arguments.

```java
public enum Day {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY,
    THURSDAY, FRIDAY, SATURDAY
}
```

```java
@ParameterizedTest
@AutoSource
void testMethod(Day arg) {
}
```

### Generation of Complex Objects

For more complicated objects, AutoParams utilizes the public constructor with arbitrary arguments to generate test data. If the class has multiple public constructors, the framework will select the one with the fewest parameters to initialize the object.

```java
@AllArgsConstructor
@Getter
public class ComplexObject {
    private final int value1;
    private final String value2;
}
```

```java
@ParameterizedTest
@AutoSource
void testMethod(ComplexObject arg) {
}
```

#### Constructor Selection Policy

When AutoParams generates objects of complex types, it adheres to the following selection criteria for constructors:

1. Priority is given to constructors that are annotated with the `@ConstructorProperties` annotation.
1. If no such annotation is present, AutoParams will choose the constructor with the fewest parameters.

```java
@Getter
public class ComplexObject {

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
}
```

```java
@ParameterizedTest
@AutoSource
void testMethod(ComplexObject arg) {
    assertNotNull(object.getValue2());
    assertNull(object.getValue3());
}
```

### Support for Generic Types

AutoParams is capable of generating objects of generic types, adhering to its constructor selection policy. When dealing with generic types, AutoParams employs a public constructor with arbitrary arguments for object creation. If multiple public constructors are available, the framework opts for the one with the fewest parameters.

```java
@AllArgsConstructor
@Getter
public class GenericObject<T1, T2> {
    private final T1 value1;
    private final T2 value2;
}
```

```java
@ParameterizedTest
@AutoSource
void testMethod(
    GenericObject<String, ComplexObject> arg1,
    GenericObject<UUID, GenericObject<String, ComplexObject>> arg2) {
}
```

### Support for Collection Types

AutoParams offers extensive support for various collection types, enhancing its utility for more complex testing scenarios.

#### Arrays

AutoParams automatically generates array instances, each containing three elements by default.

```java
@ParameterizedTest
@AutoSource
void testMethod(int[] array1, String[] array2) {
}
```

#### List Types

Both the `List<E>` interface and the `ArrayList<E>` class are supported. AutoParams generates list objects that contain a few elements.

```java
@ParameterizedTest
@AutoSource
void testMethod(List<String> list, ArrayList<UUID> arrayList) {
}
```

#### Set Types

AutoParams also supports the `Set<E>` interface and the `HashSet<E>` class, generating set objects that contain a few elements.

```java
@ParameterizedTest
@AutoSource
void testMethod(Set<String> set, HashSet<UUID> hashSet) {
}
```

#### Map Interface

Support extends to the `Map<K, V>` interface and the `HashMap<K, V>` class as well. AutoParams generates map objects containing a few key-value pairs.

```java
@ParameterizedTest
@AutoSource
void testMethod(Map<String, ComplexObject> map, HashMap<UUID, ComplexObject> hashMap) {
}
```

This comprehensive support for collection types makes AutoParams an exceptionally versatile tool for generating test data, accommodating a wide variety of data structures to ensure thorough testing.

### Support for Stream Types

AutoParams extends its versatility by offering support for various types of stream interfaces, further broadening your testing capabilities.

#### Generic Stream Interface

AutoParams supports the generic `Stream<T>` interface and generates stream objects containing a few elements.

```java
@ParameterizedTest
@AutoSource
void testMethod(Stream<ComplexObject> stream) {
}
```

#### Stream Interfaces of Primitive Types

AutoParams also accommodates stream interfaces that are specific to primitive types, such as IntStream, LongStream, and DoubleStream.

```java
@ParameterizedTest
@AutoSource
void testMethod(IntStream arg1, LongStream arg2, DoubleStream arg3) {
}
```

This added layer of support for stream types allows AutoParams to be a comprehensive solution for generating test data across a wide range of data structures and types, making your testing more robust and efficient.

### Repeat Feature

The `@Repeat` annotation in AutoParams provides you with a straightforward way to repeat a unit test multiple times, each time with newly generated, arbitrary test data. This can be particularly useful when you want to ensure that a piece of code functions as expected across a variety of input conditions.

#### How to Use

To use the `@Repeat` feature, you'll need to attach the `@Repeat` annotation alongside the `@AutoSource` annotation on your parameterized test method. The repeat property of the `@Repeat` annotation takes an integer value, specifying the number of times the test should be repeated.

Here's an example:

```java
@ParameterizedTest
@AutoSource
@Repeat(10)
void testMethod(int a, int b) {
    // This test method will be executed ten times with different sets of 'a' and 'b' each time.
    Calculator sut = new Calculator();
    int actual = sut.add(a, b);
    assertEquals(a + b, actual);
}
```

In the example above, the test method testMethod will run ten times. Each run will have a new set of randomly generated values for a and b, thanks to the `@AutoSource` annotation. The `@Repeat(10)` ensures that this cycle happens ten times, enhancing the test coverage.

#### Benefits

1. **Diverse Test Data**: The `@Repeat` feature allows you to test your methods across a wider range of automatically generated input values.
1. **Reduced Manual Effort**: There's no need to manually specify multiple sets of test data or create loops within your tests.
1. **Increased Test Robustness**: Repeating tests multiple times with varying data can uncover edge cases and improve the reliability of your software.

By using the `@Repeat` feature, you can increase the comprehensiveness and reliability of your test suite with minimal additional effort.

### `@Fix` Annotation

AutoParams provides a `@Fix` annotation to let you "freeze" or fix the value of a generated argument. Once an argument is fixed using the `@Fix` annotation, `@AutoSource` will reuse this fixed value for any subsequent parameters of the same type within the same test method.

#### How to Use

Simply decorate the parameter in your test method with the `@Fix` annotation. This will instruct AutoParams to keep that parameter's value constant while generating new values for other parameters.

Here's an example:

```java
@AllArgsConstructor
@Getter
public class ValueContainer {
    private final String value;
}
```

```java
@ParameterizedTest
@AutoSource
void testMethod(@Fix String arg1, String arg2, ValueContainer arg3) {
    assertEquals(arg1, arg2);
    assertEquals(arg1, arg3.getValue());
}
```

In the above example, the value of `arg1` is fixed by the `@Fix` annotation. The test verifies that `arg1` is equal to `arg2` and also equal to the value property of `arg3`. As a result, `arg1`, `arg2`, and `arg3.getValue()` will all contain the same string value, thanks to the `@Fix` annotation.

#### Benefits

1. **Consistency**: `@Fix` ensures that certain values stay constant, making it easier to verify relations between different parameters in your tests.
1. **Simpler Test Logic**: Reusing a fixed value across multiple parameters simplifies your test logic and makes your tests easier to read and maintain.
1. **Control Over Randomness**: While `@AutoSource` provides the benefit of random value generation, `@Fix` gives you control when you need specific values to be consistent throughout a test.

The `@Fix` annotation is a powerful feature for scenarios where you need to maintain the consistency of certain test parameters while still benefiting from the randomness and coverage offered by AutoParams.

### `@ValueAutoSource` Annotation

The `@ValueAutoSource` annotation combines the capabilities of `@ValueSource` and `@AutoSource`, providing a more flexible way to generate test arguments. Specifically, it assigns a predefined value to the first parameter and then generates arbitrary values for the remaining parameters.

#### How to Use

To employ the `@ValueAutoSource` annotation, add it above your test method and specify the values for the first parameter using the attributes like `strings`, `ints`, etc., just as you would with `@ValueSource`.

Here's an example:

```java
@ParameterizedTest
@ValueAutoSource(strings = {"foo"})
void testMethod(String arg1, String arg2) {
    assertEquals("foo", arg1);
    assertNotEquals(arg1, arg2);
}
```

In this case, `arg1` is set to `"foo"`, while `arg2` will receive an automatically generated value. The test confirms that `arg1` is exactly `"foo"` and that `arg2` is different from `arg1`.

#### Compatibility with `@Fix`

The `@ValueAutoSource` annotation is fully compatible with the `@Fix` annotation, allowing you to fix certain parameter values while also providing predefined values for others.

Here is how they can work together:

```java
@AllArgsConstructor
@Getter
public class ValueContainer {
    private final String value;
}
```

```java
@ParameterizedTest
@ValueAutoSource(strings = {"foo"})
void testMethod(@Fix String arg1, String arg2, ValueContainer arg3) {
    assertEquals("foo", arg2);
    assertEquals("foo", arg3.getValue());
}
```

#### Benefits

1. **Combining Fixed and Random Values**: `@ValueAutoSource` allows you to specify certain parameter values while letting AutoParams generate random values for others, offering the best of both worlds.
1. **Enhanced Flexibility**: This feature grants you more control over test data, making it easier to cover a wide range of scenarios.
1. **Reduced Test Complexity**: By combining the functionalities of `@ValueSource` and `@AutoSource`, you can simplify your test setup, making it easier to read and maintain.

The `@ValueAutoSource` annotation is a versatile addition to your testing toolkit, giving you greater control over test data while maintaining the benefits of automated random data generation.

### `@CsvAutoSource` Annotation

The `@CsvAutoSource` annotation merges the features of `@CsvSource` and `@AutoSource`, providing you with the flexibility to specify CSV-formatted arguments for some parameters while generating random values for the remaining ones.

#### How to Use

To use `@CsvAutoSource`, annotate your test method with it and provide CSV-formatted input values for the initial set of parameters. The remaining parameters will be populated with automatically generated values.

Here's a simple example:

```java
@ParameterizedTest
@CsvAutoSource({"16, foo"})
void testMethod(int arg1, String arg2, String arg3) {
    assertEquals(16, arg1);
    assertEquals("foo", arg2);
    assertNotEquals(arg2, arg3);
}
```

In this example, `arg1` is set to `16` and `arg2` is set to `"foo"` based on the provided CSV input. Meanwhile, `arg3` will be assigned a randomly generated value. The test verifies that `arg3` is different from `arg2`.

#### Compatibility with `@Fix`

Just like `@ValueAutoSource`, `@CsvAutoSource` is fully compatible with the `@Fix` annotation, allowing you to lock in values for specific parameters while also defining others via CSV.

Here's how they can work together:

```java
@AllArgsConstructor
@Getter
public class ValueContainer {
    private final String value;
}
```

```java
@ParameterizedTest
@CsvAutoSource({"16, foo"})
void testMethod(int arg1, @Fix String arg2, ValueContainer arg3) {
    assertEquals("foo", arg3.getValue());
}
```

#### Benefits

1. **Dual Control**: The ability to specify values via CSV for some parameters while allowing automatic random generation for others provides a high level of control over your test data.
1. **Streamlined Testing**: By allowing both manual and automated data input, `@CsvAutoSource` simplifies the setup of complex tests.
1. **Flexible and Comprehensive**: This feature offers you the flexibility to cover a broader range of test scenarios by mixing pre-defined and random values.

The `@CsvAutoSource` annotation is another versatile tool in your testing suite, adding both precision and coverage to your parameterized tests.

### `@MethodAutoSource` Annotation

The `@MethodAutoSource` annotation blends the capabilities of `@MethodSource` and `@AutoSource`. This allows you to specify the names of factory methods for the initial set of parameters, while the remaining parameters are automatically populated with arbitrary values.

#### How to Use

To employ `@MethodAutoSource`, annotate your test method with it and specify the factory method that provides the argument values for the initial parameters. AutoParams will generate values for the remaining parameters.

Here's a straightforward example:

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

In this instance, `arg1` and `arg2` are set based on the values provided by the `factoryMethod`. `arg3` will be assigned a randomly generated value, and the test confirms that `arg3` is not equal to `arg2`.

#### Compatibility with `@Fix`

The `@MethodAutoSource` annotation is also fully compatible with the `@Fix` annotation. This lets you lock in values for particular parameters while dynamically populating others through a factory method.

Here's how it works together:

```java
@AllArgsConstructor
@Getter
public class ValueContainer {
    private final String value;
}
```

```java
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

#### Benefits

1. **Customizability**: The ability to specify factory methods for some parameters allows for highly tailored test setups.
1. **Simplicity**: With automatic value generation for remaining parameters, @MethodAutoSource alleviates the need for extensive manual data preparation.
1. **Flexibility**: This feature allows you to test a wider range of scenarios by combining pre-defined and random values.

The `@MethodAutoSource` annotation enriches your testing toolkit, offering a balanced blend of control and randomness in your parameterized tests.

### Setting the Range of Values

You can constrain the range of generated arbitrary values for certain parameters using the `@Min` and `@Max` annotations. These annotations allow you to set minimum and maximum bounds, respectively, ensuring that the generated values fall within the specified range.

#### How to Use

To set the range for a parameter, simply annotate it with `@Min` to define the minimum value and `@Max` to define the maximum value.

Here's an example:

```java
@ParameterizedTest
@AutoSource
void testMethod(@Min(1) @Max(10) int value) {
    assertTrue(value >= 1);
    assertTrue(value <= 10);
}
```

In this test, the value parameter will always be an integer between `1` and `10`, inclusive.

#### Supported types

The `@Min` and `@Max` annotations are compatible with the following types:

- `byte`
- `Byte`
- `short`
- `Short`
- `int`
- `Integer`
- `long`
- `Long`
- `float`
- `Float`
- `double`
- `Double`

#### Benefits

1. **Controlled Randomness**: These annotations help you fine-tune the scope of randomness, allowing for more targeted and meaningful tests.
1. **Reduced Test Flakiness**: By constraining the range of values, you reduce the risk of encountering edge cases that could make your tests flaky.
1. **Enhanced Readability**: Using `@Min` and `@Max` makes it clear to readers what range of values are being tested, thereby improving the readability and maintainability of your test code.

By using the `@Min` and `@Max` annotations in conjunction with `@AutoSource`, you can achieve a balanced mix of randomness and predictability, making your parameterized tests both versatile and reliable.

### `@Customization` Annotation

The `@Customization` annotation allows you to tailor the generation of test data according to specific business rules or requirements. This powerful feature integrates seamlessly with the AutoParams framework, offering the flexibility to apply custom logic to your parameterized tests.

#### Business Rule Example

Let's consider a Product entity, which has some business rules to follow:

- The listPriceAmount must be greater than or equal to 100
- The listPriceAmount must be less than or equal to 1000
- A 10% discount should be offered, reflected in sellingPriceAmount

```java
@AllArgsConstructor
@Getter
public class Product {
    private final UUID id;
    private final String name;
    private final BigDecimal listPriceAmount;
    private final BigDecimal sellingPriceAmount;
}
```

#### Customizing Object Generation

You can implement these rules using the `Customizer` interface:

```java
public class ProductCustomization implements Customizer {

    @Override
    public ObjectGenerator customize(ObjectGenerator generator) {
        return (query, context) -> query.getType().equals(Product.class)
            ? new ObjectContainer(factory(context))
            : generator.generate(query, context);
    }

    private Product factory(ResolutionContext context) {
        UUID id = context.generate(UUID.class);
        String name = context.generate(String.class);

        ThreadLocalRandom random = ThreadLocalRandom.current();
        BigDecimal listPriceAmount = new BigDecimal(random.nextInt(100, 1000 + 1));
        BigDecimal sellingPriceAmount = listPriceAmount.multiply(new BigDecimal(0.9));

        return new Product(id, name, listPriceAmount, sellingPriceAmount);
    }
}
```
#### Applying Customization to Test Method

Annotate your test method to apply the customization:

```java
@ParameterizedTest
@AutoSource
@Customization(ProductCustomization.class)
void testMethod(Product arg) {
    assertTrue(arg.getSellingPriceAmount().compareTo(arg.getListPriceAmount()) < 0);
}
```

#### Composite Customization

You can also create composite customizations to apply multiple custom rules:

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

And use it like this:

```java
@ParameterizedTest
@AutoSource
@Customization(DomainCustomization.class)
void testMethod(Email email, User user, Supplier supplier, Product product) {
}
```

#### Settable Properties

If your object follows the JavaBeans spec and has settable properties, you can use `InstancePropertyCustomizer`:

```java
@Getter
@Setter
public class User {
    private Long id;
    private String name;
}
```

```java
@ParameterizedTest
@AutoSource
@Customization(InstancePropertyCustomizer.class)
void testMethod(User user) {
    assertNotNull(user.getId());
    assertNotNull(user.getName());
}
```

#### Customization Scoping

The `@Customization` annotation can also be applied to individual parameters within a test method. Once applied, the customization will affect all following parameters, unless overridden.

This feature provides a nuanced approach to data generation, enabling highly specialized and context-sensitive test scenarios.

## autoparams-mockito

autoparams-mockito is an extension of the AutoParams library that facilitates the creation of mocks for interfaces and abstract classes using Mockito, a popular mocking framework in Java. By integrating these two libraries, you can seamlessly generate mock objects for your tests.

### Install

#### Maven

For Maven, you can add the following dependency to your pom.xml:

```xml
<dependency>
  <groupId>io.github.autoparams</groupId>
  <artifactId>autoparams-mockito</artifactId>
  <version>4.6.0</version>
</dependency>
```

#### Gradle

For Gradle, use:

```groovy
testImplementation 'io.github.autoparams:autoparams-mockito:4.6.0'
```

### Generating Test Doubles with Mockito

Consider a situation where you have an interface that abstracts certain services:

```java
public interface Dependency {

    String getName();
}
```

You also have a system that relies on this `Dependency` interface:

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

To generate mock objects for interfaces or abstract classes, the autoparams-mockito extension provides the `MockitoCustomizer`. When you decorate your test method with `@Customization(MockitoCustomizer.class)`, the `@AutoSource` annotation will employ Mockito to create mock values for the specified parameters.

Here's how you can apply this in practice:

```java
@ParameterizedTest
@AutoSource
@Customization(MockitoCustomizer.class)
void testUsingMockito(@Fix Dependency stub, SystemUnderTest sut) {
    when(stub.getName()).thenReturn("World");
    assertEquals("Hello World", sut.getMessage());
}
```

In the above example:

- The `stub` argument is a mock generated by Mockito, thanks to the `MockitoCustomizer`.
- The `@Fix` annotation ensures that this mock object (`stub`) is reused as a parameter for the construction of the `SystemUnderTest` object (`sut`).

This integration simplifies the creation of mock objects for parameterized tests, making testing more efficient and straightforward.

## autoparams-lombok

autoparams-lombok is an extension for the AutoParams library that makes it easier to work with [Project Lombok](https://projectlombok.org/), a library that reduces boilerplate code in Java applications.

### Install

#### Maven

For Maven, you can add the following dependency to your pom.xml:

```xml
<dependency>
  <groupId>io.github.autoparams</groupId>
  <artifactId>autoparams-lombok</artifactId>
  <version>4.6.0</version>
</dependency>
```

#### Gradle

For Gradle, use:

```groovy
testImplementation 'io.github.autoparams:autoparams-lombok:4.6.0'
```

### `BuilderCustomizer`

When working with `@Builder` annotation, you can use the `BuilderCustomizer` to facilitate generating arbitrary objects for your tests. Here's an example:

Suppose you have a `User` class like so:

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

You can use BuilderCustomizer to create objects of type User for your tests:

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

#### Custom Method Names

If you've customized the builder method names using `builderMethodName` and `buildMethodName` in your Lombok `@Builder`, you'll need to create a subclass of `BuilderCustomizer` to handle the custom names:

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

Here's how you can extend `BuilderCustomizer`:

```java
public class UserBuilderCustomizer extends BuilderCustomizer {

    public UserBuilderCustomizer() {
        super("getBuilder", "createUser");
    }
}
```

Now, you can use your customized `UserBuilderCustomizer` in your tests:

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

This allows you to keep the benefits of using `@Builder` annotation while gaining the automatic generation capabilities provided by AutoParams.

## autoparams-kotlin

autoparams-kotlin is an extension designed to simplify and enhance the experience when working with Kotlin. 

### Install

#### Maven

For Maven, you can add the following dependency to your pom.xml:

```xml
<dependency>
  <groupId>io.github.autoparams</groupId>
  <artifactId>autoparams-kotlin</artifactId>
  <version>4.6.0</version>
</dependency>
```

#### Gradle

For Gradle, use:

```groovy
testImplementation 'io.github.autoparams:autoparams-kotlin:4.6.0'
```

### `@AutoKotlinSource` Annotation

Consider the example of a Point data class in Kotlin:

```kotlin
data class Point(val x: Int = 0, val y: Int = 0)
```

In typical test scenarios, you might want to ensure the parameters passed to your test methods aren't just default or predictable values. The autoparams-kotlin extension can assist in such cases.

Below is a demonstration using the `@AutoKotlinSource` annotation provided by autoparams-kotlin. This annotation automatically generates random parameters for your test methods:

```kotlin
@ParameterizedTest
@AutoKotlinSource
fun testMethod(point: Point) {
    assertThat(point.x).isNotEqualTo(0)
    assertThat(point.y).isNotEqualTo(0)
}
```

In this example, the `testMethod` doesn't receive a hardcoded `Point` object. Instead, thanks to the `@AutoKotlinSource` annotation, a randomized `Point` object is passed in, making the test more robust by ensuring it isn't biased by predetermined values.
