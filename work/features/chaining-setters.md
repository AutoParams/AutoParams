# Add chaining style setter support to the InstancePropertyWriter class

Example snippet for chaining setters in Java:

```java
public class Product {

    private String name;
    private Double price;

    public String getName() {
        return name;
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public Product setPrice(Double price) {
        this.price = price;
        return this;
    }
}
```

```java
@Test
@AutoParams
@Customization(InstancePropertyWriter.class)
void testMethod(Product product) {
    assertNotNull(product);
    assertNotNull(product.getName());
    assertNotNull(product.getPrice());
}
```

## Test Scenarios:

- [x] sut sets property value using chaining setter

## Implementation History:

- Created Product class with chaining setters inside SpecsForInstancePropertyWriter test class
- Added test method `sut_sets_property_value_using_chaining_setter` to verify chaining setters functionality
- Enhanced InstancePropertyWriter to detect and handle chaining setters that return the object instance
- Added `isChainingSetter()` method to identify methods that follow chaining pattern (start with "set", take one parameter, return the class type)
- Added `isAlreadyDetectedBySetter()` method to avoid duplicating setters already detected by JavaBeans introspection
- Updated both Class<?> and ParameterizedType versions of setProperties method to handle chaining setters
- All tests pass successfully
