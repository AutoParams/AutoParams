package test.autoparams.customization;

import java.lang.reflect.Type;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import autoparams.customization.Customizer;
import autoparams.customization.Fix;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import org.junit.jupiter.params.ParameterizedTest;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

@Deprecated
@SuppressWarnings("DefaultAnnotationParam")
class SpecsForFix {

    @ParameterizedTest
    @AutoSource
    void sut_fixes_value_by_exact_type(@Fix(byExactType = true) String value1, String value2) {
        assertSame(value1, value2);
    }

    @ParameterizedTest
    @AutoSource
    void sut_does_not_fix_by_exact_type(@Fix(byExactType = false) String value1, String value2) {
        assertNotSame(value1, value2);
    }

    @ParameterizedTest
    @AutoSource
    void default_value_of_byExactType_is_true(@Fix String value1, String value2) {
        assertSame(value1, value2);
    }

    @ParameterizedTest
    @AutoSource
    void sut_fixes_value_by_implemented_interfaces(
        @Fix(byImplementedInterfaces = true) ServiceImplementor service,
        ServiceClient client
    ) {
        assertSame(service, client.getServiceA());
        assertSame(service, client.getServiceB());
    }

    public static class ServiceCustomizer implements Customizer {

        @Override
        public ObjectGenerator customize(ObjectGenerator generator) {
            return (query, context) -> generator
                .generate(query, context)
                .yieldIfEmpty(() -> generate(query.getType()));
        }

        private ObjectContainer generate(Type type) {
            if (type.equals(ServiceA.class)) {
                return new ObjectContainer(new ServiceImplementor());
            } else if (type.equals(ServiceB.class)) {
                return new ObjectContainer(new ServiceImplementor());
            } else {
                return ObjectContainer.EMPTY;
            }
        }
    }

    @ParameterizedTest
    @AutoSource
    @Customization(ServiceCustomizer.class)
    void sut_does_not_fix_by_implemented_interfaces(
        @Fix(byImplementedInterfaces = false) ServiceImplementor value1,
        ServiceA value2,
        ServiceB value3
    ) {
        assertNotSame(value1, value2);
        assertNotSame(value1, value3);
    }

    @ParameterizedTest
    @AutoSource
    @Customization(ServiceCustomizer.class)
    void default_value_of_byImplementedInterfaces_is_false(
        @Fix ServiceImplementor value1,
        ServiceA value2,
        ServiceB value3
    ) {
        assertNotSame(value1, value2);
        assertNotSame(value1, value3);
    }
}
