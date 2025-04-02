package test.autoparams.generator;

import java.util.UUID;

import autoparams.AutoParams;
import autoparams.ResolutionContext;
import autoparams.generator.Factory;
import autoparams.type.TypeReference;
import org.junit.jupiter.api.Test;
import test.autoparams.Product;

import static autoparams.customization.dsl.ArgumentCustomizationDsl.freezeArgument;
import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForFactory {

    @Test
    @AutoParams
    void get_applies_customizers_correctly(
        Factory<Product> sut,
        UUID id,
        int stockQuantity
    ) {
        Product product = sut.get(
            freezeArgument("id").to(id),
            freezeArgument("stockQuantity").to(stockQuantity)
        );
        assertThat(product.id()).isEqualTo(id);
        assertThat(product.stockQuantity()).isEqualTo(stockQuantity);
    }

    @Test
    @AutoParams
    void get_applies_customizers_only_one_time(
        Factory<Product> sut,
        UUID id,
        int stockQuantity
    ) {
        sut.get(
            freezeArgument("id").to(id),
            freezeArgument("stockQuantity").to(stockQuantity)
        );

        Product product = sut.get();

        assertThat(product.id()).isNotEqualTo(id);
        assertThat(product.stockQuantity()).isNotEqualTo(stockQuantity);
    }

    @Test
    @AutoParams
    void stream_applies_customizers_correctly(
        Factory<Product> sut,
        UUID id,
        int stockQuantity
    ) {
        Product product = sut.stream(
            freezeArgument("id").to(id),
            freezeArgument("stockQuantity").to(stockQuantity)
        ).findFirst().orElseThrow();
        assertThat(product.id()).isEqualTo(id);
        assertThat(product.stockQuantity()).isEqualTo(stockQuantity);
    }

    @Test
    @AutoParams
    void stream_applies_customizers_only_one_time(
        Factory<Product> sut,
        UUID id,
        int stockQuantity
    ) {
        sut.stream(
            freezeArgument("id").to(id),
            freezeArgument("stockQuantity").to(stockQuantity)
        ).findFirst().orElseThrow();

        Product product = sut.stream().findFirst().orElseThrow();

        assertThat(product.id()).isNotEqualTo(id);
        assertThat(product.stockQuantity()).isNotEqualTo(stockQuantity);
    }

    @Test
    @AutoParams
    void getRange_applies_customizers_correctly(
        Factory<Product> sut,
        UUID id,
        int stockQuantity
    ) {
        Product product = sut.getRange(
            1,
            freezeArgument("id").to(id),
            freezeArgument("stockQuantity").to(stockQuantity)
        ).get(0);
        assertThat(product.id()).isEqualTo(id);
        assertThat(product.stockQuantity()).isEqualTo(stockQuantity);
    }

    @Test
    @AutoParams
    void getRange_applies_customizers_only_one_time(
        Factory<Product> sut,
        UUID id,
        int stockQuantity
    ) {
        sut.getRange(
            1,
            freezeArgument("id").to(id),
            freezeArgument("stockQuantity").to(stockQuantity)
        );

        Product product = sut.getRange(1).get(0);

        assertThat(product.id()).isNotEqualTo(id);
        assertThat(product.stockQuantity()).isNotEqualTo(stockQuantity);
    }

    @Test
    @AutoParams
    void customize_applies_customizers_correctly(
        Factory<Product> sut,
        UUID id,
        int stockQuantity
    ) {
        sut.customize(
            freezeArgument("id").to(id),
            freezeArgument("stockQuantity").to(stockQuantity)
        );

        Product product = sut.get();

        assertThat(product.id()).isEqualTo(id);
        assertThat(product.stockQuantity()).isEqualTo(stockQuantity);
    }

    @Test
    void create_returns_factory_instance_with_Class() {
        Factory<Product> factory = Factory.create(Product.class);
        Product product = factory.get();
        assertThat(product).isNotNull();
    }

    @Test
    @AutoParams
    void create_returns_factory_instance_with_ResolutionContext_and_Class(
        ResolutionContext context,
        UUID id
    ) {
        Factory<Product> factory = Factory.create(context, Product.class);
        context.customize(freezeArgument("id").to(id));
        Product product = factory.get();
        assertThat(product).isNotNull();
        assertThat(product.id()).isEqualTo(id);
    }

    @Test
    void create_returns_factory_instance_with_TypeReference() {
        Factory<Product> factory = Factory.create(new TypeReference<>() { });
        Product product = factory.get();
        assertThat(product).isNotNull();
    }

    @Test
    @AutoParams
    void create_returns_factory_instance_with_ResolutionContext_and_TypeReference(
        ResolutionContext context,
        UUID id
    ) {
        Factory<Product> factory = Factory.create(
            context,
            new TypeReference<>() { }
        );
        context.customize(freezeArgument("id").to(id));
        Product product = factory.get();
        assertThat(product).isNotNull();
        assertThat(product.id()).isEqualTo(id);
    }
}
