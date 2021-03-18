package org.javaunit.autoparams;

import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
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
import org.junit.jupiter.params.provider.ValueSource;

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

    @Test
    void sut_creates_single_set() throws Exception {
        AutoArgumentsProvider sut = new AutoArgumentsProvider();
        ExtensionContext context = mock(ExtensionContext.class);
        when(context.getTestMethod()).thenReturn(Optional.of(getMethod("hasSingleParameter")));

        List<Arguments> actual = sut.provideArguments(context).collect(Collectors.toList());

        assertThat(actual.size()).isEqualTo(1);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 10})
    void sut_applies_repeat(int repeat) throws Exception {
        AutoArgumentsProvider sut = new AutoArgumentsProvider();
        AutoSource annotation = mock(AutoSource.class);
        when(annotation.repeat()).thenReturn(repeat);
        ExtensionContext context = getExtensionContext("hasSingleParameter");

        sut.accept(annotation);

        Stream<? extends Arguments> actual = sut.provideArguments(context);
        assertThat(actual.toArray()).hasSize(repeat);
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

    public void hasIntegerParameters(int a0, Integer a1) {
    }

    @Test
    void sut_creates_arbitrary_int_value() throws Exception {
        AutoArgumentsProvider sut = new AutoArgumentsProvider();
        ExtensionContext context = getExtensionContext("hasIntegerParameters");

        int count = 100;
        HashSet<Integer> actual = new HashSet<Integer>();
        for (int i = 0; i < count; i++) {
            sut.provideArguments(context).map(args -> (Integer) args.get()[0]).forEach(actual::add);
        }

        assertThat(actual).hasSize(count);
    }

    @Test
    void sut_creates_arbitrary_Integer_value() throws Exception {
        AutoArgumentsProvider sut = new AutoArgumentsProvider();
        ExtensionContext context = getExtensionContext("hasIntegerParameters");

        int count = 100;
        HashSet<Integer> actual = new HashSet<Integer>();
        for (int i = 0; i < count; i++) {
            sut.provideArguments(context).map(args -> (Integer) args.get()[1]).forEach(actual::add);
        }

        assertThat(actual).hasSize(count);
    }

    public void hasLongParameters(long a0, Long a1) {
    }

    @Test
    void sut_creates_arbitrary_long_value() throws Exception {
        AutoArgumentsProvider sut = new AutoArgumentsProvider();
        ExtensionContext context = getExtensionContext("hasLongParameters");

        int count = 100;
        HashSet<Long> actual = new HashSet<Long>();
        for (int i = 0; i < count; i++) {
            sut.provideArguments(context).map(args -> (Long) args.get()[0]).forEach(actual::add);
        }

        assertThat(actual).hasSize(count);
    }

    @Test
    void sut_creates_arbitrary_Long_value() throws Exception {
        AutoArgumentsProvider sut = new AutoArgumentsProvider();
        ExtensionContext context = getExtensionContext("hasLongParameters");

        int count = 100;
        HashSet<Long> actual = new HashSet<Long>();
        for (int i = 0; i < count; i++) {
            sut.provideArguments(context).map(args -> (Long) args.get()[1]).forEach(actual::add);
        }

        assertThat(actual).hasSize(count);
    }

    public void hasFloatParameters(float a0, Float a1) {
    }

    @Test
    void sut_creates_arbitrary_float_value() throws Exception {
        AutoArgumentsProvider sut = new AutoArgumentsProvider();
        ExtensionContext context = getExtensionContext("hasFloatParameters");

        int count = 100;
        HashSet<Float> actual = new HashSet<Float>();
        for (int i = 0; i < count; i++) {
            sut.provideArguments(context).map(args -> (Float) args.get()[0]).forEach(actual::add);
        }

        assertThat(actual).hasSize(count);
    }

    @Test
    void sut_creates_arbitrary_Float_value() throws Exception {
        AutoArgumentsProvider sut = new AutoArgumentsProvider();
        ExtensionContext context = getExtensionContext("hasFloatParameters");

        int count = 100;
        HashSet<Float> actual = new HashSet<Float>();
        for (int i = 0; i < count; i++) {
            sut.provideArguments(context).map(args -> (Float) args.get()[1]).forEach(actual::add);
        }

        assertThat(actual).hasSize(count);
    }

    public void hasDoubleParameters(double a0, Double a1) {
    }

    @Test
    void sut_creates_arbitrary_double_value() throws Exception {
        AutoArgumentsProvider sut = new AutoArgumentsProvider();
        ExtensionContext context = getExtensionContext("hasDoubleParameters");

        int count = 100;
        HashSet<Double> actual = new HashSet<Double>();
        for (int i = 0; i < count; i++) {
            sut.provideArguments(context).map(args -> (Double) args.get()[0]).forEach(actual::add);
        }

        assertThat(actual).hasSize(count);
    }

    @Test
    void sut_creates_arbitrary_Double_value() throws Exception {
        AutoArgumentsProvider sut = new AutoArgumentsProvider();
        ExtensionContext context = getExtensionContext("hasDoubleParameters");

        int count = 100;
        HashSet<Double> actual = new HashSet<Double>();
        for (int i = 0; i < count; i++) {
            sut.provideArguments(context).map(args -> (Double) args.get()[1]).forEach(actual::add);
        }

        assertThat(actual).hasSize(count);
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

    public void hasBooleanParameters(boolean a0, Boolean a1) {
    }

    @Test
    void sut_creates_arbitrary_boolean_value() throws Exception {
        AutoArgumentsProvider sut = new AutoArgumentsProvider();
        ExtensionContext context = getExtensionContext("hasBooleanParameters");

        int count = 100;
        HashSet<Boolean> actual = new HashSet<Boolean>();
        for (int i = 0; i < count; i++) {
            sut.provideArguments(context).map(args -> (Boolean) args.get()[0]).forEach(actual::add);
        }

        assertThat(actual).hasSize(2);
    }

    @Test
    void sut_creates_arbitrary_Boolean_value() throws Exception {
        AutoArgumentsProvider sut = new AutoArgumentsProvider();
        ExtensionContext context = getExtensionContext("hasBooleanParameters");

        int count = 100;
        HashSet<Boolean> actual = new HashSet<Boolean>();
        for (int i = 0; i < count; i++) {
            sut.provideArguments(context).map(args -> (Boolean) args.get()[1]).forEach(actual::add);
        }

        assertThat(actual).hasSize(2);
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

    public void hasEnumObjectParameter(EnumType a0) {
    }

    @Test
    void sut_creates_arbitrary_Enum_value() throws Exception {
        AutoArgumentsProvider sut = new AutoArgumentsProvider();
        ExtensionContext context = getExtensionContext("hasEnumObjectParameter");

        int count = 100;
        HashSet<Enum<?>> actual = new HashSet<>();
        for (int i = 0; i < count; i++) {
            sut.provideArguments(context).map(args -> (Enum<?>) args.get()[0]).forEach(actual::add);
        }

        assertThat(actual).hasSize(EnumType.values().length);
    }

    private ExtensionContext getExtensionContext(String methodName) {
        ExtensionContext context = mock(ExtensionContext.class);
        when(context.getTestMethod()).thenReturn(Optional.of(getMethod(methodName)));
        return context;
    }

}
