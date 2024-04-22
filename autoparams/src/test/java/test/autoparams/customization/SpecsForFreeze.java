package test.autoparams.customization;

import autoparams.AutoSource;
import autoparams.ResolutionContext;
import autoparams.customization.Customization;
import autoparams.customization.Freeze;
import autoparams.generator.ObjectGeneratorBase;
import autoparams.generator.ObjectQuery;
import org.junit.jupiter.params.ParameterizedTest;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

@SuppressWarnings("DefaultAnnotationParam")
public class SpecsForFreeze {

    @ParameterizedTest
    @AutoSource
    void sut_freezes_value_by_exact_type(
        @Freeze(byExactType = true) String value1,
        String value2
    ) {
        assertSame(value1, value2);
    }

    @ParameterizedTest
    @AutoSource
    void sut_does_not_freeze_by_exact_type(
        @Freeze(byExactType = false) String value1,
        String value2
    ) {
        assertNotSame(value1, value2);
    }

    @ParameterizedTest
    @AutoSource
    void default_value_of_byExactType_is_true(
        @Freeze String value1,
        String value2
    ) {
        assertSame(value1, value2);
    }

    @ParameterizedTest
    @AutoSource
    void sut_freezes_value_by_implemented_interfaces(
        @Freeze(byImplementedInterfaces = true) ServiceImplementor service,
        ServiceClient client
    ) {
        assertSame(service, client.getServiceA());
        assertSame(service, client.getServiceB());
    }

    public static class ServiceAGenerator
        extends ObjectGeneratorBase<ServiceA> {

        @Override
        protected ServiceA generateObject(
            ObjectQuery query,
            ResolutionContext context
        ) {
            return new ServiceImplementor();
        }
    }

    public static class ServiceBGenerator
        extends ObjectGeneratorBase<ServiceB> {

        @Override
        protected ServiceB generateObject(
            ObjectQuery query,
            ResolutionContext context
        ) {
            return new ServiceImplementor();
        }
    }

    @ParameterizedTest
    @AutoSource
    @Customization({ ServiceAGenerator.class, ServiceBGenerator.class })
    void sut_does_not_freeze_by_implemented_interfaces(
        @Freeze(byImplementedInterfaces = false) ServiceImplementor value1,
        ServiceA value2,
        ServiceB value3
    ) {
        assertNotSame(value1, value2);
        assertNotSame(value1, value3);
    }

    @ParameterizedTest
    @AutoSource
    @Customization({ ServiceAGenerator.class, ServiceBGenerator.class })
    void default_value_of_byImplementedInterfaces_is_false(
        @Freeze ServiceImplementor value1,
        ServiceA value2,
        ServiceB value3
    ) {
        assertNotSame(value1, value2);
        assertNotSame(value1, value3);
    }
}
