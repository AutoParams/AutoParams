package autoparams.mockito.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import autoparams.mockito.MockitoCustomizer;
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
import org.javaunit.autoparams.AutoSource;
import org.javaunit.autoparams.customization.Customization;
import org.junit.jupiter.params.ParameterizedTest;

class SpecsForMockitoCustomizer {

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    @Customization(MockitoCustomizer.class)
    void sut_correctly_creates_value_for_argument_of_interface(IntSupplier value) {
        assertNotNull(value);
    }

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    @Customization(MockitoCustomizer.class)
    void sut_yields_for_concrete_type(ConcreteClass value) {
        assertEquals(ConcreteClass.class, value.getClass());
    }

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    @Customization(MockitoCustomizer.class)
    void sut_correctly_creates_value_for_argument_of_abstract_class(AbstractClass value) {
        assertNotNull(value);
    }

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    @Customization(MockitoCustomizer.class)
    void sut_supports_generic_interface(GenericInterface<Integer> value) {
        assertNotNull(value);
    }

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    @Customization(MockitoCustomizer.class)
    void sut_ignores_parameters_of_primitive_types(int arg1, double arg2) {
    }

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    @Customization(MockitoCustomizer.class)
    void sut_ignores_array(int[] arg1) {
    }

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    @Customization(MockitoCustomizer.class)
    void sut_ignores_iterable(Iterable<Integer> arg) {
        assertThat(arg).isInstanceOf(ArrayList.class);
    }

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    @Customization(MockitoCustomizer.class)
    void sut_ignores_collection(Collection<Integer> arg) {
        assertThat(arg).isInstanceOf(ArrayList.class);
    }

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    @Customization(MockitoCustomizer.class)
    void sut_ignores_abstract_collection(AbstractCollection<Integer> arg) {
        assertThat(arg).isInstanceOf(ArrayList.class);
    }

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    @Customization(MockitoCustomizer.class)
    void sut_ignores_list(List<Integer> arg) {
        assertThat(arg).isInstanceOf(ArrayList.class);
    }

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    @Customization(MockitoCustomizer.class)
    void sut_ignores_abstract_list(AbstractList<Integer> arg) {
        assertThat(arg).isInstanceOf(ArrayList.class);
    }

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    @Customization(MockitoCustomizer.class)
    void sut_ignores_set(Set<Integer> arg) {
        assertThat(arg).isInstanceOf(HashSet.class);
    }

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    @Customization(MockitoCustomizer.class)
    void sut_ignores_abstract_set(AbstractSet<Integer> arg) {
        assertThat(arg).isInstanceOf(HashSet.class);
    }

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    @Customization(MockitoCustomizer.class)
    void sut_ignores_map(Map<String, Integer> arg) {
        assertThat(arg).isInstanceOf(HashMap.class);
    }

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    @Customization(MockitoCustomizer.class)
    void sut_ignores_abstract_map(AbstractMap<String, Integer> arg) {
        assertThat(arg).isInstanceOf(HashMap.class);
    }

}
