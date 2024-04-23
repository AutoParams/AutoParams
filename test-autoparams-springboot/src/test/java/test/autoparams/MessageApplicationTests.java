package test.autoparams;

import java.net.URI;
import java.util.Map;

import autoparams.AutoSource;
import autoparams.BrakeBeforeAnnotation;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MessageApplicationTests {

    @ParameterizedTest
    @AutoSource
    @BrakeBeforeAnnotation(Autowired.class)
    void home_index_returns_correct_message(
        String name,
        @Autowired TestRestTemplate client,
        @Autowired MessageSupplier messageSupplier
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
        assert actual != null;
        assertThat(actual).containsKey("message");
        assertThat(actual.get("message"))
            .isEqualTo(messageSupplier.getMessage(name));
    }
}
