package test.autoparams.execution;

import autoparams.execution.NullGuardValidator;
import org.junit.jupiter.api.Test;

import static autoparams.execution.Selectors.parameter;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SpecsForNullGuardValidatorWithParameterNames {

    public static class GuardedNameUnguardedNote {

        public GuardedNameUnguardedNote(String name, String note) {
            if (name == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    @Test
    void sut_skips_a_parameter_excluded_by_ParameterSelector_with_name() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatCode(() -> sut.validate(
            GuardedNameUnguardedNote.class,
            q -> q.exclude(parameter("note"))
        )).doesNotThrowAnyException();
    }

    @Test
    void sut_includes_parameter_name_in_the_message_when_available() {
        NullGuardValidator sut = new NullGuardValidator();

        assertThatThrownBy(() -> sut.validate(
            GuardedNameUnguardedNote.class
        )).isInstanceOf(AssertionError.class)
            .hasMessageContaining("parameter 'note' at index 1");
    }
}
