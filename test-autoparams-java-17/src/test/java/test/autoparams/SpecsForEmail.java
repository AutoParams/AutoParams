package test.autoparams;

import java.net.URL;
import java.util.List;

import autoparams.AutoSource;
import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.generator.EmailAddressGenerationOptions;
import autoparams.generator.Factory;
import autoparams.generator.ObjectGeneratorBase;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForEmail {

    public record SignUp(String userEmail, String password) { }

    @ParameterizedTest
    @AutoSource
    void sut_generates_email_address_for_email_suffix(SignUp command) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        assertThat(command.userEmail().matches(emailRegex)).isTrue();
        assertThat(command.password().matches(emailRegex)).isFalse();
    }

    public record User(int id, String emailAddress, String encodedPassword) { }

    @ParameterizedTest
    @AutoSource
    void sut_generates_email_address_for_emailaddress_suffix(User entity) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        assertThat(entity.emailAddress().matches(emailRegex)).isTrue();
        assertThat(entity.encodedPassword().matches(emailRegex)).isFalse();
    }

    public record Customer(int customerId, String email_address) { }

    @ParameterizedTest
    @AutoSource
    void sut_generates_email_address_for_email_address_suffix(Customer value) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        assertThat(value.email_address().matches(emailRegex)).isTrue();
    }

    public static final class OptionsProvider
        extends ObjectGeneratorBase<EmailAddressGenerationOptions> {

        private final EmailAddressGenerationOptions options;

        public OptionsProvider(String... domains) {
            options = new EmailAddressGenerationOptions(domains);
        }

        @Override
        protected EmailAddressGenerationOptions generateObject(
            ObjectQuery query,
            ResolutionContext context
        ) {
            return options;
        }
    }

    @ParameterizedTest
    @AutoSource
    void sut_consumes_domains_option(List<URL> urls, Factory<User> factory) {
        String[] domains = urls.stream()
            .map(URL::getHost)
            .toArray(String[]::new);
        factory.applyCustomizer(new OptionsProvider(domains));

        List<User> actual = factory.stream().limit(100).toList();

        assertThat(domains).allMatch(domain -> actual
            .stream()
            .map(User::emailAddress)
            .anyMatch(email -> email.endsWith(domain)));
    }
}