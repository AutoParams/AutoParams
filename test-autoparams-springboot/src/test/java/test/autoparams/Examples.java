package test.autoparams;

import java.beans.ConstructorProperties;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import autoparams.AutoParams;
import autoparams.AutoSource;
import autoparams.BrakeBeforeAnnotation;
import autoparams.CsvAutoSource;
import autoparams.MethodAutoSource;
import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.Repeat;
import autoparams.ResolutionContext;
import autoparams.ValueAutoSource;
import autoparams.customization.CompositeCustomizer;
import autoparams.customization.Customization;
import autoparams.customization.Freeze;
import autoparams.customization.FreezeBy;
import autoparams.generator.Factory;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import autoparams.generator.ObjectGeneratorBase;
import autoparams.lombok.BuilderCustomizer;
import autoparams.mockito.MockitoCustomizer;
import autoparams.processor.InstancePropertyWriter;
import autoparams.spring.UseBeans;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static autoparams.customization.Matching.EXACT_TYPE;
import static autoparams.customization.Matching.IMPLEMENTED_INTERFACES;
import static autoparams.customization.Matching.PARAMETER_NAME;
import static autoparams.customization.dsl.ArgumentCustomizationDsl.freezeArgument;
import static autoparams.customization.dsl.ArgumentCustomizationDsl.set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.when;

@SpringBootTest
@SuppressWarnings({ "ClassCanBeRecord", "ConstantValue" })
public class Examples {

    public static class Calculator {

        public int add(int a, int b) {
            return a + b;
        }
    }

    @Test
    @AutoParams
    void testMethod(int a, int b) {
        Calculator sut = new Calculator();
        int actual = sut.add(a, b);
        assertEquals(a + b, actual);
    }

    @AllArgsConstructor
    @Getter
    public static class Product {

        private final UUID id;
        private final String name;
        private final BigDecimal priceAmount;
    }

    @AllArgsConstructor
    @Getter
    public static class Review {

        private final UUID id;
        private final UUID reviewerId;
        private final Product product;
        private final int rating;
        private final String comment;
    }

    @Test
    @AutoParams
    void testMethodSetup(@Freeze Product product, Review[] reviews) {
        for (Review review : reviews) {
            assertSame(product, review.getProduct());
        }
    }

    @AllArgsConstructor
    @Getter
    public static class StringContainer {

        private final String value;
    }

    @Test
    @AutoParams
    void testMethodFreezeByExactType(
        @FreezeBy(EXACT_TYPE) String s1,
        String s2,
        StringContainer container
    ) {
        assertSame(s1, s2);
        assertSame(s1, container.getValue());
    }

    @Test
    @AutoParams
    void testMethodFreezeByImplementedInterfaces(
        @FreezeBy(IMPLEMENTED_INTERFACES) String s1,
        CharSequence chars,
        StringContainer container
    ) {
        assertSame(s1, chars);
        assertNotSame(s1, container.getValue());
    }

    @Test
    @AutoParams
    void testMethodFreezeByParameterName(
        @FreezeBy(PARAMETER_NAME) UUID reviewerId,
        Review review
    ) {
        assertNotSame(reviewerId, review.getId());
        assertSame(reviewerId, review.getReviewerId());
    }

    @Test
    @AutoParams
    void testMethodFreezeByExactTypeOrImplementedInterfaces(
        @FreezeBy({ EXACT_TYPE, IMPLEMENTED_INTERFACES }) String s1,
        String s2,
        CharSequence chars
    ) {
        assertSame(s1, s2);
        assertSame(s1, chars);
    }

    @Test
    @AutoParams
    void testMethodFreeze(
        @Freeze String s1,
        String s2,
        StringContainer container
    ) {
        assertSame(s1, s2);
        assertSame(s1, container.getValue());
    }

    @Test
    @AutoParams
    void testMethodMinMax(@Min(1) @Max(10) int value) {
        assertTrue(value >= 1);
        assertTrue(value <= 10);
    }

    @Test
    void testMethodResolutionContext() {
        ResolutionContext context = new ResolutionContext();
        Product product = context.resolve();
        Review review = context.resolve();
    }

    @Test
    void testMethodFactory() {
        Factory<Product> factory = Factory.create(Product.class);
        Product product = factory.get();
        List<Product> products = factory.getRange(10);
    }

    public static class ProductGenerator extends ObjectGeneratorBase<Product> {

        @Override
        protected Product generateObject(ObjectQuery query, ResolutionContext context) {
            UUID id = context.resolve();
            String name = context.resolve();

            ThreadLocalRandom random = ThreadLocalRandom.current();
            BigDecimal priceAmount = new BigDecimal(random.nextInt(10, 10000 + 1));

            return new Product(id, name, priceAmount);
        }
    }

    @Test
    @AutoParams
    @Customization(ProductGenerator.class)
    void testMethodCustomization(Product product) {
        assertTrue(product.getPriceAmount().compareTo(BigDecimal.valueOf(10)) >= 0);
        assertTrue(product.getPriceAmount().compareTo(BigDecimal.valueOf(10000)) <= 0);
    }

