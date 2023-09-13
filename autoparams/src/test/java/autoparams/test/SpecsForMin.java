package autoparams.test;

import autoparams.AutoSource;
import autoparams.Repeat;
import autoparams.generator.ObjectGenerationContext;
import autoparams.generator.ObjectQuery;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SpecsForMin {

    @ParameterizedTest
    @AutoSource
    @Repeat(10)
    void sut_accepts_min_constraint_for_double(@Min(100) @Max(Short.MAX_VALUE) double value) {
        assertThat(value).isGreaterThanOrEqualTo(100);
    }

    @ParameterizedTest
    @AutoSource
    void sut_does_not_accept_min_constraint_without_max_constraint_for_double(
        ObjectGenerationContext context
    ) throws NoSuchMethodException {
        Method method = getClass().getDeclaredMethod(
            "hasDoubleParameterWithOnlyMinAnnotation",
            double.class
        );
        Parameter parameter = method.getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);

        assertThatThrownBy(() -> context.generate(query))
            .isInstanceOf(RuntimeException.class)
            .hasMessage(
                "The parameter annotated with @Min is missing the required"
                + " @Max annotation. Please annotate the parameter with"
                + " both @Min and @Max annotations to specify"
                + " the minimum and maximum allowed values.");
    }

    void hasDoubleParameterWithOnlyMinAnnotation(@Min(100) double arg) {
    }

    @ParameterizedTest
    @AutoSource
    void sut_does_not_accept_min_constraint_without_max_constraint_for_float(
        ObjectGenerationContext context
    ) throws NoSuchMethodException {
        Method method = getClass().getDeclaredMethod(
            "hasFloatParameterWithOnlyMinAnnotation",
            float.class
        );
        Parameter parameter = method.getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);

        assertThatThrownBy(() -> context.generate(query))
            .isInstanceOf(RuntimeException.class)
            .hasMessage(
                "The parameter annotated with @Min is missing the required"
                + " @Max annotation. Please annotate the parameter with"
                + " both @Min and @Max annotations to specify"
                + " the minimum and maximum allowed values.");
    }

    void hasFloatParameterWithOnlyMinAnnotation(@Min(100) float arg) {
    }
}
