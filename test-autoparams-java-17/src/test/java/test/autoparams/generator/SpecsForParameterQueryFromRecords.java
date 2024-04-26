package test.autoparams.generator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Optional;

import autoparams.generator.ParameterQuery;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForParameterQueryFromRecords {

    public record User(String name, String email) {
    }

    @Test
    void getParameterName_returns_name_of_record_field() {
        Constructor<?> constructor = User.class.getConstructors()[0];
        int index = 1;
        Parameter parameter = constructor.getParameters()[index];
        ParameterQuery sut = new ParameterQuery(
            parameter,
            index,
            parameter.getAnnotatedType().getType()
        );

        Optional<String> actual = sut.getParameterName();

        assertThat(actual).contains("email");
    }
}
