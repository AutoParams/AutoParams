package test.autoparams;

import java.lang.reflect.Method;
import java.util.Optional;

import autoparams.AutoSource;
import autoparams.PlainParameterContext;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForPlainParameterContext {

    @Deprecated
    @ParameterizedTest
    @AutoSource
    void getTarget_returns_some_if_target_is_present(
        Object target
    ) throws NoSuchMethodException {
        Method method = getClass().getMethod("method", String.class);
        int index = 0;
        PlainParameterContext sut = new PlainParameterContext(
            method.getParameters()[index],
            index,
            target
        );

        Optional<Object> actual = sut.getTarget();

        assertThat(actual).isPresent().contains(target);
    }

    public void method(String a) {
    }
}
