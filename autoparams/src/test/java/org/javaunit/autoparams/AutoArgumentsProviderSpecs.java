package org.javaunit.autoparams;

import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.CsvSource;

public class AutoArgumentsProviderSpecs {

    private static Method getMethod(String methodName) {
        Method[] methods = AutoArgumentsProviderSpecs.class.getMethods();
        return stream(methods).filter(m -> m.getName().equals(methodName)).findFirst().orElse(null);
    }

    @Test
    void sut_implements_ArgumentsProvider() {
        assertThat(AutoArgumentsProvider.class.getInterfaces()).contains(ArgumentsProvider.class);
    }

    public void hasSingleParameter(int a) {
    }

    public void hasTwoParameters(int a0, int a1) {
    }

    @ParameterizedTest
    @CsvSource({"hasSingleParameter, 1", "hasTwoParameters, 2"})
    void sut_provides_arguments_as_many_as_parameters(String methodName, int count)
        throws Exception {
        AutoArgumentsProvider sut = new AutoArgumentsProvider();
        ExtensionContext context = mock(ExtensionContext.class);
        when(context.getTestMethod()).thenReturn(Optional.of(getMethod(methodName)));

        Stream<? extends Arguments> actual = sut.provideArguments(context);

        for (Arguments arguments : actual.collect(Collectors.toList())) {
            assertThat(arguments.get()).hasSize(count);
        }
    }

    public void hasBigDecimalParameters(BigDecimal a0) {
    }

    @Test
    void sut_creates_arbitrary_BigDecimal_value() throws Exception {
        AutoArgumentsProvider sut = new AutoArgumentsProvider();
        ExtensionContext context = getExtensionContext("hasBigDecimalParameters");

        int count = 100;
        HashSet<BigDecimal> actual = new HashSet<BigDecimal>();
        for (int i = 0; i < count; i++) {
            sut.provideArguments(context).map(args -> (BigDecimal) args.get()[0])
                .forEach(actual::add);
        }

        assertThat(actual).hasSize(count);
    }

    public void hasStringParameters(String a0) {
    }

    @Test
    void sut_creates_arbitrary_String_value() throws Exception {
        AutoArgumentsProvider sut = new AutoArgumentsProvider();
        ExtensionContext context = getExtensionContext("hasStringParameters");

        int count = 100;
        HashSet<String> actual = new HashSet<String>();
        for (int i = 0; i < count; i++) {
            sut.provideArguments(context).map(args -> (String) args.get()[0]).forEach(actual::add);
        }

        assertThat(actual).hasSize(count);
    }

    public void hasUuidParameters(UUID a0) {
    }

    @Test
    void sut_creates_arbitrary_uuid_value() throws Exception {
        AutoArgumentsProvider sut = new AutoArgumentsProvider();
        ExtensionContext context = getExtensionContext("hasUuidParameters");

        int count = 100;
        HashSet<UUID> actual = new HashSet<UUID>();
        for (int i = 0; i < count; i++) {
            sut.provideArguments(context).map(args -> (UUID) args.get()[0]).forEach(actual::add);
        }

        assertThat(actual).hasSize(count);
    }

    public void hasComplexObjectParameter(ComplexObject a0) {
    }

    @Test
    void sut_creates_arbitrary_complex_object() throws Exception {
        AutoArgumentsProvider sut = new AutoArgumentsProvider();
        ExtensionContext context = getExtensionContext("hasComplexObjectParameter");

        Object actual = sut.provideArguments(context).map(args -> args.get()[0])
            .collect(Collectors.toList()).get(0);

        assertThat(actual).isInstanceOf(ComplexObject.class);
    }

    public void hasMoreComplexObjectParameter(MoreComplexObject a0) {
    }

    @Test
    void sut_creates_arbitrary_object_having_complex_object() throws Exception {
        AutoArgumentsProvider sut = new AutoArgumentsProvider();
        ExtensionContext context = getExtensionContext("hasMoreComplexObjectParameter");

        Object actual = sut.provideArguments(context).map(args -> args.get()[0])
            .collect(Collectors.toList()).get(0);

        assertThat(actual).isInstanceOf(MoreComplexObject.class);
    }

    private ExtensionContext getExtensionContext(String methodName) {
        ExtensionContext context = mock(ExtensionContext.class);
        when(context.getTestMethod()).thenReturn(Optional.of(getMethod(methodName)));
        return context;
    }

}