    public static class ReviewGenerator extends ObjectGeneratorBase<Review> {

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

    @Test
    @AutoParams
    @Customization({ ProductGenerator.class, ReviewGenerator.class })
    void testMethodCustomizationWithArray(Product product, Review review) {
        assertTrue(product.getPriceAmount().compareTo(BigDecimal.valueOf(10)) >= 0);
        assertTrue(product.getPriceAmount().compareTo(BigDecimal.valueOf(10000)) <= 0);
        assertTrue(review.getRating() >= 1);
        assertTrue(review.getRating() <= 5);
    }

    public static class DomainCustomizer extends CompositeCustomizer {

        public DomainCustomizer() {
            super(
                new ProductGenerator(),
                new ReviewGenerator()
            );
        }
    }

    @Test
    @AutoParams
    @Customization(DomainCustomizer.class)
    void testMethodDomainCustomizer(Product product, Review review) {
        assertTrue(product.getPriceAmount().compareTo(BigDecimal.valueOf(10)) >= 0);
        assertTrue(product.getPriceAmount().compareTo(BigDecimal.valueOf(10000)) <= 0);
        assertTrue(review.getRating() >= 1);
        assertTrue(review.getRating() <= 5);
    }

    public static class FreeProductGenerator extends ObjectGeneratorBase<Product> {

        @Override
        protected Product generateObject(ObjectQuery query, ResolutionContext context) {
            UUID id = context.resolve();
            String name = context.resolve();

            BigDecimal priceAmount = BigDecimal.ZERO;

            return new Product(id, name, priceAmount);
        }
    }

    @Test
    @AutoParams
    @Customization(DomainCustomizer.class)
    void testMethodCustomizationOnParameter(
        Product product1,
        @Customization(FreeProductGenerator.class) Product product2
    ) {
        assertTrue(product1.getPriceAmount().compareTo(BigDecimal.valueOf(10)) >= 0);
        assertTrue(product1.getPriceAmount().compareTo(BigDecimal.valueOf(10000)) <= 0);

        assertEquals(BigDecimal.ZERO, product2.getPriceAmount());
    }

    public record ProductArgumentFreezer(Product product) implements ObjectGenerator {

        @Override
        public ObjectContainer generate(ObjectQuery query, ResolutionContext context) {
            if (query instanceof ParameterQuery parameterQuery) {
                Parameter parameter = parameterQuery.getParameter();
                if (parameter.isNamePresent() && parameter.getName().equals("product")) {
                    return new ObjectContainer(product);
                }
            }

            return ObjectContainer.EMPTY;
        }
    }

    public record RatingArgumentFreezer(int rating) implements ObjectGenerator {

        @Override
        public ObjectContainer generate(ObjectQuery query, ResolutionContext context) {
            if (query instanceof ParameterQuery parameterQuery) {
                Parameter parameter = parameterQuery.getParameter();
                if (parameter.isNamePresent() && parameter.getName().equals("rating")) {
                    return new ObjectContainer(rating);
                }
            }

            return ObjectContainer.EMPTY;
        }
    }

    @Test
    @AutoParams
    void testMethodFreezers(Product product, @Max(5) int rating, ResolutionContext context) {
        context.applyCustomizer(new ProductArgumentFreezer(product));
        context.applyCustomizer(new RatingArgumentFreezer(rating));
        Review review = context.resolve();
        assertSame(product, review.getProduct());
        assertEquals(rating, review.getRating());
    }

    @Test
    @AutoParams
    void testMethodDsl(Product product, @Max(5) int rating, ResolutionContext context) {
        context.customize(
            freezeArgument("product").in(Review.class).to(product),
            freezeArgument("rating").to(rating)
        );
        Review review = context.resolve();
        assertSame(product, review.getProduct());
        assertEquals(rating, review.getRating());
    }

    @Getter
    @Setter
    public static class User {

        private Long id;
        private String name;
    }

    @Test
    @AutoParams
    @Customization(InstancePropertyWriter.class)
    void testMethodInstancePropertyWriter(User user) {
        assertNotNull(user.getId());
        assertNotNull(user.getName());
    }

    @ParameterizedTest
    @ValueAutoSource(strings = { "Camera", "Candle" })
    void testMethodValueAutoSource(String name, Factory<Product> factory) {
        Product product = factory.get(
            freezeArgument("name").to(name)
        );
        assertTrue(product.getName().startsWith("Ca"));
    }

    @ParameterizedTest
    @CsvAutoSource({
        "Product 1, 500",
        "Product 2, 10000"
    })
    void testMethodCsvAutoSource(String name, BigDecimal priceAmount, UUID id) {
        Product product = new Product(id, name, priceAmount);
        assertTrue(product.getName().startsWith("Product"));
    }

    @ParameterizedTest
    @MethodAutoSource("testDataSource")
    void testMethodMethodAutoSource(String name, BigDecimal priceAmount, UUID id) {
        Product product = new Product(id, name, priceAmount);
        assertTrue(product.getName().startsWith("Product"));
    }

