# Enhance Log Readability

## Problem Statement
By using the `@LogResolution` annotation, you can log the process of dependency injection and value generation during object creation. However, the current log messages are too long and complex, which reduces readability.

**Current Log Example**:
```text
> Resolving for: class your.app.User
|-- > Resolving for: interface autoparams.generator.ConstructorResolver
|   |-- > Resolving for: interface autoparams.generator.ConstructorExtractor
|   |   < Resolved(<1 ms): autoparams.generator.DefaultConstructorExtractor@4c418496 for: interface autoparams.generator.ConstructorExtractor
|   < Resolved(<1 ms): autoparams.generator.CompositeConstructorResolver@12d35bc9 for: interface autoparams.generator.ConstructorResolver
|
|-- > Resolving for: Parameter java.util.UUID id
|   < Resolved(<1 ms): 587c2513-7781-4249-8a72-d274f5ea1f9d for: Parameter java.util.UUID id
|
|-- > Resolving for: Parameter java.lang.String email
|   |-- > Resolving for: class autoparams.generator.EmailAddressGenerationOptions
|   |   < Resolved(<1 ms): EmailAddressGenerationOptions[domains=["test.com"]] for: class autoparams.generator.EmailAddressGenerationOptions
|   < Resolved(1 ms): 787ed241-e6e2-427a-9b53-bdf8f53b95ae@test.com for: Parameter java.lang.String email
|
|-- > Resolving for: Parameter java.lang.String username
|   < Resolved(<1 ms): usernamefc365ad6-cc7e-4f1a-8766-ecca1429698f for: Parameter java.lang.String username
< Resolved(1 ms): User[id=587c2513-7781-4249-8a72-d274f5ea1f9d, email=787ed241-e6e2-427a-9b53-bdf8f53b95ae@test.com, username=usernamefc365ad6-cc7e-4f1a-8766-ecca1429698f] for: class your.app.User
```

1. Repetitive use of 'Resolving for' and 'Resolved' text
2. Limited readability due to ASCII characters
3. Reduced readability caused by separating the start and end of object resolution logs
4. Inclusion of technical details such as `ConstructorResolver` and `ConstructorExtractor` that are not part of domain knowledge

## Proposed Solution

Replace the current verbose log format with a cleaner `query → value (elapsed)` format using tree structure.

**Proposed Format**:

```java
public record User(UUID id, String email) {
}

public record Address(String street, String city, String zipCode) {
}

public record Product(String name, BigDecimal price) {
}

public record Order(User customer, Address shippingAddress, List<Product> products) {
}
```

```text
Order (5ms)
 ├─ User customer (2ms)
 │   ├─ UUID id → 587c2513-7781-4249-8a72-d274f5ea1f9d (1ms)
 │   └─ String email → user@test.com (1ms)
 ├─ Address shippingAddress (2ms)
 │   ├─ String street → street123 (1ms)
 │   ├─ String city → city456 (< 1ms)
 │   └─ String zipCode → zipCode789 (1ms)
 └─ List<Product> products → ArrayList<Product> (3ms)
     ├─ Product (1ms)
     │   ├─ String name → nameabc123 (1ms)
     │   └─ BigDecimal price → 19.99 (1ms)
     ├─ Product (1ms)
     │   ├─ String name → nameabc456 (1ms)
     │   └─ BigDecimal price → 29.99 (1ms)
     └─ Product (1ms)
         ├─ String name → namexyz789 (1ms)
         └─ BigDecimal price → 39.99 (1ms)
```

## Implementation Steps

### 1. ObjectQuery.toLog(boolean verbose) Method

Add `toLog(boolean verbose)` default method to `ObjectQuery` interface to implement custom logging behavior for different query types. This default method should return a concise string representation of the query, depending on the `verbose` flag.

**Expected Result Example**:

| Verbose | Result        |
|---------|---------------|
| true    | your.app.User |
| false   | User          |

**Test Class:** `test.autoparams.SpecsForObjectQuery`

**Test Scenarios**:
- [x] toLog returns class name with package when verbose is true
- [x] toLog returns simple class name when verbose is false

