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
    void constructor_has_null_guard_against_null_domains() {
        assertThatThrownBy(() -> new EmailAddressGenerationOptions(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("domains");
    }

    @Test
    void constructor_has_null_guard_against_null_element_in_domain() {
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
}
