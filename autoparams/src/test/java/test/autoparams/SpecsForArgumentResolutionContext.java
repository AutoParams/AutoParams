package test.autoparams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;

import autoparams.ArgumentResolutionContext;
import autoparams.AutoSource;
import autoparams.ResolutionContext;
import autoparams.ValueAutoSource;
import org.junit.jupiter.params.ParameterizedTest;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForArgumentResolutionContext {

    @Target({ ElementType.PARAMETER })
    @Retention(RUNTIME)
    public @interface SimpleAnnotation {
    }

    @Target({ ElementType.PARAMETER })
    @Retention(RUNTIME)
    @Repeatable(RepeatableAnnotations.class)
    public @interface RepeatableAnnotation {

        int value();
    }

    @Target({ ElementType.PARAMETER })
    @Retention(RUNTIME)
    public @interface RepeatableAnnotations {

        RepeatableAnnotation[] value();
    }

    @SuppressWarnings("unused")
    @ParameterizedTest
    @AutoSource
    void getResolutionContext_returns_resolution_context(
        @SimpleAnnotation int x,
        @RepeatableAnnotation(1024) @RepeatableAnnotation(65536) int y,
        ResolutionContext context
    ) {
        Method method = context.getExtensionContext().getRequiredTestMethod();
        int index = 0;
        Parameter parameter = method.getParameters()[index];
        ArgumentResolutionContext sut = new ArgumentResolutionContext(
            context, parameter,
            index
        );

        ResolutionContext actual = sut.getResolutionContext();

        assertThat(actual).isSameAs(context);
    }

    @SuppressWarnings("unused")
    @ParameterizedTest
    @AutoSource
    void getParameter_returns_parameter(
        @SimpleAnnotation int x,
        @RepeatableAnnotation(1024) @RepeatableAnnotation(65536) int y,
        ResolutionContext context
    ) {
        Method method = context.getExtensionContext().getRequiredTestMethod();
        int index = 0;
        Parameter parameter = method.getParameters()[index];
        ArgumentResolutionContext sut = new ArgumentResolutionContext(
            context, parameter,
            index
        );

        Parameter actual = sut.getParameter();

        assertThat(actual).isSameAs(parameter);
    }

    @SuppressWarnings("unused")
    @ParameterizedTest
    @ValueAutoSource(ints = { 1, 2 })
    void getIndex_returns_index(
        int index,
        @SimpleAnnotation int x,
        @RepeatableAnnotation(1024) @RepeatableAnnotation(65536) int y,
        ResolutionContext context
    ) {
        Method method = context.getExtensionContext().getRequiredTestMethod();
        Parameter parameter = method.getParameters()[index];
        ArgumentResolutionContext sut = new ArgumentResolutionContext(
            context, parameter,
            index
        );

        int actual = sut.getIndex();

        assertThat(actual).isEqualTo(index);
    }

    @SuppressWarnings("unused")
    @ParameterizedTest
    @AutoSource
    void getTarget_returns_none_if_target_is_not_present(
        @SimpleAnnotation int x,
        @RepeatableAnnotation(1024) @RepeatableAnnotation(65536) int y,
        ResolutionContext context
    ) {
        Method method = context.getExtensionContext().getRequiredTestMethod();
        int index = 0;
        Parameter parameter = method.getParameters()[index];
        ArgumentResolutionContext sut = new ArgumentResolutionContext(
            context,
            parameter,
            index
        );

        Optional<Object> actual = sut.getTarget();

        assertThat(actual).isEmpty();
    }

    @SuppressWarnings("unused")
    @ParameterizedTest
    @AutoSource
    void getTarget_returns_some_if_target_is_present(
        @SimpleAnnotation int x,
        @RepeatableAnnotation(1024) @RepeatableAnnotation(65536) int y,
        ResolutionContext context
    ) {
        Method method = context.getExtensionContext().getRequiredTestMethod();
        int index = 0;
        Parameter parameter = method.getParameters()[index];
        ArgumentResolutionContext sut = new ArgumentResolutionContext(
            context,
            parameter,
            index,
            this
        );

        Optional<Object> actual = sut.getTarget();

        assertThat(actual).isPresent().contains(this);
    }

    @SuppressWarnings("unused")
    @ParameterizedTest
    @AutoSource
    void isAnnotated_returns_true_if_parameter_is_annotated(
        @SimpleAnnotation int x,
        @RepeatableAnnotation(1024) @RepeatableAnnotation(65536) int y,
        ResolutionContext context
    ) {
        Method method = context.getExtensionContext().getRequiredTestMethod();
        int index = 0;
        Parameter parameter = method.getParameters()[index];
        ArgumentResolutionContext sut = new ArgumentResolutionContext(
            context,
            parameter,
            index
        );

        boolean actual = sut.isAnnotated(SimpleAnnotation.class);

        assertThat(actual).isTrue();
    }

    @SuppressWarnings("unused")
    @ParameterizedTest
    @AutoSource
    void isAnnotated_returns_false_if_parameter_is_not_annotated(
        @SimpleAnnotation int x,
        @RepeatableAnnotation(1024) @RepeatableAnnotation(65536) int y,
        ResolutionContext context
    ) {
        Method method = context.getExtensionContext().getRequiredTestMethod();
        int index = 0;
        Parameter parameter = method.getParameters()[index];
        ArgumentResolutionContext sut = new ArgumentResolutionContext(
            context,
            parameter,
            index
        );

        boolean actual = sut.isAnnotated(RepeatableAnnotation.class);

        assertThat(actual).isFalse();
    }

    @SuppressWarnings("unused")
    @ParameterizedTest
    @AutoSource
    void findAnnotation_returns_annotation_if_parameter_is_annotated(
        @SimpleAnnotation int x,
        @RepeatableAnnotation(1024) @RepeatableAnnotation(65536) int y,
        ResolutionContext context
    ) {
        Method method = context.getExtensionContext().getRequiredTestMethod();
        int index = 0;
        Parameter parameter = method.getParameters()[index];
        ArgumentResolutionContext sut = new ArgumentResolutionContext(
            context,
            parameter,
            index
        );

        Optional<SimpleAnnotation> actual =
            sut.findAnnotation(SimpleAnnotation.class);

        assertThat(actual).isPresent();
    }

    @SuppressWarnings("unused")
    @ParameterizedTest
    @AutoSource
    void findRepeatableAnnotations_returns_annotations_if_parameter_is_annotated(
        @SimpleAnnotation int x,
        @RepeatableAnnotation(1024) @RepeatableAnnotation(65536) int y,
        ResolutionContext context
    ) {
        Method method = context.getExtensionContext().getRequiredTestMethod();
        int index = 1;
        Parameter parameter = method.getParameters()[index];
        ArgumentResolutionContext sut = new ArgumentResolutionContext(
            context,
            parameter,
            index
        );

        List<RepeatableAnnotation> actual =
            sut.findRepeatableAnnotations(RepeatableAnnotation.class);

        assertThat(actual).hasSize(2);
        assertThat(actual.get(0).value()).isEqualTo(1024);
        assertThat(actual.get(1).value()).isEqualTo(65536);
    }
}
