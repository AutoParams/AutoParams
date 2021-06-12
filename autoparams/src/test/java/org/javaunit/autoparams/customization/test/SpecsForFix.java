package org.javaunit.autoparams.customization.test;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.javaunit.autoparams.AutoSource;
import org.javaunit.autoparams.customization.Customization;
import org.javaunit.autoparams.customization.Fix;
import org.javaunit.autoparams.mockito.MockitoCustomizer;
import org.junit.jupiter.params.ParameterizedTest;

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

    @ParameterizedTest
    @AutoSource
    @Customization(MockitoCustomizer.class)
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
    @Customization(MockitoCustomizer.class)
    void default_value_of_byImplementedInterfaces_is_false(
        @Fix ServiceImplementor value1,
        ServiceA value2,
        ServiceB value3
    ) {
        assertNotSame(value1, value2);
        assertNotSame(value1, value3);
    }

}
