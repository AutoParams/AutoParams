package test.autoparams.generator;

import java.util.ArrayList;
import java.util.UUID;

import autoparams.AutoParams;
import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.generator.Factory;
import autoparams.generator.ObjectGeneratorBase;
import autoparams.type.TypeReference;
import org.junit.jupiter.api.Test;
import test.autoparams.Product;

import static autoparams.customization.dsl.ArgumentCustomizationDsl.freezeArgument;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    void create_with_varargs_infers_class_correctly() {
        Factory<Product> factory = Factory.create();
        Product product = factory.get();
        assertThat(product).isNotNull();
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void create_with_varargs_has_null_guard() {
        assertThatThrownBy(() -> Factory.create((Object[]) null))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("typeHint");
    }

    @Test
    void create_with_varargs_has_guard_against_non_empty_array() {
        assertThatThrownBy(() -> Factory.create(new String[1]))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("typeHint");
    }

    @Test
    void create_with_varargs_has_guard_against_generic_class() {
        assertThatThrownBy(() -> Factory.create(new ArrayList[0]))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    public static class StringFreezer extends ObjectGeneratorBase<String> {

        private final String value;

        public StringFreezer(String value) {
            this.value = value;
        }

        @Override
        protected String generateObject(
            ObjectQuery query,
            ResolutionContext context
        ) {
            return value;
        }
    }

    @Test
    @AutoParams
    void create_with_ResolutionContext_and_varargs_infers_class_correctly(
        String value
    ) {
        ResolutionContext context = new ResolutionContext();
        context.customize(new StringFreezer(value));

        Factory<String> factory = Factory.create(context);

        String actual = factory.get();
        assertThat(actual).isEqualTo(value);
    }

    @Test
    void create_with_ResolutionContext_and_varargs_has_guard_against_null_context() {
        assertThatThrownBy(() -> Factory.create((ResolutionContext) null))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("context");
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    @AutoParams
    void create_with_ResolutionContext_and_varargs_has_guard_against_null_typeHint(
        ResolutionContext context
    ) {
        assertThatThrownBy(() -> Factory.create(context, (Object[]) null))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("typeHint");
    }

    @Test
    @AutoParams
    void create_with_ResolutionContext_and_varargs_has_guard_against_non_empty_array(
        ResolutionContext context
    ) {
        assertThatThrownBy(() -> Factory.create(context, new String[1]))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("typeHint");
    }

    @Test
    @AutoParams
    void create_with_ResolutionContext_and_varargs_has_guard_against_generic_class(
        ResolutionContext context
    ) {
        assertThatThrownBy(() -> Factory.create(context, new ArrayList[0]))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_has_guard_against_null_type() {
        assertThatThrownBy(() -> Factory.create((Class<?>) null))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("type");
    }
}
