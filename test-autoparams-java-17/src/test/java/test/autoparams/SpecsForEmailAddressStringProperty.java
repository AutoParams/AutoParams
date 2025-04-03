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

    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    public record SignUp(String userEmail, String password) { }

    @ParameterizedTest
    @AutoSource
    void sut_generates_email_address_for_email_suffix(SignUp command) {
        assertThat(command.userEmail()).matches(EMAIL_REGEX);
        assertThat(command.password()).doesNotMatch(EMAIL_REGEX);
    }

    public record User(int id, String emailAddress, String encodedPassword) { }

    @ParameterizedTest
    @AutoSource
    void sut_generates_email_address_for_email_address_suffix(User entity) {
        assertThat(entity.emailAddress()).matches(EMAIL_REGEX);
        assertThat(entity.encodedPassword()).doesNotMatch(EMAIL_REGEX);
    }

    @SuppressWarnings("RecordComponentName")
    public record Customer(int customerId, String email_address) { }

    @ParameterizedTest
    @AutoSource
    void sut_generates_email_address_for_underscored_email_address_suffix(
        Customer value
    ) {
        assertThat(value.email_address()).matches(EMAIL_REGEX);
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
        assertThat(emails.primary()).matches(EMAIL_REGEX);
        assertThat(emails.secondary()).matches(EMAIL_REGEX);
    }

    public record UserEmailAddresses(String primary, String secondary) { }

    @ParameterizedTest
    @AutoSource
    void sut_generates_email_address_for_string_properties_of_type_with_email_addresses_suffix(
        UserEmailAddresses addresses
    ) {
        assertThat(addresses.primary()).matches(EMAIL_REGEX);
        assertThat(addresses.secondary()).matches(EMAIL_REGEX);
    }

    @SuppressWarnings("TypeName")
    public record User_Email_Addresses(String primary, String secondary) { }

    @SuppressWarnings("LineLength")
    @ParameterizedTest
    @AutoSource
    void sut_generates_email_address_for_string_properties_of_type_with_underscored_email_addresses_suffix(
        User_Email_Addresses addresses
    ) {
        assertThat(addresses.primary()).matches(EMAIL_REGEX);
        assertThat(addresses.secondary()).matches(EMAIL_REGEX);
    }
}