**Notice**:
- Do not remove `@FunctionalInterface` annotation from `ObjectQuery` interface.
- Do not edit `DefaultObjectQuery` class.
- Add a final newline to the new test class file.

### 2. ParameterQuery.toLog Implementation

**Expected Result Example**:

#### ParameterQuery.toLog Implementation

| Verbose | Result                 |
|---------|------------------------|
| true    | java.lang.String email |
| false   | String email           |

**Test Class:** `test.autoparams.SpecsForParameterQueryUsingName` in the `test-autoparams-java-17` project

**Test Scenarios**:
- [x] toLog returns parameter type with package and parameter name when verbose is true
- [x] toLog returns simple type name and parameter name when verbose is false

### 3. Log Visibility Control

Add `@LogVisibility` annotation to control which queries appear in different log modes. Technical implementation details like `ConstructorResolver` and `ConstructorExtractor` should only appear in verbose logs to improve readability.

**Project**: autoparams

```java
package autoparams;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LogVisibility {
    boolean verboseOnly() default false;
}
```

**Usage Examples**:

```java
@LogVisibility(verboseOnly = true)
public class ConstructorResolverQuery implements ObjectQuery {
}
```

**Test Class:** `test.autoparams.SpecsForResolutionLogging` in the `test-autoparams-java-17` project

**Test Scenarios**:
- [x] sut does not print log for EmailAddressGenerationOptions
- [x] sut does not print log for SupportedParameterPredicate
- [x] sut does not print log for ResolutionContext
- [x] sut does not print log for URIGenerationOptions
  Use `record Link(String uri, String text)`

### 4. Query → Value (Elapsed) Format

Implement the new log format. The logger should print logs in the format `query → value (elapsed)`.

```java
@Test
@AutoParams
@LogResolution
void testMethod(ResolutionContext context) {
    context.resolve(String.class);
    // Expected log output:
    // String → 587c2513-7781-4249-8a72-d274f5ea1f9d (1ms)
    // or
    // String → 587c2513-7781-4249-8a72-d274f5ea1f9d (< 1ms)
}
```

**Test Class:** `test.autoparams.SpecsForResolutionLogging` in the `test-autoparams-java-17` project

**Test Scenarios**:
- [x] sut prints single line with simple object query
- [x] sut prints arrow after query with simple object query
- [x] sut prints resolved value after arrow with simple object query
- [x] sut prints elapsed time in ms after resolved value with simple object query

### 5. One-Depth Tree Structure

Implement a one-depth tree structure for the log output. The logger should print the query and its resolved value in a tree-like format, with each level indented.

```java
@Test
@AutoParams
@LogResolution
void testMethod(ResolutionContext context) {
    context.resolve(Address.class);
    // Expected log output:
    // Address → Address[street=street123, city=city456, zipCode=zipCode789] (2ms)
    //  ├─ String street → street123 (1ms)
    //  ├─ String city → city456 (< 1ms)
    //  └─ String zipCode → zipCode789 (1ms)
}
```

**Test Class:** `test.autoparams.SpecsForResolutionLogging` in the `test-autoparams-java-17` project

**Test Scenarios**:
- [x] sut does not print any log for ConstructorResolver
- [x] sut does not print any log for ConstructorExtractor
- [x] sut prints one depth tree structure with first child
  Example: ` ├─ `
- [x] sut prints one depth tree structure with second child
  Example: ` ├─ `
- [x] sut prints one depth tree structure with last child
  Example: ` └─ `
- [x] sut prints one depth entry correctly
  Example: ` ├─ String street → street123 (1ms)`
- [x] ~~sut prints value of root correctly~~ (Removed: Conflicts with Branch Node Value Control - branch nodes should not print values by default)
  ~~Example: `Address → Address[street=street123, city=city456, zipCode=zipCode789] (2ms)`~~
- [x] sut prints value of last child correctly
  Example: ` └─ String zipCode → zipCodexyz789 (1ms)`

### 6. Two-Depth Tree Structure

Implement a two-depth tree structure for the log output. The logger should print the query and its resolved value in a tree-like format, with each level indented.

