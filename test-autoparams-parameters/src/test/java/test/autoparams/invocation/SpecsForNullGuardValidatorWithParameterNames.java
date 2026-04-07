package test.autoparams.invocation;

import autoparams.AutoParams;
import autoparams.ResolutionContext;
import autoparams.invocation.NullGuardValidator;
import org.junit.jupiter.api.Test;

import static autoparams.invocation.Selectors.parameter;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("unused")
public class SpecsForNullGuardValidatorWithParameterNames {

    public static class GuardedNameUnguardedNote {

        public GuardedNameUnguardedNote(String name, String note) {
            if (name == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    @Test
    @AutoParams
    void sut_skips_a_parameter_excluded_by_ParameterSelector_with_name(ResolutionContext context) {
        NullGuardValidator sut = new NullGuardValidator(context);

        assertThatCode(() -> sut.validate(
            GuardedNameUnguardedNote.class,
            q -> q.exclude(parameter("note"))
        )).doesNotThrowAnyException();
    }

    @Test
    @AutoParams
    void sut_includes_parameter_name_in_the_message_when_available(ResolutionContext context) {
        NullGuardValidator sut = new NullGuardValidator(context);

        assertThatThrownBy(() -> sut.validate(
            GuardedNameUnguardedNote.class
        )).isInstanceOf(AssertionError.class)
            .hasMessageContaining("parameter 'note' at index 1");
    }
}