    static Stream<Arguments> testDataSource() {
        return Stream.of(
            arguments("Product 1", new BigDecimal(500)),
            arguments("Product 2", new BigDecimal(10000))
        );
    }

    @ParameterizedTest
    @ValueAutoSource(ints = { 1, 2, 3 })
    @Repeat(5)
    void testMethodValueAutoSourceRepeat(int a, int b) {
        Calculator sut = new Calculator();
        int actual = sut.add(a, b);
        assertEquals(a + b, actual);
    }

    @ParameterizedTest
    @AutoSource
    @Repeat(10)
    void testMethodAutoSourceRepeat(int a, int b) {
        Calculator sut = new Calculator();
        int actual = sut.add(a, b);
        assertEquals(a + b, actual);
    }

    @Getter
    public static class ComplexObject {

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

    @Test
    @AutoParams
    void testMethodConstructorSelection(ComplexObject object) {
        assertNotNull(object.getValue2());
        assertNull(object.getValue3());
    }

    public interface Dependency {

        String getName();
    }

    public static class SystemUnderTest {

        private final Dependency dependency;

        public SystemUnderTest(Dependency dependency) {
            this.dependency = dependency;
        }

        public String getMessage() {
            return "Hello " + dependency.getName();
        }
    }

    @Test
    @AutoParams
    @Customization(MockitoCustomizer.class)
    void testMethodMockito(@Freeze Dependency stub, SystemUnderTest sut) {
        when(stub.getName()).thenReturn("World");
        assertEquals("Hello World", sut.getMessage());
    }

    @ParameterizedTest
    @AutoSource
    @BrakeBeforeAnnotation(Autowired.class)
    void testMethodBrakeBeforeAnnotation(String name, @Autowired MessageSupplier service) {
        String message = service.getMessage(name);
        assertTrue(message.startsWith("Hello"));
        assertTrue(message.contains(name));
    }

    @Test
    @AutoParams
    @UseBeans
    void testMethodUseBeans(MessageSupplier service, String name) {
        String message = service.getMessage(name);
        assertTrue(message.startsWith("Hello"));
        assertTrue(message.contains(name));
    }

    @Builder
    @Getter
    public static class Order {

        private final UUID id;
        private final UUID productId;
        private final Integer quantity;
        private final UUID customerId;
        private final BigDecimal orderedPriceAmount;
        private final String comment;
    }

    @Test
    @AutoParams
    @Customization(BuilderCustomizer.class)
    void testMethodBuilder(Order order) {
        assertThat(order.getId()).isNotNull();
        assertThat(order.getProductId()).isNotNull();
        assertThat(order.getQuantity()).isNotNull();
        assertThat(order.getQuantity()).isPositive();
        assertThat(order.getCustomerId()).isNotNull();
        assertThat(order.getOrderedPriceAmount()).isNotNull();
        assertThat(order.getComment()).isNotNull();
    }

    @Builder(builderMethodName = "getBuilder", buildMethodName = "create")
    @Getter
    public static class Shipment {

        private final UUID id;
        private final UUID orderId;
        private final String postalCode;
        private final String address;
        private final Boolean shipped;
    }

    public static class ShipmentBuilderCustomizer extends BuilderCustomizer {

        public ShipmentBuilderCustomizer() {
            super("getBuilder", "create");
        }
    }

    @Test
    @AutoParams
    @Customization(ShipmentBuilderCustomizer.class)
    void testMethodConfiguredBuilder(Shipment shipment) {
        assertThat(shipment.getId()).isNotNull();
        assertThat(shipment.getOrderId()).isNotNull();
        assertThat(shipment.getPostalCode()).isNotNull();
        assertThat(shipment.getAddress()).isNotNull();
        assertThat(shipment.getShipped()).isNotNull();
    }

    @Test
    @AutoParams
    void testMethodCollectionSize(@Size(min = 5) ArrayList<String> arrayList) {
        assertThat(arrayList).hasSize(5);
    }

    @Test
    @AutoParams
    void testMethodArraySize(@Size(min = 5) String[] array) {
        assertThat(array).hasSize(5);
    }

    @AllArgsConstructor
    @Getter
    public static class Book {

        private final String title;
        private final List<String> authors;
        private final List<String> quotes;
    }

    @Test
    @AutoParams
    void testMethodCollectionSizeFreezeBy(
        @Size(min = 5) @FreezeBy(PARAMETER_NAME) List<String> authors,
        @Size(min = 7) @FreezeBy(PARAMETER_NAME) List<String> quotes,
        Book book
    ) {
        assertThat(book.getAuthors()).hasSize(5);
        assertThat(book.getQuotes()).hasSize(7);
    }

    @Test
    @AutoParams
    void testMethodDslSet(Product product, @Max(5) int rating, ResolutionContext context) {
        context.customize(
            set(Review::getProduct).to(product),
            set(Review::getRating).to(rating)
        );
        Review review = context.resolve();
        assertSame(product, review.getProduct());
        assertEquals(rating, review.getRating());
    }
}
