package test.autoparams.processor;

import autoparams.ResolutionContext;
import autoparams.customization.CompositeCustomizer;
import autoparams.customization.Customization;
import autoparams.processor.InstanceFieldWriter;
import test.autoparams.AutoParameterizedTest;
import test.autoparams.customization.Entity;
import test.autoparams.customization.Inventory;
import test.autoparams.customization.Operator;
import test.autoparams.customization.User;
import test.autoparams.customization.Versioned;
import test.autoparams.customization.Worker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SpecsForInstanceFieldWriter {

    public static class DomainCustomizer extends CompositeCustomizer {

        public DomainCustomizer() {
            super(
                new InstanceFieldWriter(Versioned.class).toCustomizer(),
                new InstanceFieldWriter(Entity.class).toCustomizer(),
                new InstanceFieldWriter(Inventory.class).toCustomizer(),
                new InstanceFieldWriter(Worker.class)
                    .excluding("activeWorks", "closedWorks")
                    .toCustomizer(),
                new InstanceFieldWriter(Operator.class)
                    .including("teamName", "phoneNumber")
                    .toCustomizer(),
                new InstanceFieldWriter(User.class).toCustomizer()
            );
        }
    }

    @AutoParameterizedTest
    @Customization(DomainCustomizer.class)
    void sut_sets_fields(Versioned versioned) {
        assertNotNull(versioned.getVersion());
    }

    @AutoParameterizedTest
    @Customization(DomainCustomizer.class)
    void sut_sets_fields_in_generic_class(Entity<Long> entity) {
        assertNotNull(entity.getId());
    }

    @AutoParameterizedTest
    @Customization(DomainCustomizer.class)
    void sut_sets_inherited_fields(Worker worker) {
        assertNotNull(worker.getId());
        assertNotNull(worker.getUsername());
    }

    @AutoParameterizedTest
    @Customization(DomainCustomizer.class)
    void sut_does_not_set_static_fields(User user) {
        assertEquals("hello world", User.getDefaultGreeting());
    }

    @AutoParameterizedTest
    @Customization(DomainCustomizer.class)
    void sut_does_not_set_excluded_fields(Worker worker) {
        assertEquals(0, worker.getActiveWorks());
        assertEquals(0, worker.getClosedWorks());
    }

    @AutoParameterizedTest
    @Customization(DomainCustomizer.class)
    void sut_sets_only_included_fields(Operator operator) {
        assertNotNull(operator.getTeamName());
        assertNotNull(operator.getPhoneNumber());
        assertEquals(0, operator.getActiveWorks());
        assertEquals(0, operator.getClosedWorks());
    }

    @AutoParameterizedTest
    void including_accumulates_conditions(ResolutionContext context) {
        // Arrange
        InstanceFieldWriter sut = new InstanceFieldWriter(Operator.class)
            .excluding("activeWorks")
            .including("teamName", "activeWorks");

        context.applyCustomizer(sut.toCustomizer());

        // Act
        Operator actual = context.resolve(Operator.class);

        // Assert
        assertNotNull(actual.getTeamName());
        assertEquals(0, actual.getActiveWorks());
    }

    @AutoParameterizedTest
    void excluding_accumulates_conditions(ResolutionContext context) {
        // Arrange
        InstanceFieldWriter sut = new InstanceFieldWriter(Operator.class)
            .including("teamName", "activeWorks")
            .excluding("activeWorks");

        context.applyCustomizer(sut.toCustomizer());

        // Act
        Operator actual = context.resolve(Operator.class);

        // Assert
        assertNull(actual.getPhoneNumber());
        assertEquals(0, actual.getActiveWorks());
    }
}
