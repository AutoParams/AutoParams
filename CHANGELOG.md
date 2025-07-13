# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [11.3.0] - 2025-07-13

### Added
- Added `Design` class for fluent object configuration with explicit property values. While `Factory` generates objects with random values, `Design` allows precise control over object properties for predictable test data:
  ```java
  // Basic property configuration with fixed and dynamic values
  Product product = Design.of(Product.class)
      .set(Product::getName, "Custom Product Name")
      .supply(Product::getPrice, () -> Math.random() * 100)
      .instantiate();
  ```
  The `.set()` method configures properties with constant values, while `.supply()` uses suppliers for dynamic value generation on each instantiation.

  ```java
  // Nested object configuration for complex hierarchies
  Review review = Design.of(Review.class)
      .set(Review::getRating, 5)
      .design(Review::getProduct, product -> product
          .set(Product::getName, "Amazing Widget")
          .supply(Product::getPrice, () -> random.nextInt(10, 100)))
      .instantiate();
  ```
  The `.design()` method enables fluent configuration of nested object properties while maintaining type safety.

  ```java
  // Multiple instance creation with identical configurations
  List<Product> products = design.instantiate(3);
  ```
  Create multiple objects with the same configuration for bulk test data generation.

  ```java
  // ResolutionContext integration for context-based customizations
  Review review = Design.of(Review.class)
      .set(Review::getRating, 5)
      .instantiate(context);
  ```
  Combine explicit `Design` configurations with `ResolutionContext` customization patterns.

  ```java
  // Reusable customizers extending CompositeCustomizer
  public class ProductCustomizer extends CompositeCustomizer {
      public ProductCustomizer() {
          super(
              Design.of(Product.class)
                  .set(Product::getName, "Premium Product")
                  .set(Product::getPrice, 199.99)
          );
      }
  }

  @Test
  @AutoParams
  @Customization(ProductCustomizer.class)
  void testWithCustomizedProduct(Review review) {
      assertEquals("Premium Product", review.getProduct().getName());
      assertEquals(199.99, review.getProduct().getPrice());
  }
  ```
  Create standardized object configurations that can be applied across multiple tests using the `@Customization` annotation.

## [11.2.3] - 2025-07-09

### Added
- Enhanced `BuilderCustomizer` to support Lombok's `@Singular` annotation for collection fields

## [11.2.2] - 2025-07-09

### Fixed
- Enhanced wildcard type support for parameterized type generators:
  - `List<? extends T>` - Lists with bounded wildcards
  - `Set<? extends T>` - Sets with bounded wildcards
  - `Map<? extends K, ? extends V>` - Maps with bounded wildcards for both keys and values
  - `Optional<? extends T>` - Optional values with bounded wildcards

## [11.2.1] - 2025-07-09

### Fixed
- Improved support for wildcard generic types in parameter generation

## [11.2.0] - 2025-06-29

### Added
- Added `enableLogging()` method to `ResolutionContext` to control resolution logging
- Added `toLog(boolean)` method to `ObjectQuery` interface for customizable logging format
- Added `LogVisibility` annotation to control which types appear in resolution logs

### Changed
- Enhanced resolution logging with cleaner hierarchical format and improved readability:
  ```
  // Before (11.1.x):
  > Resolving: for Parameter your.app.User user
  |-- > Resolving: for Parameter final java.util.UUID id
  |   < Resolved(<1 ms): fbdf7aa8-1af7-4308... for Parameter final java.util.UUID id
  |-- > Resolving: for Parameter final java.lang.String email
  |   < Resolved(2 ms): 53bf56a3-8a42-47f3...@test.com for Parameter final java.lang.String email
  < Resolved(5 ms): User(id=fbdf7aa8..., email=53bf56a3...@test.com) for Parameter your.app.User user
  
  // After (11.2.0):
  User user (5ms)
   ├─ UUID id → fbdf7aa8-1af7-4308... (< 1ms)
   └─ String email → 53bf56a3-8a42-47f3...@test.com (2ms)
  ```
- Modified `ResolutionContext` constructor to disable logging by default - call `enableLogging()` to enable

## Previous Releases

For changes in previous versions, please refer to the [GitHub Releases](https://github.com/AutoParams/AutoParams/releases) page.
