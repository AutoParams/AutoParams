package test.autoparams;

import java.util.function.Predicate;

import autoparams.AutoSource;
import autoparams.Repeat;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForPasswordStringProperty {

    public record Container(String password, String userPassword) { }

    @ParameterizedTest
    @AutoSource
    @Repeat(100)
    void sut_generates_password(Container container) {
        assertThat(container.password()).satisfies(this::passwordRules);
    }

    @ParameterizedTest
    @AutoSource
    @Repeat(100)
    void sut_generates_password_for_property_with_password_suffix(
        Container container
    ) {
        assertThat(container.userPassword()).satisfies(this::passwordRules);
    }

    private void passwordRules(String password) {
        assertThat(password)
            .doesNotContain("password")
            .matches(
                longEnough(),
                "is at least 8 characters long"
            )
            .matches(
                containsSomeOf("abcdefghijklmnopqrstuvwxyz"),
                "contains at least one lowercase letter"
            )
            .matches(
                containsSomeOf("ABCDEFGHIJKLMNOPQRSTUVWXYZ"),
                "contains at least one uppercase letter"
            )
            .matches(
                containsSomeOf("0123456789"),
                "contains at least one digit"
            )
            .matches(
                containsSomeOf("!@#$%^&*[]{}()<>~`-=_+;:'\",./?\\| "),
                "contains at least one special character"
            )
            .matches(
                doesNotContain4ConsecutiveCharacters(),
                "does not contain 4 consecutive characters"
            )
            .matches(
                doesNotContain3RepeatedCharacters(),
                "does not contain 3 repeated characters"
            );
    }

    private Predicate<String> longEnough() {
        return password -> password.length() >= 8;
    }

    private Predicate<String> containsSomeOf(CharSequence characters) {
        return password -> {
            for (int i = 0; i < characters.length(); i++) {
                for (int j = 0; j < password.length(); j++) {
                    if (password.charAt(j) == characters.charAt(i)) {
                        return true;
                    }
                }
            }

            return false;
        };
    }

    private Predicate<String> doesNotContain4ConsecutiveCharacters() {
        return password -> {
            for (int i = 0; i < password.length() - 3; i++) {
                if (password.charAt(i) + 1 == password.charAt(i + 1) &&
                    password.charAt(i + 1) + 1 == password.charAt(i + 2) &&
                    password.charAt(i + 2) + 1 == password.charAt(i + 3)) {
                    return false;
                }
            }

            return true;
        };
    }

    private Predicate<String> doesNotContain3RepeatedCharacters() {
        return password -> {
            for (int i = 0; i < password.length() - 2; i++) {
                if (password.charAt(i) == password.charAt(i + 1) &&
                    password.charAt(i + 1) == password.charAt(i + 2)) {
                    return false;
                }
            }

            return true;
        };
    }
}
