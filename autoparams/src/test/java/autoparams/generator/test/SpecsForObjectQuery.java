package autoparams.generator.test;

import autoparams.generator.ObjectQuery;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class SpecsForObjectQuery {

    @Test
    void query_from_parameter_stringifies_self_with_parameter_information()
        throws NoSuchMethodException, SecurityException {

        // Arrange
        Method method = getClass().getDeclaredMethod("consumeInt", int.class);
        Parameter parameter = method.getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);

        // Act
        String actual = query.toString();

        // Assert
        Assertions.assertThat(actual).contains("int");
    }

    void consumeInt(int arg) {
    }
}
