package test.autoparams.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import autoparams.type.TypeReference;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForTypeReference {

    @Test
    void sut_resolves_type_correctly() {
        Type actual = new TypeReference<String>() { }.getType();
        assertThat(actual).isEqualTo(String.class);
    }

    @Test
    void sut_resolves_generic_type() {
        Type actual = new TypeReference<ArrayList<String>>() { }.getType();
        assertThat(actual).isInstanceOf(ParameterizedType.class);
    }

    @Test
    void sut_resolves_generic_type_correctly() {
        Type type = new TypeReference<ArrayList<String>>() { }.getType();
        ParameterizedType actual = (ParameterizedType) type;
        assertThat(actual.getRawType()).isEqualTo(ArrayList.class);
        assertThat(actual.getActualTypeArguments()).hasSize(1);
        assertThat(actual.getActualTypeArguments()).contains(String.class);
    }
}