```java
@Test
@AutoParams
@LogResolution
void testMethod(ResolutionContext context) {
    context.resolve(new TypeReference<List<Address>>() { });
    // Expected log output:
    // List<Address> → [Address[...], Address[...]] (3ms)
    //  ├─ Address → Address[street=streetabc123, city=cityabc123, zipCode=zipCodeabc123] (1ms)
    //  │   ├─ String street → streetabc123 (1ms)
    //  │   ├─ String city → cityabc123 (1ms)
    //  │   └─ String zipCode → zipCodeabc123 (1ms)
    //  ├─ Address → Address[street=streetxyz456, city=cityxyz456, zipCode=zipCodexyz456] (1ms)
    //  │   ├─ String street → streetxyz456 (1ms)
    //  │   ├─ String city → cityxyz456 (1ms)
    //  │   └─ String zipCode → zipCodexyz456 (1ms)
    //  └─ Address → Address[street=streetxyz789, city=cityxyz789, zipCode=zipCodexyz789] (1ms)
    //      ├─ String street → streetxyz789 (1ms)
    //      ├─ String city → cityxyz789 (1ms)
    //      └─ String zipCode → zipCodexyz789 (1ms)
}
```

**Test Class:** `test.autoparams.SpecsForResolutionLogging` in the `test-autoparams-java-17` project

**Test Scenarios**:
- [x] sut prints two depth tree structure with first leaf
  Example: ` │   ├─ String street → streetabc123 (1ms)`
- [x] sut prints two depth tree structure with second leaf
  Example: ` │   ├─ String city → cityabc123 (1ms)`
- [x] sut prints two depth tree structure with last leaf
  Example: ` │   └─ String zipCode → zipCodeabc123 (1ms)`
- [x] sut prints two depth tree structure with first leaf of last stem
  Example: `     ├─ String street → streetxyz789 (1ms)`
- [x] sut prints two depth tree structure with second leaf of last stem
  Example: `     ├─ String city → cityxyz789 (1ms)`
- [x] sut prints two depth tree structure with last leaf of last stem
  Example: `     └─ String zipCode → zipCodexyz789 (1ms)`

### 7. N-Depth Tree Structure

Implement an N-depth tree structure for the log output. The logger should print the query and its resolved value in a tree-like format, with each level indented.

```java
@Test
@AutoParams
@LogResolution
void testMethod(ResolutionContext context) {
    context.resolve(Order.class);
    // Expected log output:
    //  0: Order → Order[customer=User[...], shippingAddress=Address[...], products=[Product[...], Product[...]]] (5ms)
    //  1:  ├─ User customer → User[id=587c2513..., email=user@test.com] (2ms)
    //  2:  │   ├─ UUID id → 587c2513-7781-4249-8a72-d274f5ea1f9d (1ms)
    //  3:  │   └─ String email → user@test.com (1ms)
    //  4:  ├─ Address shippingAddress → Address[street=street123, city=city456, zipCode=12345] (2ms)
    //  5:  │   ├─ String street → street123 (1ms)
    //  6:  │   ├─ String city → city456 (< 1ms)
    //  7:  │   └─ String zipCode → zipCode789 (1ms)
    //  8:  └─ List<Product> products → [Product[...], Product[...]] (3ms)
    //  9:      ├─ Product → Product[name=nameabc123, price=19.99] (1ms)
    // 10:      │   ├─ String name → nameabc123 (1ms)
    // 11:      │   └─ BigDecimal price → 19.99 (1ms)
    // 12:      ├─ Product → Product[name=nameabc456, price=29.99] (1ms)
    // 13:      │   ├─ String name → nameabc456 (1ms)
    // 14:      │   └─ BigDecimal price → 29.99 (1ms)
    // 15:      └─ Product → Product[name=namexyz789, price=39.99] (1ms)
    // 16:          ├─ String name → namexyz789 (1ms)
    // 17:          └─ BigDecimal price → 39.99 (1ms)
}
```

**Test Class:** `test.autoparams.SpecsForResolutionLogging` in the `test-autoparams-java-17` project

**Test Scenarios**:
- [x] sut prints deep tree structure with first leaf
  Example: `     │   ├─ String name → nameabc123 (1ms)`
