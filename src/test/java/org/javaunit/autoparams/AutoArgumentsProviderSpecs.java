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

    @Test
    void sut_implements_ArgumentsProvider() {
        assertThat(AutoArgumentsProvider.class.getInterfaces()).contains(ArgumentsProvider.class);
    }

    public void hasSingleParameter(int a) {
    }

    @Test
    void sut_creates_single_set() throws Exception {
        var sut = new AutoArgumentsProvider();
        var context = mock(ExtensionContext.class);
        when(context.getTestMethod()).thenReturn(Optional.of(getMethod("hasSingleParameter")));

        Stream<? extends Arguments> actual = sut.provideArguments(context);

        assertThat(actual).hasSize(1);
    }

    public void hasTwoParameters(int a0, int a1) {
    }

    @ParameterizedTest
    @CsvSource({ "hasSingleParameter, 1", "hasTwoParameters, 2" })
    void sut_provides_arguments_as_many_as_parameters(String methodName, int count) throws Exception {
        var sut = new AutoArgumentsProvider();
        var context = mock(ExtensionContext.class);
        when(context.getTestMethod()).thenReturn(Optional.of(getMethod(methodName)));

        Stream<? extends Arguments> actual = sut.provideArguments(context);

        for (Arguments arguments : actual.collect(Collectors.toList())) {
            assertThat(arguments.get()).hasSize(count);
        }
    }

    public void hasIntegerParameters(int a0, Integer a1) {
    }

    @Test
    void sut_creates_arbitrary_int_value() throws Exception {
        var sut = new AutoArgumentsProvider();
        ExtensionContext context = getExtensionContext("hasIntegerParameters");

        int count = 100;
        var actual = new HashSet<Integer>();
        for (int i = 0; i < count; i++) {
            sut.provideArguments(context).map(args -> (Integer) args.get()[0]).forEach(actual::add);
        }

        assertThat(actual).hasSize(count);
    }

    @Test
    void sut_creates_arbitrary_Integer_value() throws Exception {
        var sut = new AutoArgumentsProvider();
        ExtensionContext context = getExtensionContext("hasIntegerParameters");

        int count = 100;
        var actual = new HashSet<Integer>();
        for (int i = 0; i < count; i++) {
            sut.provideArguments(context).map(args -> (Integer) args.get()[1]).forEach(actual::add);
        }

        assertThat(actual).hasSize(count);
    }

    public void hasFloatParameters(float a0, Float a1) {
    }

    @Test
    void sut_creates_arbitrary_float_value() throws Exception {
        var sut = new AutoArgumentsProvider();
        ExtensionContext context = getExtensionContext("hasFloatParameters");

        int count = 100;
        var actual = new HashSet<Float>();
        for (int i = 0; i < count; i++) {
            sut.provideArguments(context).map(args -> (Float) args.get()[0]).forEach(actual::add);
        }

        assertThat(actual).hasSize(count);
    }

    @Test
    void sut_creates_arbitrary_Float_value() throws Exception {
        var sut = new AutoArgumentsProvider();
        ExtensionContext context = getExtensionContext("hasFloatParameters");

        int count = 100;
        var actual = new HashSet<Float>();
        for (int i = 0; i < count; i++) {
            sut.provideArguments(context).map(args -> (Float) args.get()[1]).forEach(actual::add);
        }

        assertThat(actual).hasSize(count);
    }

    public void hasDoubleParameters(double a0, Double a1) {
    }

    @Test
    void sut_creates_arbitrary_double_value() throws Exception {
        var sut = new AutoArgumentsProvider();
        ExtensionContext context = getExtensionContext("hasDoubleParameters");

        int count = 100;
        var actual = new HashSet<Double>();
        for (int i = 0; i < count; i++) {
            sut.provideArguments(context).map(args -> (Double) args.get()[0]).forEach(actual::add);
        }

        assertThat(actual).hasSize(count);
    }

    @Test
    void sut_creates_arbitrary_Double_value() throws Exception {
        var sut = new AutoArgumentsProvider();
        ExtensionContext context = getExtensionContext("hasDoubleParameters");

        int count = 100;
        var actual = new HashSet<Double>();
        for (int i = 0; i < count; i++) {
            sut.provideArguments(context).map(args -> (Double) args.get()[1]).forEach(actual::add);
        }

        assertThat(actual).hasSize(count);
    }

    public void hasBigDecimalParameters(BigDecimal a0) {
    }

    @Test
    void sut_creates_arbitrary_BigDecimal_value() throws Exception {
        var sut = new AutoArgumentsProvider();
        ExtensionContext context = getExtensionContext("hasBigDecimalParameters");

        int count = 100;
        var actual = new HashSet<BigDecimal>();
        for (int i = 0; i < count; i++) {
            sut.provideArguments(context).map(args -> (BigDecimal) args.get()[0]).forEach(actual::add);
        }

        assertThat(actual).hasSize(count);
    }

    public void hasStringParameters(String a0) {
    }

    @Test
    void sut_creates_arbitrary_String_value() throws Exception {
        var sut = new AutoArgumentsProvider();
        ExtensionContext context = getExtensionContext("hasStringParameters");

        int count = 100;
        var actual = new HashSet<String>();
        for (int i = 0; i < count; i++) {
            sut.provideArguments(context).map(args -> (String) args.get()[0]).forEach(actual::add);
        }

        assertThat(actual).hasSize(count);
    }

    public void hasUUIDParameters(UUID a0) {
    }

    @Test
    void sut_creates_arbitrary_UUID_value() throws Exception {
        var sut = new AutoArgumentsProvider();
        ExtensionContext context = getExtensionContext("hasUUIDParameters");

        int count = 100;
        var actual = new HashSet<UUID>();
        for (int i = 0; i < count; i++) {
            sut.provideArguments(context).map(args -> (UUID) args.get()[0]).forEach(actual::add);
        }

        assertThat(actual).hasSize(count);
    }

    public void hasBooleanParameters(boolean a0, Boolean a1) {
    }

    @Test
    void sut_creates_arbitrary_boolean_value() throws Exception {
        var sut = new AutoArgumentsProvider();
        ExtensionContext context = getExtensionContext("hasBooleanParameters");

        int count = 100;
        var actual = new HashSet<Boolean>();
        for (int i = 0; i < count; i++) {
            sut.provideArguments(context).map(args -> (Boolean) args.get()[0]).forEach(actual::add);
        }

        assertThat(actual).hasSize(2);
    }

    @Test
    void sut_creates_arbitrary_Boolean_value() throws Exception {
        var sut = new AutoArgumentsProvider();
        ExtensionContext context = getExtensionContext("hasBooleanParameters");

        int count = 100;
        var actual = new HashSet<Boolean>();
        for (int i = 0; i < count; i++) {
            sut.provideArguments(context).map(args -> (Boolean) args.get()[1]).forEach(actual::add);
        }

        assertThat(actual).hasSize(2);
    }

    public void hasComplexObjectParameter(ComplexObject a0) {
    }

    @Test
    void sut_creates_arbitrary_complex_object() throws Exception {
        var sut = new AutoArgumentsProvider();
        ExtensionContext context = getExtensionContext("hasComplexObjectParameter");

        Object actual = sut.provideArguments(context).map(args -> args.get()[0]).collect(Collectors.toList()).get(0);

        assertThat(actual).isInstanceOf(ComplexObject.class);
    }

    private ExtensionContext getExtensionContext(String methodName) {
        ExtensionContext context = mock(ExtensionContext.class);
        when(context.getTestMethod()).thenReturn(Optional.of(getMethod(methodName)));
        return context;
    }

    private static Method getMethod(String methodName) {
        Method[] methods = AutoArgumentsProviderSpecs.class.getMethods();
        return stream(methods).filter(m -> m.getName().equals(methodName)).findFirst().orElse(null);
    }

}
