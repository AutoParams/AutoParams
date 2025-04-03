package test.autoparams.spring;

import java.net.URI;
import java.util.Map;

import autoparams.AutoSource;
import autoparams.ResolutionContext;
import autoparams.customization.Customization;
import autoparams.spring.BeanGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import test.autoparams.HelloSupplier;
import test.autoparams.MessageSupplier;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpecsForBeanGenerator {

    @ParameterizedTest
    @AutoSource
    @Customization(BeanGenerator.class)
    void sut_provides_spring_bean_arguments_correctly(
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

    @Test
    void sut_with_BeanFactory_works_correctly(
        @Autowired BeanFactory beanFactory
    ) {
        var context = new ResolutionContext();
        context.customize(new BeanGenerator(beanFactory));
        MessageSupplier service = context.resolve(MessageSupplier.class);
        assertThat(service).isInstanceOf(HelloSupplier.class);
    }
}
