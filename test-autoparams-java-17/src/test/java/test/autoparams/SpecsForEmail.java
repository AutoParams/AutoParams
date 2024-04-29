package test.autoparams;

import autoparams.AutoSource;
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
}
