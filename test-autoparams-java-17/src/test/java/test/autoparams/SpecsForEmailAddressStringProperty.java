package test.autoparams;

import java.net.URL;
import java.util.List;

import autoparams.AutoSource;
import autoparams.ObjectQuery;
import autoparams.Repeat;
import autoparams.ResolutionContext;
import autoparams.generator.EmailAddressGenerationOptions;
import autoparams.generator.Factory;
import autoparams.generator.ObjectGeneratorBase;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForEmailAddressStringProperty {

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
    void sut_generates_email_address_for_email_address_suffix(User entity) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        assertThat(entity.emailAddress().matches(emailRegex)).isTrue();
        assertThat(entity.encodedPassword().matches(emailRegex)).isFalse();
    }

    @SuppressWarnings("RecordComponentName")
    public record Customer(int customerId, String email_address) { }

    @ParameterizedTest
    @AutoSource
    void sut_generates_email_address_for_underscored_email_address_suffix(
        Customer value
    ) {
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

    public record UserEmails(String primary, String secondary) { }

    @ParameterizedTest
    @AutoSource
    @Repeat(10)
    void sut_generates_email_address_for_string_properties_of_type_with_emails_suffix(
        UserEmails emails
    ) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        assertThat(emails.primary().matches(emailRegex)).isTrue();
        assertThat(emails.secondary().matches(emailRegex)).isTrue();
    }

    public record UserEmailAddresses(String primary, String secondary) { }

    @ParameterizedTest
    @AutoSource
    void sut_generates_email_address_for_string_properties_of_type_with_email_addresses_suffix(
        UserEmailAddresses addresses
    ) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        assertThat(addresses.primary().matches(emailRegex)).isTrue();
        assertThat(addresses.secondary().matches(emailRegex)).isTrue();
    }

    @SuppressWarnings("TypeName")
    public record User_Email_Addresses(String primary, String secondary) { }

    @SuppressWarnings("LineLength")
    @ParameterizedTest
    @AutoSource
    void sut_generates_email_address_for_string_properties_of_type_with_underscored_email_addresses_suffix(
        User_Email_Addresses addresses
    ) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        assertThat(addresses.primary().matches(emailRegex)).isTrue();
        assertThat(addresses.secondary().matches(emailRegex)).isTrue();
    }
}
