package test.autoparams.generator;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import autoparams.AutoSource;
import autoparams.generator.ParameterQuery;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForParameterQuery {

    @ParameterizedTest
    @AutoSource
    void constructor_correctly_sets_index(
        int index
    ) throws NoSuchMethodException {

        Method method = getClass().getMethod("method", String.class);
        Parameter parameter = method.getParameters()[0];

        ParameterQuery sut = new ParameterQuery(
            parameter,
            index,
            parameter.getAnnotatedType().getType()
        );

        assertThat(sut.getIndex()).isEqualTo(index);
    }

    public void method(String a) {
    }
}
