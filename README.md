# AutoParams

[![CI](https://github.com/AutoParams/AutoParams/actions/workflows/ci.yml/badge.svg)](https://github.com/AutoParams/AutoParams/actions/workflows/ci.yml)
[![Publish](https://github.com/AutoParams/AutoParams/actions/workflows/publish.yml/badge.svg)](https://github.com/AutoParams/AutoParams/actions/workflows/publish.yml)

**AutoParams** is a JUnit 5 extension for automatic test data generation, inspired by AutoFixture.

Manually creating test data is often repetitive and distracts from the core logic of the test. AutoParams eliminates this boilerplate by supplying automatically generated values to your test method parameters, allowing you to write concise and focused tests.

Getting started is simple: annotate your `@Test` method with `@AutoParams`, and the parameters will be populated with generated data.

```java
@Test
@AutoParams
void testMethod(int a, int b) {
    Calculator sut = new Calculator();
    int actual = sut.add(a, b);
    assertEquals(a + b, actual);
}
```

In the example above, `a` and `b` are automatically generated, making the test cleaner and reducing the need for manual setup or value triangulation.

AutoParams also supports more advanced scenarios. When multiple generated values need to share a reference—such as reviews referring to the same product—you can use the `@Freeze` annotation to ensure consistency.

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

    private final UUID id;
    private final UUID reviewerId;
    private final Product product;
    private final int rating;
    private final String comment;
}
```

```java
@Test
@AutoParams
void testMethod(@Freeze Product product, Review[] reviews) {
    for (Review review : reviews) {
        assertSame(product, review.getProduct());
    }
}
```

This ensures that all generated `Review` instances refer to the same frozen `Product`, simplifying test setup in scenarios involving shared dependencies.

## Requirements

- JDK 1.8 or higher

## Install

### Maven

For Maven, you can add the following dependency to your pom.xml:

```xml
<dependency>
  <groupId>io.github.autoparams</groupId>
  <artifactId>autoparams</artifactId>
  <version>10.0.0</version>
</dependency>
```

### Gradle

For Gradle, use:

```groovy
testImplementation 'io.github.autoparams:autoparams:10.0.0'
```

## Features

AutoParams provides a set of features designed to make your tests more expressive and reduce repetitive setup. Here are some of its key capabilities:

### `@Freeze` Annotation

The `@Freeze` annotation allows you to "freeze" a generated value so that it is reused consistently across other parameters of the same type within a single test method.

To apply it, annotate one of your method parameters with `@Freeze`. AutoParams will then propagate that frozen value to all other parameters of the same type—including nested fields in complex objects.

```java
@AllArgsConstructor
@Getter
public class StringContainer {

    private final String value;
}
```

```java
@Test
@AutoParams
void testMethodFreeze(String s1, @Freeze String s2, String s3, StringContainer container) {
    assertNotEquals(s1, s2);
    assertEquals(s2, s3);
    assertEquals(s2, container.getValue());
}
```

In this example, `s2` is frozen, meaning the same string value is reused for `s3` and for the `value` field inside `container`. Meanwhile, `s1` is independently generated.

This makes `@Freeze` particularly useful when certain dependencies in your test need to remain consistent, while still allowing variation in the rest of the test data.

### Setting the Range of Values

You can constrain the range of automatically generated values using the `@Min` and `@Max` annotations. These let you define minimum and maximum bounds for numeric parameters, ensuring that generated values fall within a specified range.

To apply a range, annotate the parameter with `@Min` and/or `@Max` as needed.

Here's an example:

```java
@Test
@AutoParams
void testMethod(@Min(1) @Max(10) int value) {
    assertTrue(value >= 1);
    assertTrue(value <= 10);
}
```

In this test, the `value` parameter will always be an integer between `1` and `10`, inclusive.

The `@Min` and `@Max` annotations are compatible with the following types:

- `byte`
- `java.lang.Byte`
- `short`
- `java.lang.Short`
- `int`
- `java.lang.Integer`
- `long`
- `java.lang.Long`
- `float`
- `java.lang.Float`
- `double`
- `java.lang.Double`

By combining `@Min` and `@Max` with `@AutoParams`, you can strike a balance between randomness and control, making your parameterized tests more robust and predictable.

### `ResolutionContext` class

The `ResolutionContext` class provides the core mechanism for generating test data. While it is used internally by AutoParams, you can also instantiate and use it directly in your own test code when needed.

Here's an example:

```java
@Test
void testMethod() {
    ResolutionContext context = new ResolutionContext();
    Product product = context.resolve();
    Review review = context.resolve();
}
```

In this example, `ResolutionContext` is used to manually generate instances of `Product` and `Review` outside of the `@AutoParams` annotation. This offers more control and flexibility for writing custom test logic or handling special cases.

### `Factory<T>` class

The `Factory<T>` class is useful when you need to generate multiple instances of the same type. It allows you to create single instances or collections of generated objects on demand.

Here's an example:

```java
@Test
void testMethod() {
    Factory<Product> factory = Factory.create(Product.class);
    Product product = factory.get();
    List<Product> products = factory.getRange(10);
}
```

In this example, a `Factory<Product>` is created to produce `Product` instances. The `get()` method creates a single instance, while `getRange(n)` returns a list of `n` instances. This approach is particularly helpful when you need bulk data generation in your tests.

### Customization

Customization is one of the most powerful features offered by AutoParams. It gives you full control over how test data is generated, allowing you to enforce business rules or tailor the data to meet specific testing needs.

For example, suppose the `Product` entity must follow these business rules:

- `priceAmount` must be greater than or equal to `10`
- `priceAmount` must be less than or equal to `10000`

You can implement these rules using a custom generator by extending `ObjectGeneratorBase<T>`:

```java
public class ProductGenerator extends ObjectGeneratorBase<Product> {

    @Override
    protected Product generateObject(ObjectQuery query, ResolutionContext context) {
        UUID id = context.resolve();
        String name = context.resolve();

        ThreadLocalRandom random = ThreadLocalRandom.current();
        BigDecimal priceAmount = new BigDecimal(random.nextInt(10, 10000 + 1));

        return new Product(id, name, priceAmount);
    }
}
```

This custom generator creates a `Product` instance that adheres to the business constraints. It uses `ResolutionContext` to generate supporting values like `id` and `name`, and applies explicit logic to generate a valid `priceAmount`.

You can apply this custom generator using the `@Customization` annotation:

```java
@Test
@AutoParams
@Customization(ProductGenerator.class)
void testMethod(Product product) {
    assertTrue(product.getPriceAmount().compareTo(BigDecimal.valueOf(10)) >= 0);
    assertTrue(product.getPriceAmount().compareTo(BigDecimal.valueOf(10000)) <= 0);
}
```

In this test, AutoParams uses `ProductGenerator` to ensure that the `Product` instance respects the required pricing constraints.

You can also apply multiple custom generators at once by listing them in the `@Customization` annotation:

```java
public class ReviewGenerator extends ObjectGeneratorBase<Review> {

    @Override
    protected Review generateObject(ObjectQuery query, ResolutionContext context) {
        UUID id = context.resolve();
        UUID reviewerId = context.resolve();
        Product product = context.resolve();
        String comment = context.resolve();

        ThreadLocalRandom random = ThreadLocalRandom.current();
        int rating = random.nextInt(1, 5 + 1);

        return new Review(id, reviewerId, product, rating, comment);
    }
}
```

```java
@Test
@AutoParams
@Customization({ ProductGenerator.class, ReviewGenerator.class })
void testMethod(Product product, Review review) {
    assertTrue(product.getPriceAmount().compareTo(BigDecimal.valueOf(10)) >= 0);
    assertTrue(product.getPriceAmount().compareTo(BigDecimal.valueOf(10000)) <= 0);
    assertTrue(review.getRating() >= 1);
    assertTrue(review.getRating() <= 5);
}
```

Alternatively, if you prefer to encapsulate multiple generators into a single reusable configuration, you can extend `CompositeCustomizer`:

```java
public class DomainCustomizer extends CompositeCustomizer {

    public DomainCustomizer() {
        super(
            new ProductGenerator(),
            new ReviewGenerator()
        );
    }
}
```

```java
@Test
@AutoParams
@Customization(DomainCustomizer.class)
void testMethod(Product product, Review review) {
    assertTrue(product.getPriceAmount().compareTo(BigDecimal.valueOf(10)) >= 0);
    assertTrue(product.getPriceAmount().compareTo(BigDecimal.valueOf(10000)) <= 0);
    assertTrue(review.getRating() >= 1);
    assertTrue(review.getRating() <= 5);
}
```

This approach gives you a clean and reusable way to manage related custom generators, improving maintainability and consistency across your test suite.

#### Customization Scoping

The `@Customization` annotation can also be applied to individual parameters within a test method. When used in this way, the specified customization will apply to that parameter and all subsequent parameters of the same type—unless explicitly overridden.

This allows for fine-grained control over how test data is generated, making it easier to set up complex, context-specific scenarios.

Here's an example of a custom generator that creates a free product (with zero prices):

```java
public class FreeProductGenerator extends ObjectGeneratorBase<Product> {

    @Override
    protected Product generateObject(ObjectQuery query, ResolutionContext context) {
        UUID id = context.resolve();
        String name = context.resolve();

        BigDecimal priceAmount = BigDecimal.ZERO;

        return new Product(id, name, priceAmount);
    }
}
```

And here’s how to apply it to a specific parameter:

```java
@Test
@AutoParams
@Customization(DomainCustomizer.class)
void testMethod(
    Product product1,
    @Customization(FreeProductGenerator.class) Product product2
) {
    assertTrue(product1.getPriceAmount().compareTo(BigDecimal.valueOf(10)) >= 0);
    assertTrue(product1.getPriceAmount().compareTo(BigDecimal.valueOf(10000)) <= 0);

    assertEquals(BigDecimal.ZERO, product2.getPriceAmount());
}
```

In this test, `product1` is generated using the default logic from `DomainCustomizer`, while `product2` uses the `FreeProductGenerator` to produce a free product. This demonstrates how per-parameter customization gives you precise control over test data generation.

#### One-time Customizations with DSL(Domain-Specific Language)

AutoParams allows you to define one-time customizations directly within your test method using a domain-specific language(DSL). This is useful when you want to customize test data generation in a highly localized, context-specific way—without having to create separate generator classes.

```java
import static autoparams.customization.dsl.ArgumentCustomizationDsl.freezeArgument;

public class TestClass {

    @Test
    @AutoParams
    void testMethod(Product product, @Max(5) int rating, ResolutionContext context) {
        context.customize(
            freezeArgument("product").in(Review.class).to(product),
            freezeArgument("rating").to(rating)
        );
        Review review = context.resolve();
        assertSame(product, review.getProduct());
        assertEquals(rating, review.getRating());
    }
}
```

In this example, we use the `freezeArgument` static method from the `ArgumentCustomizationDsl` class to customize the behavior of the `ResolutionContext`. Specifically:

- The `product` property in any `Review` instance created by the context will be set to the `product` parameter of the test.
- Likewise, the `rating` property will be set to the `rating` parameter.

This approach is especially useful for quickly fixing values without defining a full custom generator or specifying customization at the test method level. It improves the readability and maintainability of localized scenarios by keeping custom logic close to the test logic.

> **Note**  
> The `freezeArgument("name")` method relies on the availability of parameter names at runtime. However, Java does not include parameter names in bytecode by default. To ensure this works correctly, you can:
>
> 1. Use a **record class**, which preserves parameter names by design.
> 2. **Compile with the `-parameters` option** using `javac`.  
>    If you're using **Spring Boot**, this option is automatically enabled when you use the [Spring Boot Gradle plugin](https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/htmlsingle/#reacting-to-other-plugins.java) or the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/htmlsingle/#using).
> 3. Apply the **`@ConstructorProperties`** annotation to explicitly declare parameter names.  
>    If you're using **Lombok**, and the constructor is generated by a Lombok annotation such as `@AllArgsConstructor`, this annotation can be automatically added by enabling the `lombok.anyConstructor.addConstructorProperties = true` option.  
>    For more details, see: [https://projectlombok.org/features/constructor](https://projectlombok.org/features/constructor)


#### Settable Properties

If a class follows the JavaBeans convention—meaning it has a no-arguments constructor and public setter methods—AutoParams can automatically populate its properties using the `InstancePropertyWriter` customizer.

Here's a simple example:

```java
@Getter
@Setter
public class User {

    private Long id;
    private String name;
}
```

By applying the `InstancePropertyWriter` customizer, AutoParams will generate and assign values to the `id` and `name` properties automatically:

```java
@Test
@AutoParams
@Customization(InstancePropertyWriter.class)
void testMethod(User user) {
    assertNotNull(user.getId());
    assertNotNull(user.getName());
}
```

In this test, the `User` object is created using its default constructor, and AutoParams sets values on its writable properties via their setters. This is especially useful when working with legacy models or data transfer objects(DTOs) that do not have constructors covering all fields.

### Parameterized Tests

AutoParams also supports **parameterized tests**, allowing you to execute the same test logic with multiple sets of input data. With AutoParams, you can seamlessly combine manually specified values with automatically generated test data—enabling both flexibility and convenience.

Here are some of the features you can use for parameterized tests.

#### `@ValueAutoSource` Annotation

The `@ValueAutoSource` annotation is a simple yet powerful tool for writing parameterized tests with AutoParams.

```java
@ParameterizedTest
@ValueAutoSource(strings = { "Camera", "Candle" })
void testMethod(String name, Factory<Product> factory) {
    Product product = factory.get(
        freezeArgument("name").to(name)
    );
    assertTrue(product.getName().startsWith("Ca"));
}
``` 

> **Note**  
> This feature depends on parameter name availability. See the note in the [One-time Customizations with DSL](#one-time-customizations-with-dsldomain-specific-language) section for details.

In this example, the test method is executed twice—once with `"Camera"` and once with `"Candle"` as the value of the `name` parameter. The `factory` parameter is resolved automatically by AutoParams and can be customized using the DSL, as shown with `freezeArgument`.

This enables the creation of test objects (`Product` in this case) that are partially controlled (e.g., a fixed name) and partially randomized (e.g., all other properties), striking a balance between specificity and variety.

The usage of `@ValueAutoSource` is similar to JUnit 5's `@ValueSource`, and it supports the following types of literal values:

- `short`
- `byte`
- `int`
- `long`
- `float`
- `double`
- `char`
- `boolean`
- `java.lang.String`
- `java.lang.Class`

#### `@CsvAutoSource` Annotation

The `@CsvAutoSource` annotation lets you define repeated test inputs in CSV format, similar to JUnit 5’s @CsvSource. Any parameters not explicitly provided in the CSV rows will be automatically generated by AutoParams.

```java
@ParameterizedTest
@CsvAutoSource({
    "Product 1, 500",
    "Product 2, 10000"
})
void testMethod(String name, BigDecimal priceAmount, UUID id) {
    Product product = new Product(id, name, priceAmount);
    assertTrue(product.getName().startsWith("Product"));
}
```

In this example, the `@CsvAutoSource` annotation provides values for the `name` and `priceAmount` parameters. The remaining parameter(`id`) is resolved automatically by AutoParams.

The test will run once for each line in the CSV input array—twice in this case—allowing you to repeat the same test logic with multiple fixed inputs while still benefiting from automatic value generation for the rest.

This approach makes it easy to test combinations of fixed and dynamic values in a concise and expressive way.

#### `@MethodAutoSource` Annotation

The `@MethodAutoSource` annotation combines the features of JUnit 5’s `@MethodSource` and AutoParams’s `@AutoSource`. You can specify a method that provides test data, and AutoParams will fill in any remaining parameters automatically.

```java
@ParameterizedTest
@MethodAutoSource("testDataSource")
void testMethod(String name, BigDecimal priceAmount, UUID id) {
    Product product = new Product(id, name, priceAmount);
    assertTrue(product.getName().startsWith("Product"));
}

static Stream<Arguments> testDataSource() {
    return Stream.of(
        arguments("Product 1", new BigDecimal(500)),
        arguments("Product 2", new BigDecimal(10000))
    );
}
```

In this example, the `testDataSource` method provides values for the `name` and `priceAmount` parameters. The remaining parameter(`id`) is automatically resolved by AutoParams and provided as an argument to the test method.

This setup allows you to blend manually specified values with automatically generated ones, giving you both precision and variability in your parameterized tests.

#### `@Repeat` Annotation

The `@Repeat` annotation allows you to run a test multiple times, generating fresh random values for unspecified parameters on each run.

```java
@ParameterizedTest
@ValueAutoSource(ints = { 1, 2, 3 })
@Repeat(5)
void testMethod(int a, int b) {
    Calculator sut = new Calculator();
    int actual = sut.add(a, b);
    assertEquals(a + b, actual);
}
```
In this example, the test is executed 15 times in total—five times for each of the values `1`, `2`, and `3` assigned to the parameter `a`. For each run, the value of `b` is automatically generated by AutoParams.

If you want AutoParams to generate values for **all** parameters and still repeat the test multiple times, you can combine `@AutoSource` with `@Repeat`.

```java
@ParameterizedTest
@AutoSource
@Repeat(10)
void testMethod(int a, int b) {
    Calculator sut = new Calculator();
    int actual = sut.add(a, b);
    assertEquals(a + b, actual);
}
```

This combination is useful when you want to explore a wider range of inputs and increase test coverage with minimal setup.

### Constructor Selection Policy

When AutoParams generates instances of complex types that have multiple constructors, it follows a specific policy to determine which constructor to use:

1. Constructors annotated with `@ConstructorProperties` are prioritized.
1. If no such annotation is present, AutoParams chooses the constructor with the **fewest** parameters.

Here's an example:

```java
@Getter
public class ComplexObject {

    private final int value1;
    private final String value2;
    private final UUID value3;

    @ConstructorProperties({ "value1", "value2", "value3" })
    public ComplexObject(int value1, String value2, UUID value3) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
    }

    @ConstructorProperties({ "value1", "value2" })
    public ComplexObject(int value1, String value2) {
        this(value1, value2, null);
    }

    public ComplexObject(int value1) {
        this(value1, null, null);
    }
}
```

```java
@Test
@AutoParams
void testMethod(ComplexObject object) {
    assertNotNull(object.getValue2());
    assertNull(object.getValue3());
}
```

In this example, AutoParams selects the constructor with the `@ConstructorProperties` annotation that has the fewest parameters—(`int`, `String`)—and assigns null to the `value3` field. This shows how constructor selection can affect the structure of the generated object.

## `autoparams-spring`

When testing a Spring application, you often need both of the following:

- Beans provided by the **Spring IoC container**
- Arbitrary test data automatically generated by **AutoParams**

The `autoparams-spring` extension bridges these two needs, allowing you to write test methods that receive **Spring-managed beans** and **AutoParams-generated arguments** side by side.

This means you can write tests that automatically inject service components from your application context and use auto-generated test data at the same time, with minimal setup.

### Install

#### Maven

For Maven, you can add the following dependency to your pom.xml:

```xml
<dependency>
  <groupId>io.github.autoparams</groupId>
  <artifactId>autoparams-spring</artifactId>
  <version>10.0.0</version>
</dependency>
```

#### Gradle

For Gradle, use:

```groovy
testImplementation 'io.github.autoparams:autoparams-spring:10.0.0'
```

### `@UseBeans` Annotation

Suppose your Spring application has a `HelloSupplier` bean that implements the `MessageSupplier` interface:

```java
public interface MessageSupplier {

    String getMessage(String name);
}
```

```java
@Component
public class HelloSupplier implements MessageSupplier {

    @Override
    public String getMessage(String name) {
        return "Hello, " + name + "!";
    }
}
```

If you want to test how your `MessageSupplier` bean behaves, you can use the `@UseBeans` annotation like this:

```java
@SpringBootTest
public class TestClass {

    @Test
    @AutoParams
    @UseBeans
    void testMethod(MessageSupplier service, String name) {
        String message = service.getMessage(name);
        assertTrue(message.startsWith("Hello"));
        assertTrue(message.contains(name));
    }
}
```

In this test:

- The `service` parameter is automatically resolved as a Spring bean.
- The `name` parameter is randomly generated by AutoParams.

This allows you to seamlessly combine real Spring components with generated test data, making your tests both concise and expressive.

## `autoparams-mockito`

`autoparams-mockito` is an extension of AutoParams that enables the creation of test doubles for interfaces and abstract classes using Mockito, a widely used mocking framework for Java. It allows AutoParams to generate test doubles without requiring manual setup.

`autoparams-mockito` is an extension of AutoParams that enables the automatic creation of test doubles for interfaces and abstract classes using Mockito, a widely used mocking framework for Java. With this extension, AutoParams can generate test doubles seamlessly—with minimal setup.

### Install

#### Maven

For Maven, you can add the following dependency to your pom.xml:

```xml
<dependency>
  <groupId>io.github.autoparams</groupId>
  <artifactId>autoparams-mockito</artifactId>
  <version>10.0.0</version>
</dependency>
```

#### Gradle

For Gradle, use:

```groovy
testImplementation 'io.github.autoparams:autoparams-mockito:10.0.0'
```

### Generating Test Doubles with Mockito

Suppose you have an interface that represents a dependency:

```java
public interface Dependency {

    String getName();
}
```

And a system under test that relies on this dependency:

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

By using the `@Customization(MockitoCustomizer.class`) annotation, AutoParams will automatically generate Mockito-based test doubles for eligible parameters (such as interfaces and abstract classes).

Here’s an example:

```java
@Test
@AutoParams
@Customization(MockitoCustomizer.class)
void testMethod(@Freeze Dependency stub, SystemUnderTest sut) {
    when(stub.getName()).thenReturn("World");
    assertEquals("Hello World", sut.getMessage());
}
```

In this test:

- `stub` is a test double automatically generated by Mockito.
- The `@Freeze` annotation ensures that the same `stub` instance is injected into the `SystemUnderTest`.
- You can configure the test double using standard Mockito syntax.

This integration streamlines test setup, enabling you to focus on verifying behavior rather than wiring dependencies manually.

## `autoparams-lombok`

`autoparams-lombok` is an extension for AutoParams that enhances compatibility with [Project Lombok](https://projectlombok.org/), a popular Java library for reducing boilerplate code.

### Install

#### Maven

For Maven, you can add the following dependency to your pom.xml:

```xml
<dependency>
  <groupId>io.github.autoparams</groupId>
  <artifactId>autoparams-lombok</artifactId>
  <version>10.0.0</version>
</dependency>
```

#### Gradle

For Gradle, use:

```groovy
testImplementation 'io.github.autoparams:autoparams-lombok:10.0.0'
```

### `BuilderCustomizer` Class

If you're using Lombok's `@Builder` annotation, the `BuilderCustomizer` allows AutoParams to generate arbitrary objects via the builder, making it easier to write tests without manually constructing instances.

Suppose you have an `Order` class like this:

```java
@Builder
@Getter
public class Order {

    private final UUID id;
    private final UUID productId;
    private final Integer quantity;
    private final UUID customerId;
    private final BigDecimal orderedPriceAmount;
    private final String comment;
}
```

To automatically generate `Order` instances, apply `BuilderCustomizer` in your test:

```java
@ParameterizedTest
@AutoSource
@Customization(BuilderCustomizer.class)
void testMethod(Order order) {
    assertThat(order.getId()).isNotNull();
    assertThat(order.getProductId()).isNotNull();
    assertThat(order.getQuantity()).isNotNull();
    assertThat(order.getQuantity()).isPositive();
    assertThat(order.getCustomerId()).isNotNull();
    assertThat(order.getOrderedPriceAmount()).isNotNull();
    assertThat(order.getComment()).isNotNull();
}
```

This eliminates the need to manually configure builder calls, helping you write cleaner and more maintainable tests.

#### Custom Method Names

If your class uses custom builder method names via Lombok's `builderMethodName` and `buildMethodName` attributes, you can still use `BuilderCustomizer` by extending it to specify those method names.

For example, consider the following `Shipment` class:

```java
@Builder(builderMethodName = "getBuilder", buildMethodName = "create")
@Getter
public class Shipment {

    private final UUID id;
    private final UUID orderId;
    private final String postalCode;
    private final String address;
    private final Boolean shipped;
}
```

To make this compatible with AutoParams, define a custom subclass:

```java
public class ShipmentBuilderCustomizer extends BuilderCustomizer {

    public ShipmentBuilderCustomizer() {
        super("getBuilder", "create");
    }
}
```

Then apply it in your test:

```java
@ParameterizedTest
@AutoSource
@Customization(ShipmentBuilderCustomizer.class)
void testMethod(Shipment shipment) {
    assertThat(shipment.getId()).isNotNull();
    assertThat(shipment.getOrderId()).isNotNull();
    assertThat(shipment.getPostalCode()).isNotNull();
    assertThat(shipment.getAddress()).isNotNull();
    assertThat(shipment.getShipped()).isNotNull();
}
```

This allows you to use custom builder patterns while still benefiting from automatic object generation.

## `autoparams-kotlin`

`autoparams-kotlin` is an extension of AutoParams that adds Kotlin-specific support for test data generation. It helps reduce boilerplate in Kotlin tests by generating values in a way that aligns with Kotlin’s language features.

### Install

#### Maven

For Maven, you can add the following dependency to your pom.xml:

```xml
<dependency>
  <groupId>io.github.autoparams</groupId>
  <artifactId>autoparams-kotlin</artifactId>
  <version>10.0.0</version>
</dependency>
```

#### Gradle (Groovy)

For Gradle-Groovy, use:

```groovy
testImplementation 'io.github.autoparams:autoparams-kotlin:10.0.0'
```

#### Gradle (Kotlin)

For Gradle-Kotlin, use:

```kotlin
testImplementation("io.github.autoparams:autoparams-kotlin:10.0.0")
```

### `@AutoKotlinParams` Annotation

Consider the following Kotlin data class:

```kotlin
data class Point(val x: Int = 0, val y: Int = 0)
```

Using default values in tests can result in limited test coverage. The `@AutoKotlinParams` annotation enables AutoParams to provide randomized arguments for Kotlin test methods, ensuring more varied and meaningful input.

Here's an example:

```kotlin
@Test
@AutoKotlinParams
fun testMethod(point: Point) {
    assertThat(point.x).isNotEqualTo(0)
    assertThat(point.y).isNotEqualTo(0)
}
```

In this test, the `point` parameter is automatically initialized with non-default, randomly generated values—allowing the test to cover a broader range of scenarios without requiring manual setup.
