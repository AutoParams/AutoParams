package test.autoparams.execution;

import java.lang.reflect.Parameter;
import java.util.concurrent.atomic.AtomicReference;

import autoparams.AutoParams;
import autoparams.ResolutionContext;
import autoparams.customization.Customization;
import autoparams.customization.Customizer;
import autoparams.customization.FreezeBy;
import autoparams.execution.NullGuardValidator;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import org.junit.jupiter.api.Test;

import static autoparams.customization.Matching.IMPLEMENTED_INTERFACES;
import static autoparams.execution.Selectors.allConstructors;
import static autoparams.execution.Selectors.constructor;
import static autoparams.execution.Selectors.method;
import static autoparams.execution.Selectors.parameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SpecsForNullGuardValidator {

    private static final String LINE_SEPARATOR =
        System.lineSeparator();

    @Test
    void sut_throws_when_a_public_constructor_accepts_null_for_a_non_primitive_parameter() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatThrownBy(
            () -> sut.validate(UnguardedConstructor.class)
        ).isInstanceOf(AssertionError.class);
    }

    public static class UnguardedConstructor {

        public UnguardedConstructor(String value) {
        }
    }

    @Test
    void sut_does_not_throw_when_a_public_constructor_throws_IllegalArgumentException_for_null() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatCode(() -> sut.validate(GuardedConstructor.class))
            .doesNotThrowAnyException();
    }

    public static class GuardedConstructor {

        public GuardedConstructor(String value) {
            if (value == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    @Test
    void sut_throws_when_a_public_method_accepts_null_for_a_non_primitive_parameter() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatThrownBy(
            () -> sut.validate(UnguardedMethod.class)
        ).isInstanceOf(AssertionError.class);
    }

    public static class UnguardedMethod {

        public void execute(String value) {
        }
    }

    @Test
    void sut_does_not_throw_when_a_public_method_throws_IllegalArgumentException_for_null() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatCode(() -> sut.validate(GuardedMethod.class))
            .doesNotThrowAnyException();
    }

    public static class GuardedMethod {

        public void execute(String value) {
            if (value == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    @Test
    void sut_throws_AssertionError_when_a_public_static_method_accepts_null_without_throwing() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatThrownBy(
            () -> sut.validate(UnguardedStaticMethod.class)
        ).isInstanceOf(AssertionError.class);
    }

    public static class UnguardedStaticMethod {

        public static void execute(String value) {
        }
    }

    @Test
    void sut_does_not_throw_when_a_public_static_method_throws_IllegalArgumentException_for_null() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatCode(() -> sut.validate(GuardedStaticMethod.class))
            .doesNotThrowAnyException();
    }

    public static class GuardedStaticMethod {

        public static void execute(String value) {
            if (value == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    @Test
    void sut_validates_public_methods_inherited_from_a_direct_superclass() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatThrownBy(
            () -> sut.validate(InheritedUnguardedMethod.class)
        ).isInstanceOf(AssertionError.class);
    }

    public static class InheritedUnguardedMethod extends UnguardedMethod {
    }

    @Test
    void sut_validates_public_methods_inherited_from_a_grandparent_class() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatThrownBy(
            () -> sut.validate(GrandchildOfUnguardedMethod.class)
        ).isInstanceOf(AssertionError.class);
    }

    public static class GrandchildOfUnguardedMethod extends InheritedUnguardedMethod {
    }

    @Test
    void sut_does_not_throw_when_class_has_only_parameterless_public_methods() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatCode(() -> sut.validate(ParameterlessMethod.class))
            .doesNotThrowAnyException();
    }

    public static class ParameterlessMethod {

        public void execute() {
        }
    }

    @Test
    void sut_skips_primitive_parameters() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatCode(() -> sut.validate(PrimitiveParameterOnly.class))
            .doesNotThrowAnyException();
    }

    public static class PrimitiveParameterOnly {

        public PrimitiveParameterOnly(int value) {
        }
    }

    @Test
    void sut_skips_methods_declared_in_Object() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatCode(() -> sut.validate(NoCustomMethods.class))
            .doesNotThrowAnyException();
    }

    public static class NoCustomMethods {
    }

    @Test
    void sut_throws_AssertionError_when_constructor_throws_non_IllegalArgumentException_for_null() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatThrownBy(
            () -> sut.validate(ConstructorThrowingNullPointerException.class)
        ).isInstanceOf(AssertionError.class);
    }

    public static class ConstructorThrowingNullPointerException {

        public ConstructorThrowingNullPointerException(String value) {
            if (value == null) {
                throw new NullPointerException();
            }
        }
    }

    @Test
    void sut_throws_AssertionError_when_method_throws_non_IllegalArgumentException_for_null() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatThrownBy(
            () -> sut.validate(MethodThrowingNullPointerException.class)
        ).isInstanceOf(AssertionError.class);
    }

    public static class MethodThrowingNullPointerException {

        public void execute(String value) {
            if (value == null) {
                throw new NullPointerException();
            }
        }
    }

    @Test
    void sut_does_not_throw_when_exception_satisfies_custom_Predicate() {
        NullGuardValidator sut = new NullGuardValidator(
            e -> e instanceof NullPointerException
        );

        assertThatCode(
            () -> sut.validate(ConstructorThrowingNullPointerException.class)
        ).doesNotThrowAnyException();
    }

    @Test
    void sut_throws_AssertionError_when_exception_does_not_satisfy_custom_Predicate() {
        NullGuardValidator sut = new NullGuardValidator(
            e -> e instanceof NullPointerException
        );

        assertThatThrownBy(
            () -> sut.validate(GuardedMethod.class)
        ).isInstanceOf(AssertionError.class);
    }

    @Test
    void sut_does_not_throw_when_exception_satisfies_custom_BiPredicate() {
        NullGuardValidator sut = new NullGuardValidator(
            (parameter, e) -> e instanceof NullPointerException
        );

        assertThatCode(
            () -> sut.validate(ConstructorThrowingNullPointerException.class)
        ).doesNotThrowAnyException();
    }

    @Test
    void sut_throws_AssertionError_when_exception_does_not_satisfy_custom_BiPredicate() {
        NullGuardValidator sut = new NullGuardValidator(
            (parameter, e) -> e instanceof NullPointerException
        );

        assertThatThrownBy(
            () -> sut.validate(GuardedMethod.class)
        ).isInstanceOf(AssertionError.class);
    }

    @Test
    void sut_correctly_passes_the_constructor_parameter_to_BiPredicate() {
        AtomicReference<Parameter> captured = new AtomicReference<>();
        NullGuardValidator sut = new NullGuardValidator((parameter, e) -> {
            captured.set(parameter);
            return e instanceof IllegalArgumentException;
        });

        sut.validate(GuardedConstructor.class);

        Parameter expected = GuardedConstructor.class
            .getConstructors()[0].getParameters()[0];
        assertThat(captured.get()).isEqualTo(expected);
    }

    @Test
    void sut_correctly_passes_the_method_parameter_to_BiPredicate() {
        AtomicReference<Parameter> captured = new AtomicReference<>();
        NullGuardValidator sut = new NullGuardValidator((parameter, e) -> {
            captured.set(parameter);
            return e instanceof IllegalArgumentException;
        });

        sut.validate(GuardedMethod.class);

        Parameter expected = GuardedMethod.class
            .getMethods()[0].getParameters()[0];
        assertThat(captured.get()).isEqualTo(expected);
    }

    @Test
    @AutoParams
    void sut_uses_ResolutionContext_to_generate_arguments_for_non_null_constructor_parameters(
        @FreezeBy(IMPLEMENTED_INTERFACES) Circle circle,
        ResolutionContext context
    ) {
        NullGuardValidator sut = new NullGuardValidator(context);

        assertThatThrownBy(
            () -> sut.validate(PartiallyGuardedConstructor.class)
        ).isInstanceOf(AssertionError.class);
    }

    public interface Shape {
    }

    public static class Circle implements Shape {
    }

    public static class PartiallyGuardedConstructor {

        public PartiallyGuardedConstructor(Shape shape1, Shape shape2, Shape shape3) {
            if (shape1 == null) {
                throw new IllegalArgumentException();
            }
            if (shape3 == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    @Test
    @AutoParams
    void sut_uses_ResolutionContext_to_generate_arguments_for_non_null_method_parameters(
        @FreezeBy(IMPLEMENTED_INTERFACES) Circle circle,
        ResolutionContext context
    ) {
        NullGuardValidator sut = new NullGuardValidator(context);

        assertThatThrownBy(
            () -> sut.validate(PartiallyGuardedMethod.class)
        ).isInstanceOf(AssertionError.class);
    }

    public static class PartiallyGuardedMethod {

        public void execute(Shape shape1, Shape shape2, Shape shape3) {
            if (shape1 == null) {
                throw new IllegalArgumentException();
            }
            if (shape3 == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    @Test
    void sut_sets_the_message_of_AssertionError_for_a_constructor_parameter_that_accepted_null() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatThrownBy(
            () -> sut.validate(UnguardedConstructor.class)
        ).isInstanceOf(AssertionError.class)
            .hasMessage(
                "NullGuardValidator found 1 violation(s) in"
                + " UnguardedConstructor. Expected"
                + " IllegalArgumentException or an exception"
                + " satisfying the configured condition"
                + " for null arguments." + LINE_SEPARATOR
                + LINE_SEPARATOR
                + "  UnguardedConstructor(String):"
                + " parameter at index 0 accepted null"
                + " without throwing." + LINE_SEPARATOR
        );
    }

    @Test
    void sut_sets_the_message_of_AssertionError_for_a_method_parameter_that_accepted_null() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatThrownBy(
            () -> sut.validate(UnguardedMethod.class)
        ).isInstanceOf(AssertionError.class)
            .hasMessage(
                "NullGuardValidator found 1 violation(s) in"
                + " UnguardedMethod. Expected"
                + " IllegalArgumentException or an exception"
                + " satisfying the configured condition"
                + " for null arguments." + LINE_SEPARATOR
                + LINE_SEPARATOR
                + "  UnguardedMethod.execute(String):"
                + " parameter at index 0 accepted null"
                + " without throwing." + LINE_SEPARATOR
        );
    }

    @Test
    void sut_uses_declaring_class_name_in_method_signature_for_inherited_methods() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatThrownBy(
            () -> sut.validate(InheritedUnguardedMethod.class)
        ).isInstanceOf(AssertionError.class)
            .hasMessageContaining(
                "UnguardedMethod.execute(String)"
            ).satisfies(e -> assertThat(e.getMessage())
                .doesNotContain(
                    "InheritedUnguardedMethod.execute"
                )
        );
    }

    @Test
    void sut_sets_error_message_for_constructor_that_threw_rejected_exception_without_message() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatThrownBy(
            () -> sut.validate(
                ConstructorThrowingNullPointerException.class
            )
        ).isInstanceOf(AssertionError.class)
            .hasMessageContaining(
                "threw NullPointerException,"
                + " which does not satisfy the condition."
        );
    }

    @Test
    void sut_sets_error_message_for_constructor_that_threw_rejected_exception_with_message() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatThrownBy(
            () -> sut.validate(
                ConstructorThrowingNullPointerExceptionWithMessage.class
            )
        ).isInstanceOf(AssertionError.class)
            .hasMessageContaining(
                "threw NullPointerException with message"
                + " \"value must not be null\","
                + " which does not satisfy the condition."
        );
    }

    public static class ConstructorThrowingNullPointerExceptionWithMessage {

        public ConstructorThrowingNullPointerExceptionWithMessage(
            String value
        ) {
            if (value == null) {
                throw new NullPointerException(
                    "value must not be null"
                );
            }
        }
    }

    @Test
    void sut_sets_error_message_for_method_that_threw_rejected_exception_without_message() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatThrownBy(
            () -> sut.validate(
                MethodThrowingNullPointerException.class
            )
        ).isInstanceOf(AssertionError.class)
            .hasMessageContaining(
                "threw NullPointerException,"
                + " which does not satisfy the condition."
        );
    }

    @Test
    void sut_sets_error_message_for_method_that_threw_rejected_exception_with_message() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatThrownBy(
            () -> sut.validate(
                MethodThrowingNullPointerExceptionWithMessage.class
            )
        ).isInstanceOf(AssertionError.class)
            .hasMessageContaining(
                "threw NullPointerException with message"
                + " \"value must not be null\","
                + " which does not satisfy the condition."
        );
    }

    public static class MethodThrowingNullPointerExceptionWithMessage {

        public void execute(String value) {
            if (value == null) {
                throw new NullPointerException(
                    "value must not be null"
                );
            }
        }
    }

    @Test
    void sut_reports_all_violations_in_a_single_AssertionError_message() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatThrownBy(
            () -> sut.validate(TwoUnguardedMethods.class)
        ).isInstanceOf(AssertionError.class)
            .hasMessageContaining("2 violation(s)")
            .hasMessageContaining(
                "TwoUnguardedMethods.first(String)"
            ).hasMessageContaining(
                "TwoUnguardedMethods.second(String)"
        );
    }

    public static class TwoUnguardedMethods {

        public void first(String value) {
        }

        public void second(String value) {
        }
    }

    @Test
    @AutoParams
    @Customization(StaticFactoryUnguardedMethodCustomizer.class)
    void sut_uses_ResolutionContext_to_create_instances_when_validating_instance_methods(
        ResolutionContext context
    ) {
        NullGuardValidator sut = new NullGuardValidator(context);

        assertThatThrownBy(
            () -> sut.validate(StaticFactoryUnguardedMethod.class)
        ).isInstanceOf(AssertionError.class);
    }

    public static class StaticFactoryUnguardedMethod {

        private StaticFactoryUnguardedMethod() {
        }

        public static StaticFactoryUnguardedMethod create() {
            return new StaticFactoryUnguardedMethod();
        }

        public void execute(String value) {
        }
    }

    public static class StaticFactoryUnguardedMethodCustomizer
        implements Customizer {

        @Override
        public ObjectGenerator customize(ObjectGenerator generator) {
            return (query, context) ->
                query.getType() == StaticFactoryUnguardedMethod.class
                    ? new ObjectContainer(
                        StaticFactoryUnguardedMethod.create()
                    )
                    : generator.generate(query, context);
        }
    }

    @Test
    void sut_skips_a_constructor_excluded_by_ConstructorSelector_with_predicate() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatCode(() -> sut.validate(
            UnguardedConstructor.class,
            q -> q.exclude(constructor(c -> c.getParameterCount() == 1))
        )).doesNotThrowAnyException();
    }

    @Test
    void sut_skips_a_constructor_excluded_by_ConstructorSelector_with_parameter_types() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatCode(() -> sut.validate(
            UnguardedConstructor.class,
            q -> q.exclude(constructor(String.class))
        )).doesNotThrowAnyException();
    }

    @Test
    void sut_accumulates_exclusion_conditions_when_exclude_is_called_multiple_times() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatCode(() -> sut.validate(
            TwoUnguardedConstructors.class,
            q -> q
                .exclude(constructor(String.class))
                .exclude(constructor(Integer.class))
        )).doesNotThrowAnyException();
    }

    public static class TwoUnguardedConstructors {

        public TwoUnguardedConstructors(String value) {
        }

        public TwoUnguardedConstructors(Integer value) {
        }
    }

    @Test
    void sut_skips_all_constructors_when_allConstructors_selector_is_used() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatCode(() -> sut.validate(
            UnguardedConstructor.class,
            q -> q.exclude(allConstructors())
        )).doesNotThrowAnyException();
    }

    @Test
    void sut_skips_a_method_excluded_by_MethodSelector_with_predicate() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatCode(() -> sut.validate(
            UnguardedMethod.class,
            q -> q.exclude(method(m -> m.getName().equals("execute")))
        )).doesNotThrowAnyException();
    }

    @Test
    void sut_skips_a_method_excluded_by_MethodSelector_with_name() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatCode(() -> sut.validate(
            UnguardedMethod.class,
            q -> q.exclude(method("execute"))
        )).doesNotThrowAnyException();
    }

    @Test
    void sut_skips_a_method_excluded_by_MethodSelector_with_name_and_parameter_types() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatThrownBy(() -> sut.validate(
            OverloadedUnguardedMethods.class,
            q -> q.exclude(method("execute", String.class))
        )).isInstanceOf(AssertionError.class)
            .hasMessageContaining("execute(Integer)")
            .satisfies(e -> assertThat(e.getMessage())
                .doesNotContain("execute(String)")
        );
    }

    public static class OverloadedUnguardedMethods {

        public void execute(String value) {
        }

        public void execute(Integer value) {
        }
    }

    @Test
    void sut_skips_a_static_method_excluded_by_MethodSelector_with_name() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatCode(() -> sut.validate(
            UnguardedStaticMethod.class,
            q -> q.exclude(method("execute"))
        )).doesNotThrowAnyException();
    }

    @Test
    void sut_skips_a_static_method_excluded_by_MethodSelector_with_name_and_parameter_types() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatCode(() -> sut.validate(
            UnguardedStaticMethod.class,
            q -> q.exclude(method("execute", String.class))
        )).doesNotThrowAnyException();
    }

    @Test
    void sut_still_validates_non_excluded_methods_when_some_methods_are_excluded() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatThrownBy(() -> sut.validate(
            TwoUnguardedMethods.class,
            q -> q.exclude(method("first"))
        )).isInstanceOf(AssertionError.class)
            .hasMessageContaining("second(String)")
            .satisfies(e -> assertThat(e.getMessage())
                .doesNotContain("first(String)")
        );
    }

    @Test
    void sut_skips_a_parameter_excluded_by_ParameterSelector_with_predicate() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatCode(() -> sut.validate(
            GuardedStringUnguardedInteger.class,
            q -> q.exclude(parameter(p -> p.getType() == Integer.class))
        )).doesNotThrowAnyException();
    }

    public static class GuardedStringUnguardedInteger {

        public GuardedStringUnguardedInteger(String value, Integer number) {
            if (value == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    @Test
    void sut_skips_a_parameter_excluded_by_ParameterSelector_with_BiPredicate_using_index() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatCode(() -> sut.validate(
            FirstGuardedSecondUnguardedStrings.class,
            q -> q.exclude(parameter(
                (p, index) -> p.getType() == String.class && index == 1
            ))
        )).doesNotThrowAnyException();
    }

    public static class FirstGuardedSecondUnguardedStrings {

        public FirstGuardedSecondUnguardedStrings(
            String first,
            String second
        ) {
            if (first == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    @Test
    void sut_skips_a_parameter_excluded_by_ParameterSelector_scoped_to_a_method() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatThrownBy(() -> sut.validate(
            TwoUnguardedMethods.class,
            q -> q.exclude(
                parameter(p -> p.getType() == String.class)
                    .of(method("first"))
            )
        )).isInstanceOf(AssertionError.class)
            .hasMessageContaining("second(String)")
            .satisfies(e -> assertThat(e.getMessage())
                .doesNotContain("first(String)")
        );
    }

    @Test
    void sut_skips_a_parameter_excluded_by_ParameterSelector_scoped_to_a_constructor() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatThrownBy(() -> sut.validate(
            TwoConstructorsAndMethod.class,
            q -> q.exclude(
                parameter(p -> p.getType() == Integer.class)
                    .of(constructor(Integer.class))
            )
        )).isInstanceOf(AssertionError.class)
            .hasMessageContaining("TwoConstructorsAndMethod(String)")
            .hasMessageContaining("execute(Integer)")
            .satisfies(e -> assertThat(e.getMessage())
                .doesNotContain(
                    "TwoConstructorsAndMethod(Integer)"
                )
        );
    }

    public static class TwoConstructorsAndMethod {

        public TwoConstructorsAndMethod(String value) {
        }

        public TwoConstructorsAndMethod(Integer value) {
        }

        public void execute(Integer value) {
        }
    }

    @Test
    void sut_throws_when_parameter_name_is_used_but_parameter_names_are_not_available() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatThrownBy(() -> sut.validate(
            UnguardedConstructor.class,
            q -> q.exclude(parameter("value"))
        )).isInstanceOf(RuntimeException.class);
    }
}
