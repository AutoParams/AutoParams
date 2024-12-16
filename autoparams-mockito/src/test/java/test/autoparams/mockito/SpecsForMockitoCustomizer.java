package test.autoparams.mockito;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.IntSupplier;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import autoparams.mockito.MockitoCustomizer;
import org.junit.jupiter.params.ParameterizedTest;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class SpecsForMockitoCustomizer {

    @Target(METHOD)
    @Retention(RUNTIME)
    @ParameterizedTest
    @AutoSource
    @Customization(MockitoCustomizer.class)
    @interface AutoMockitoParameterizedTest {
    }

    @AutoMockitoParameterizedTest
    void sut_correctly_creates_value_for_argument_of_interface(IntSupplier value) {
        assertNotNull(value);
    }

    @AutoMockitoParameterizedTest
    void sut_yields_for_concrete_type(ConcreteClass value) {
        assertEquals(ConcreteClass.class, value.getClass());
    }

    @AutoMockitoParameterizedTest
    void sut_correctly_creates_value_for_argument_of_abstract_class(AbstractClass value) {
        assertNotNull(value);
    }

    @AutoMockitoParameterizedTest
    void sut_supports_generic_interface(GenericInterface<Integer> value) {
        assertNotNull(value);
    }

    @SuppressWarnings("unused")
    @AutoMockitoParameterizedTest
    void sut_ignores_parameters_of_primitive_types(int arg1, double arg2) {
    }

    @SuppressWarnings("unused")
    @AutoMockitoParameterizedTest
    void sut_ignores_array(int[] arg1) {
    }

    @AutoMockitoParameterizedTest
    void sut_ignores_iterable(Iterable<Integer> arg) {
        assertThat(arg).isInstanceOf(ArrayList.class);
    }

    @AutoMockitoParameterizedTest
    void sut_ignores_collection(Collection<Integer> arg) {
        assertThat(arg).isInstanceOf(ArrayList.class);
    }

    @AutoMockitoParameterizedTest
    void sut_ignores_abstract_collection(AbstractCollection<Integer> arg) {
        assertThat(arg).isInstanceOf(ArrayList.class);
    }

    @AutoMockitoParameterizedTest
    void sut_ignores_list(List<Integer> arg) {
        assertThat(arg).isInstanceOf(ArrayList.class);
    }

    @AutoMockitoParameterizedTest
    void sut_ignores_abstract_list(AbstractList<Integer> arg) {
        assertThat(arg).isInstanceOf(ArrayList.class);
    }

    @AutoMockitoParameterizedTest
    void sut_ignores_set(Set<Integer> arg) {
        assertThat(arg).isInstanceOf(HashSet.class);
    }

    @AutoMockitoParameterizedTest
    void sut_ignores_abstract_set(AbstractSet<Integer> arg) {
        assertThat(arg).isInstanceOf(HashSet.class);
    }

    @AutoMockitoParameterizedTest
    void sut_ignores_map(Map<String, Integer> arg) {
        assertThat(arg).isInstanceOf(HashMap.class);
    }

    @AutoMockitoParameterizedTest
    void sut_ignores_abstract_map(AbstractMap<String, Integer> arg) {
        assertThat(arg).isInstanceOf(HashMap.class);
    }

    @AutoMockitoParameterizedTest
    void sut_does_not_stub_non_abstract_method(IntContainer arg) {
        when(arg.getValue()).thenReturn(100);
        assertThat(arg.square()).isEqualTo(10000);
    }
}
