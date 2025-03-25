package test.autoparams.spring;

import java.net.URI;
import java.util.Map;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import autoparams.spring.ApplicationContextDelegate;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import test.autoparams.MessageSupplier;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpecsForApplicationContextDelegate {

    @ParameterizedTest
    @AutoSource
    @Customization(ApplicationContextDelegate.class)
    void provides_spring_bean_arguments_correctly(
        TestRestTemplate client,
        MessageSupplier messageSupplier,
        String name
    ) {
        // Arrange
        URI path = URI.create("/?name=" + name);

        // Act
        ResponseEntity<Map<String, Object>> response = client.exchange(
            new RequestEntity<>(HttpMethod.GET, path),
            new ParameterizedTypeReference<>() { }
        );
        Map<String, Object> actual = response.getBody();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).containsKey("message");
        assertThat(actual.get("message"))
            .isEqualTo(messageSupplier.getMessage(name));
    }
}
