# Factory

## Overview

`Factory<T>` is a class for creating instances of a given type. It supports both simple and generic type creation.

`Factory<T>` is particularly useful when you need to:
- Create instances of specific types
- Apply customizations to generated objects
- Create streams of test data

## Basic Usage

**Basic Factory Creation**

Create a factory by specifying the target class:

```java
Factory<Product> factory = Factory.create(Product.class);
Product product = factory.get();
```

**Type Inference Creation**

When the variable type is explicitly declared, you can use type inference:

```java
Factory<Product> factory = Factory.create();
Product product = factory.get();
```

**Generic Factory Creation**

For creating collections or complex generic types, use `TypeReference`:

```java
Factory<List<Product>> factory = Factory.create(new TypeReference<List<Product>>() { });
List<Product> products = factory.get();
```

**Diamond Operator with TypeReference**

When the variable type is explicitly declared, you can use the diamond operator for cleaner syntax:

```java
Factory<List<Product>> factory = Factory.create(new TypeReference<>() { });
List<Product> products = factory.get();
```

### Generating Objects

`Factory<T>` provides multiple methods for generating objects based on your needs:

**Single Object Generation**

Use `get()` to generate a single object:

```java
Factory<Product> factory = Factory.create();
Product product = factory.get();
```

**Multiple Object Generation**

Use `getRange(int size)` to generate a specific number of objects:

```java
Factory<Product> factory = Factory.create();
List<Product> products = factory.getRange(5); // Creates exactly 5 products
```

**Stream Generation**

Use `stream()` for infinite streams:

```java
Factory<Product> factory = Factory.create();

// Generate 10 products using stream
List<Product> products = factory.stream()
    .limit(10)
    .collect(Collectors.toList());
```

### Automatic Injection in Tests

AutoParams can automatically inject `Factory<T>` instances into your test methods:

**Basic Factory Injection**

AutoParams automatically injects `Factory<T>` instances into test method parameters:

```java
@Test
@AutoParams
void testWithFactory(Factory<Product> factory) {
    Product product = factory.get();
    assertThat(product).isNotNull();
}
```

**Generic Factory Injection**

Generic factories are also automatically injected:

```java
@Test
@AutoParams
void testWithGenericFactory(Factory<List<Product>> listFactory) {
    List<Product> products = listFactory.get();
    assertThat(products).hasSize(3);
}
```

**Multiple Factory Injection**

You can inject multiple factories in the same test method:

```java
@Test
@AutoParams
void testWithMultipleFactories(
    Factory<Product> productFactory,
    Factory<List<Product>> listFactory
) {
    Product singleProduct = productFactory.get();
    List<Product> productList = listFactory.get();

    assertThat(singleProduct).isNotNull();
    assertThat(productList).hasSize(3);
}
```

## Customizations with DSL

`Factory<T>` integrates with AutoParams' customization DSL to let you set specific property values:

```java
import static autoparams.customization.dsl.ArgumentCustomizationDsl.set;

// Single property customization
Factory<Product> factory = Factory.create();
Product product = factory.get(
    set(Product::getName).to("Custom Product Name")
);

// Multiple property customization
Product premiumProduct = factory.get(
    set(Product::getName).to("Premium Product"),
    set(Product::getPriceAmount).to(BigDecimal.valueOf(999.99)),
    set(Product::getStockQuantity).to(100)
);
```

**Customization with Multiple Objects**

Apply customizations when generating multiple objects:

```java
Factory<Product> factory = Factory.create();
List<Product> products = factory.getRange(
    3,
    set(Product::getName).to("Bulk Product"),
    set(Product::getStockQuantity).to(50)
);
```

## Next Steps
