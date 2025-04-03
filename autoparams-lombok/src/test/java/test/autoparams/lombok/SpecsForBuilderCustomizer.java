package test.autoparams.lombok;

import java.math.BigDecimal;
import java.util.UUID;

import autoparams.AutoSource;
import autoparams.ResolutionContext;
import autoparams.lombok.BuilderCustomizer;
import lombok.Builder;
import lombok.Getter;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForBuilderCustomizer {

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

    @ParameterizedTest
    @AutoSource
    void sut_creates_instance_using_builder(ResolutionContext context) {
        context.applyCustomizer(new BuilderCustomizer());
        Order order = context.resolve(Order.class);
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

    @ParameterizedTest
    @AutoSource
    void sut_creates_instance_using_configured_builder(
        ResolutionContext context
    ) {
        context.applyCustomizer(new BuilderCustomizer("getBuilder", "create"));
        Shipment shipment = context.resolve(Shipment.class);
        assertThat(shipment.getId()).isNotNull();
        assertThat(shipment.getOrderId()).isNotNull();
        assertThat(shipment.getPostalCode()).isNotNull();
        assertThat(shipment.getAddress()).isNotNull();
        assertThat(shipment.getShipped()).isNotNull();
    }
}
