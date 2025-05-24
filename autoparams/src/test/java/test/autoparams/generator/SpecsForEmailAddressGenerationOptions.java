package test.autoparams.generator;

import java.net.URL;
import java.util.List;

import autoparams.AutoSource;
import autoparams.generator.EmailAddressGenerationOptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SpecsForEmailAddressGenerationOptions {

    @SuppressWarnings("DataFlowIssue")
    @Test
    void constructor_has_guard_against_null_domains() {
        assertThatThrownBy(() -> new EmailAddressGenerationOptions(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("domains");
    }

    @Test
    void constructor_has_guard_against_empty_domains() {
        String[] domains = { };
        assertThatThrownBy(() -> new EmailAddressGenerationOptions(domains))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("domains");
    }

    @Test
    void constructor_has_guard_against_null_element_in_domain() {
        String[] domains = { null };
        assertThatThrownBy(() -> new EmailAddressGenerationOptions(domains))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("domains");
    }

    @ParameterizedTest
    @AutoSource
    void constructor_correctly_sets_domains(List<URL> urls) {
        String[] domains = urls
            .stream()
            .map(URL::getHost)
            .toArray(String[]::new);

        EmailAddressGenerationOptions sut =
            new EmailAddressGenerationOptions(domains);

        assertThat(sut.domains()).containsExactlyInAnyOrder(domains);
    }

    @SuppressWarnings("DataFlowIssue")
    @ParameterizedTest
    @AutoSource
    void domains_returns_immutable_list(List<URL> urls) {
        String[] domains = urls
            .stream()
            .map(URL::getHost)
            .toArray(String[]::new);

        EmailAddressGenerationOptions sut =
            new EmailAddressGenerationOptions(domains);

        assertThatThrownBy(() -> sut.domains().set(0, "new.domain"))
            .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void default_instance_has_single_domain() {
        EmailAddressGenerationOptions sut =
            EmailAddressGenerationOptions.DEFAULT;

        assertThat(sut.domains()).containsExactlyInAnyOrder("test.com");
    }

    @Test
    void toString_returns_a_string_starting_with_the_class_name() {
        // Arrange
        EmailAddressGenerationOptions sut = EmailAddressGenerationOptions.DEFAULT;

        // Act
        String actual = sut.toString();

        // Assert
        assertThat(actual).startsWith("EmailAddressGenerationOptions");
    }

    @Test
    void toString_properly_formats_the_domains_list_with_square_brackets() {
        // Arrange
        EmailAddressGenerationOptions sut = new EmailAddressGenerationOptions(
            new String[] { "example.com", "test.org", "gmail.com" }
        );

        // Act
        String actual = sut.toString();

        // Assert
        assertThat(actual)
            .contains("domains=[\"example.com\", \"test.org\", \"gmail.com\"]");
    }

    @Test
    void toString_surrounds_the_entire_output_with_brackets_after_the_class_name() {
        // Arrange
        EmailAddressGenerationOptions sut = new EmailAddressGenerationOptions(
            new String[] { "test.domain" }
        );

        // Act
        String actual = sut.toString();

        // Assert
        assertThat(actual).matches("EmailAddressGenerationOptions\\[.*]");
    }
}
