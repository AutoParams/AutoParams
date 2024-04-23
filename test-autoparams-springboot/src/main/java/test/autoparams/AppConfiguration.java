package test.autoparams;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public MessageSupplier messageSupplier() {
        return new HelloSupplier();
    }
}