- [x] sut prints deep tree structure with last leaf
  Example: `     │   └─ BigDecimal price → 19.99 (1ms)`
- [x] sut prints deep tree structure with first leaf of last stem
  Example: `         ├─ String name → namexyz789 (1ms)`
- [x] sut prints deep tree structure with last leaf of last stem
  Example: `         └─ BigDecimal price → 39.99 (1ms)`

### 8. Internal Type Formatter (Refactoring)

Implement an internal type formatter to format `Type` for logging. `ObjectQuery.toLog(boolean verbose)` and `ParameterQuery.toLog(boolean verbose)` methods should use this formatter to produce concise type names.

- [x] Add package-private `TypeFormatter` class in the `autoparams` package.
- [x] Implement `format(Type type, boolean verbose)` method to format types based on the `verbose` flag.
- [x] Use this formatter in `ObjectQuery.toLog(boolean verbose)` method.
- [x] Use this formatter in `ParameterQuery.toLog(boolean verbose)` method.

### 9. Generic Type Formatting

Implement generic type formatting in the log output. The logger should print generic types in a concise format in non-verbose mode, e.g., `List<String>` instead of `java.util.List<java.lang.String>`.

```java
public record Gen1<T>(T value) {
}
```

```java
public record Gen2<T, U>(T value1, U value2) {
}
```

```java
public record Gen3<T, U, V>(T value1, U value2, V value3) {
}
```

**Test Class:** `test.autoparams.SpecsForResolutionLogging` in the `test-autoparams-java-17` project

**Test Methods:**
```java
@ParameterizedTest
@MethodAutoSource("genericTestCases")
@LogResolution
void sut_formats_generic_types_correctly(
    TypeReference<?> typeReference,
    String expected,
    ResolutionContext context
) {
    String[] output = captureOutput(() -> context.resolve(typeReference));
    assertThat(output[0]).startsWith(expected);
}

static Stream<Arguments> genericTestCases() {
    return Stream.of(
        arguments(
            new TypeReference<Gen1<Integer>>() { },
            "Gen1<Integer>"
        ),
        ...
    );
}
```

**Test Cases:**
- [x] `Gen1<Integer>`
- [x] `Gen1<Gen1<Integer>>`
- [x] `Gen2<Integer, Long>`
- [x] `Gen2<Gen1<Integer>, Gen1<Long>>`
- [x] `Gen3<Integer, Long, Double>`
- [x] `Gen3<Gen1<Integer>, Gen1<Long>, Gen1<Double>>`
- [x] `Gen1<Gen2<Integer, Gen3<Long, Double, String>>>`

### 10. Branch Node Value Control

Implement control over branch node values in the log output. The logger should not print values for branch nodes by default for concise logs, but should allow specific conditions to output values when necessary.

**Conditions for branch nodes to output values:**
- If the type of the query and the type of the generated value are different, output the type of the value. For example, if the query is `List<String>` and the generated value is of type `ArrayList<String>`, output the value's type.

**Test Class:** `test.autoparams.SpecsForResolutionLogging` in the `test-autoparams-java-17` project

**Test Scenarios**:
- [x] sut does not print values for branch nodes by default
- [x] sut prints branch value if query type and value type differ

### 11. FieldQuery.toLog Implementation

Implement `toLog(boolean verbose)` method for `FieldQuery` interface to support field-based logging in resolution logs.

**Expected Result Example**:

| Verbose | Result                     |
|---------|----------------------------|
| true    | java.lang.String fieldName |
| false   | String fieldName           |

**Test Class:** `test.autoparams.SpecsForFieldQuery`

**Test Scenarios**:
- [x] toLog returns field type with package and field name when verbose is true
- [x] toLog returns simple type name and field name when verbose is false

## Backlogs

- [x] Rename the `@LogVisible` annotation to `@LogVisibility` to better reflect its purpose.
- [x] Format generic types in the log output to be more concise, e.g., `List<String>` instead of `List<java.lang.String>`.
- [x] Update the JavaDoc log output examples to reflect the new log format.
- [ ] Handle cases where the generated value is `null`.
- [ ] Improve the performance of the implementation.
