package test.autoparams.lombok;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;

import autoparams.AutoParams;
import autoparams.AutoSource;
import autoparams.ResolutionContext;
import autoparams.customization.Customization;
import autoparams.lombok.BuilderCustomizer;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import org.junit.jupiter.api.Test;
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

    @Builder
    @Getter
    public static class User {

        @Singular private List<String> roles;
    }

    @Test
    @AutoParams
    @Customization(BuilderCustomizer.class)
    void sut_creates_instance_with_singular_list(User user) {
        assertThat(user.getRoles()).isNotNull();
        assertThat(user.getRoles()).isNotEmpty();
    }

    public abstract static class HierarchyEntity<H> {

        H parent;
        final Set<H> children = new HashSet<>();

        protected HierarchyEntity(@Nullable H parent, @Nullable Set<H> children) {
            this.parent = parent;
            if (children != null) {
                this.children.addAll(children);
            }
        }

        public H getParent() {
            return parent;
        }

        public Set<H> getChildren() {
            return children;
        }
    }

    @Getter
    public static class Category extends HierarchyEntity<Category> {

        private final String name;

        @Builder
        public Category(String name, Category parent, @Nullable Set<Category> children) {
            super(parent, children);
            this.name = name;
        }
    }

    @Test
    @AutoParams
    @Customization(BuilderCustomizer.class)
    void sut_handles_self_reference_without_stackoverflow(Category category) {
        assertThat(category).isNotNull();
        assertThat(category.getName()).isNotNull();
        assertThat(category.getParent()).isNull();
    }

    @ParameterizedTest
    @AutoSource
    @Customization(BuilderCustomizer.class)
    void sut_handles_collection_with_self_reference(Category category) {
        assertThat(category).isNotNull();
        assertThat(category.getName()).isNotNull();
        assertThat(category.getChildren()).isNotNull();
    }

    @Getter
    public static class Node {

        private final String value;
        private final Node left;
        private final Node right;

        @Builder
        public Node(String value, Node left, Node right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }
    }

    @Test
    @AutoParams
    @Customization(BuilderCustomizer.class)
    void sut_handles_tree_structure_without_stackoverflow(Node node) {
        assertThat(node).isNotNull();
        assertThat(node.getValue()).isNotNull();
        assertThat(node.getLeft()).isNull();
        assertThat(node.getRight()).isNull();
    }
}
